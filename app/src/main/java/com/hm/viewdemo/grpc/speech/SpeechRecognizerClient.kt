package com.hm.viewdemo.grpc.speech

import android.util.Log
import com.hm.viewdemo.grpc.GrpcChannelProvider
import com.hm.viewdemo.grpc.speech.v1.AudioChunk
import com.hm.viewdemo.grpc.speech.v1.RecognitionConfig
import com.hm.viewdemo.grpc.speech.v1.SpeechServiceGrpc
import com.hm.viewdemo.grpc.speech.v1.StreamingRecognizeRequest
import com.hm.viewdemo.grpc.speech.v1.StreamingRecognizeResponse
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.stub.StreamObserver
import java.util.UUID

/**
 * 流式语音识别客户端。
 *
 * 典型调用顺序：
 * ```
 * startSession(zh-CN, listener) { sender ->
 *   // 录音回调中：
 *   sender.sendChunk(bytes, ts, isLast = false)
 *   ...
 *   sender.sendChunk(empty, ts, isLast = true) // 用户松手
 * }
 * ```
 */
class SpeechRecognizerClient(
    private val channel: ManagedChannel = GrpcChannelProvider.get()
) {

    interface Listener {
        /** 中间结果，可多次回调（用于实时显示候选文本）。 */
        fun onPartial(text: String, confidence: Float) = Unit

        /** 最终结果，只回调一次。 */
        fun onFinal(text: String, confidence: Float)

        /** 链路/业务错误。 */
        fun onError(code: Int, msg: String)

        /** 流正常关闭。 */
        fun onCompleted() = Unit
    }

    private var requestObserver: StreamObserver<StreamingRecognizeRequest>? = null

    fun startSession(
        language: String = "zh-CN",
        listener: Listener,
        onReady: (Sender) -> Unit
    ) {
        val sessionId = UUID.randomUUID().toString()
        val stub = SpeechServiceGrpc.newStub(channel)
        val responseObserver = object : StreamObserver<StreamingRecognizeResponse> {
            override fun onNext(value: StreamingRecognizeResponse) {
                if (value.hasError()) {
                    listener.onError(value.error.code, value.error.message.ifBlank { "ASR error" })
                    return
                }
                for (result in value.resultsList) {
                    val alt = result.alternativesList.firstOrNull() ?: continue
                    if (result.isFinal) {
                        listener.onFinal(alt.transcript, alt.confidence)
                    } else {
                        listener.onPartial(alt.transcript, alt.confidence)
                    }
                }
            }

            override fun onError(t: Throwable) {
                val st = Status.fromThrowable(t)
                Log.w("ASR", "stream onError: ${st.code} ${st.description}", t)
                listener.onError(
                    st.code.value(),
                    st.description ?: t.message ?: "unknown gRPC error"
                )
            }

            override fun onCompleted() {
                listener.onCompleted()
            }
        }

        requestObserver = stub.streamingRecognize(responseObserver).also { reqObs ->
            // 首包：发送会话配置
            val config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setAudioChannelCount(1)
                .setLanguageCode(language)
                .setEnableInterimResults(true)
                .setSessionId(sessionId)
                .build()
            reqObs.onNext(
                StreamingRecognizeRequest.newBuilder().setConfig(config).build()
            )
            onReady(Sender())
        }
    }

    inner class Sender internal constructor() {

        fun sendChunk(bytes: ByteArray, timestampMs: Long, isLast: Boolean = false) {
            val chunk = AudioChunk.newBuilder()
                .setContent(ByteString.copyFrom(bytes))
                .setTimestampMs(timestampMs)
                .setIsLast(isLast)
                .build()
            val req = StreamingRecognizeRequest.newBuilder().setAudioChunk(chunk).build()
            requestObserver?.onNext(req)
            if (isLast) {
                runCatching { requestObserver?.onCompleted() }
                requestObserver = null
            }
        }

        fun cancel(reason: String = "user cancel") {
            runCatching {
                requestObserver?.onError(
                    Status.CANCELLED.withDescription(reason).asRuntimeException()
                )
            }
            requestObserver = null
        }
    }
}
