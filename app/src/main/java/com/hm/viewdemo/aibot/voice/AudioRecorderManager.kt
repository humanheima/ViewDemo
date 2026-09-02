package com.hm.viewdemo.aibot.voice

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import android.os.SystemClock
import kotlin.concurrent.thread
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.math.log10

/**
 * 基于 [AudioRecord] 的 PCM 音频采集器。
 *
 * 默认参数 16kHz / 16bit / MONO，每 40ms 回调一片，
 * 直接喂给 gRPC 流式上传，不做压缩/编码。
 */
class AudioRecorderManager(
    private val sampleRate: Int = 16000,
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO,
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
    private val chunkMs: Int = 40,
    /** 单次录音最长时长（秒）；到时自动 stop 避免资源泄漏 */
    private val maxDurationSec: Int = 60
) {

    interface Callback {
        fun onReady(bufferSize: Int) = Unit
        fun onChunk(bytes: ByteArray, size: Int, timestampMs: Long)
        fun onMaxDurationReached(totalMs: Long) = Unit
        fun onError(code: Int, msg: String)
        fun onStopped() = Unit
    }

    @Volatile
    private var isRecording = false
    private var audioRecord: AudioRecord? = null
    private var recordThread: Thread? = null

    fun start(callback: Callback) {
        if (isRecording) return
        val minBuf = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        if (minBuf == AudioRecord.ERROR || minBuf == AudioRecord.ERROR_BAD_VALUE) {
            callback.onError(ERROR_BAD_CONFIG, "AudioRecord.getMinBufferSize failed")
            return
        }
        val frameBytes = sampleRate * 2 * chunkMs / 1000   // 16bit mono → ×2 bytes/sample
        val bufferSize = max(minBuf, frameBytes * 4)

        audioRecord = try {
            AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            ).also {
                check(it.state == AudioRecord.STATE_INITIALIZED) { "AudioRecord init state=${it.state}" }
                it.startRecording()
            }
        } catch (t: Throwable) {
            callback.onError(ERROR_INIT, "启动录音失败: ${t.message}")
            return
        }

        isRecording = true
        callback.onReady(bufferSize)

        recordThread = thread(start = true, name = "audio-record") {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            val buffer = ByteArray(frameBytes)
            val startMs = SystemClock.elapsedRealtime()
            val maxMs = maxDurationSec * 1000L
            try {
                while (isRecording && !Thread.interrupted()) {
                    val n = audioRecord?.read(buffer, 0, buffer.size) ?: AudioRecord.ERROR
                    if (n > 0) {
                        val ts = SystemClock.elapsedRealtime() - startMs
                        callback.onChunk(buffer.copyOf(n), n, ts)
                        if (ts >= maxMs) {
                            callback.onMaxDurationReached(ts)
                            break
                        }
                    } else if (n == AudioRecord.ERROR_INVALID_OPERATION
                        || n == AudioRecord.ERROR_BAD_VALUE
                        || n == AudioRecord.ERROR_DEAD_OBJECT
                    ) {
                        callback.onError(ERROR_READ, "AudioRecord.read code=$n")
                        break
                    }
                }
            } catch (_: InterruptedException) {
                // 正常停止
            } finally {
                releaseRecord()
                callback.onStopped()
            }
        }
    }

    /** 同步请求停止；会触发 [Callback.onStopped] 回调 */
    fun stop() {
        isRecording = false
        recordThread?.interrupt()
        recordThread = null
    }

    private fun releaseRecord() {
        audioRecord?.apply {
            runCatching {
                if (recordingState == AudioRecord.RECORDSTATE_RECORDING) stop()
            }
            release()
        }
        audioRecord = null
    }

    /** 计算 RMS → dBFS (范围 -60..0)，用于波形/音量条动画。 */
    fun calcRmsDb(bytes: ByteArray, size: Int): Float {
        if (size <= 1) return -60f
        var sum = 0.0
        var i = 0
        val count = size / 2
        while (i < size - 1) {
            val lo = bytes[i].toInt() and 0xFF
            val hi = bytes[i + 1].toInt()
            val sample = (hi shl 8) or lo
            sum += (sample * sample).toDouble()
            i += 2
        }
        if (count == 0) return -60f
        val rms = sqrt(sum / count)
        val db = 20 * log10(rms / 32768.0 + 1e-10)
        return db.coerceIn(-60.0, 0.0).toFloat()
    }

    companion object {
        const val ERROR_BAD_CONFIG = -1001
        const val ERROR_INIT = -1002
        const val ERROR_READ = -1003
    }
}
