package com.hm.viewdemo.mockserver

import com.hm.viewdemo.grpc.speech.v1.RecognizeRequest
import com.hm.viewdemo.grpc.speech.v1.RecognizeResponse
import com.hm.viewdemo.grpc.speech.v1.SpeechRecognitionAlternative
import com.hm.viewdemo.grpc.speech.v1.SpeechRecognitionResult
import com.hm.viewdemo.grpc.speech.v1.SpeechServiceGrpc
import com.hm.viewdemo.grpc.speech.v1.StreamingRecognitionResult
import com.hm.viewdemo.grpc.speech.v1.StreamingRecognizeRequest
import com.hm.viewdemo.grpc.speech.v1.StreamingRecognizeResponse
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.concurrent.atomic.AtomicInteger

/**
 * Mock gRPC 语音识别服务端。
 *
 * 不接入真实 ASR 引擎，仅根据收到的音频分片数量模拟识别进度：
 * - 每 ~2 秒（约 50 个 40ms 分片）返回一次 partial 中间结果
 * - 收到 is_last=true 时返回 final 最终结果并关闭流
 * - 非流式 Recognize 直接返回固定文本
 *
 * 启动方式：
 *   ./gradlew :mockserver:run
 *   指定端口：./gradlew :mockserver:run --args="50052"
 *
 * Android 模拟器连接地址：10.0.2.2:50051
 * 真机连接地址：宿主机局域网 IP:50051
 */
class MockSpeechServer {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 50051 else 50051

            val server: Server = ServerBuilder.forPort(port)
                .addService(MockSpeechServiceImpl())
                .maxInboundMessageSize(16 * 1024 * 1024)
                .build()

            println()
            println("========================================")
            println("  Mock gRPC ASR Server")
            println("  Port    : $port")
            println("  Proto   : speech.v1.SpeechService")
            println("  Transport: plaintext (no TLS)")
            println("========================================")
            println()
            println("  Android emulator  : 10.0.2.2:$port")
            println("  Real device (WiFi): <this-machine-LAN-IP>:$port")
            println()
            println("Waiting for connections... (Ctrl+C to stop)")
            println()

            Runtime.getRuntime().addShutdownHook(Thread {
                println("\nShutting down mock server...")
                server.shutdown()
            })

            server.start()
            server.awaitTermination()
        }
    }
}

/**
 * SpeechService 实现：模拟流式 + 非流式语音识别。
 */
class MockSpeechServiceImpl : SpeechServiceGrpc.SpeechServiceImplBase() {

    /**
     * 随时间递增的 mock 短语，每 ~2 秒推一条 partial。
     * 收到 is_last 时取最后匹配的短语作为 final。
     */
    private val mockPhrases = listOf(
        "你好",
        "你好，世界",
        "你好，今天天气怎么样",
        "你好，今天天气怎么样，帮我查一下",
        "你好，今天天气怎么样，帮我查一下附近的餐厅"
    )

    // ===================== 双向流式 =====================

    override fun streamingRecognize(
        responseObserver: StreamObserver<StreamingRecognizeResponse>
    ): StreamObserver<StreamingRecognizeRequest> {
        val sessionId = System.nanoTime() and 0xFFFF
        println("\n[Session #$sessionId] --- new streaming session ---")

        return object : StreamObserver<StreamingRecognizeRequest> {
            private val chunkCount = AtomicInteger(0)
            private val totalBytes = AtomicInteger(0)
            private var lastPartialIndex = -1

            override fun onNext(request: StreamingRecognizeRequest) {
                when {
                    request.hasConfig() -> {
                        val cfg = request.config
                        println("[Session #$sessionId] config:" +
                            " lang=${cfg.languageCode}" +
                            " rate=${cfg.sampleRateHertz}Hz" +
                            " interim=${cfg.enableInterimResults}" +
                            " session=${cfg.sessionId}")
                    }

                    request.hasAudioChunk() -> {
                        val chunk = request.audioChunk
                        val n = chunkCount.incrementAndGet()
                        totalBytes.addAndGet(chunk.content.size())

                        if (chunk.isLast) {
                            println("[Session #$sessionId] is_last received" +
                                " (chunk #$n, total ${totalBytes.get()} bytes, ${chunk.timestampMs}ms)")
                            val idx = phraseIndexForChunks(n)
                            sendFinal(mockPhrases[idx])
                            responseObserver.onCompleted()
                            println("[Session #$sessionId] --- session completed ---")
                        } else {
                            // 每 ~2 秒（50 chunks × 40ms）推送一次 partial
                            val idx = phraseIndexForChunks(n)
                            if (idx > lastPartialIndex && idx < mockPhrases.size) {
                                lastPartialIndex = idx
                                val phrase = mockPhrases[idx]
                                println("[Session #$sessionId] chunk #$n" +
                                    " (${totalBytes.get()}B, ${chunk.timestampMs}ms)" +
                                    " -> partial: \"$phrase\"")
                                sendPartial(phrase)
                            }
                        }
                    }
                }
            }

            override fun onError(t: Throwable) {
                println("[Session #$sessionId] client error: ${t.message}")
            }

            override fun onCompleted() {
                println("[Session #$sessionId] client stream completed" +
                    " (total ${chunkCount.get()} chunks, ${totalBytes.get()} bytes)")
                // 客户端可能没发 is_last 就 onCompleted，补发一次 final
                val idx = phraseIndexForChunks(chunkCount.get())
                sendFinal(mockPhrases[idx])
                responseObserver.onCompleted()
            }

            /** chunk count → phrase index: 每 50 chunks (≈2s) 对应一个短语 */
            private fun phraseIndexForChunks(n: Int): Int {
                val secondsApprox = n * 40 / 1000
                return (secondsApprox / 2).coerceIn(0, mockPhrases.size - 1)
            }

            private fun sendPartial(text: String, confidence: Float = 0.7f) {
                val resp = StreamingRecognizeResponse.newBuilder()
                    .addResults(
                        StreamingRecognitionResult.newBuilder()
                            .setIsFinal(false)
                            .addAlternatives(
                                SpeechRecognitionAlternative.newBuilder()
                                    .setTranscript(text)
                                    .setConfidence(confidence)
                                    .build()
                            )
                            .build()
                    )
                    .build()
                responseObserver.onNext(resp)
            }

            private fun sendFinal(text: String, confidence: Float = 0.95f) {
                val resp = StreamingRecognizeResponse.newBuilder()
                    .addResults(
                        StreamingRecognitionResult.newBuilder()
                            .setIsFinal(true)
                            .addAlternatives(
                                SpeechRecognitionAlternative.newBuilder()
                                    .setTranscript(text)
                                    .setConfidence(confidence)
                                    .build()
                            )
                            .build()
                    )
                    .build()
                println("[Session #$sessionId] -> final: \"$text\" (confidence=$confidence)")
                responseObserver.onNext(resp)
            }
        }
    }

    // ===================== 非流式 =====================

    override fun recognize(
        request: RecognizeRequest,
        responseObserver: StreamObserver<RecognizeResponse>
    ) {
        println("\n[Recognize] non-streaming:" +
            " ${request.audio.size()} bytes, lang=${request.config.languageCode}")

        val resp = RecognizeResponse.newBuilder()
            .addResults(
                SpeechRecognitionResult.newBuilder()
                    .addAlternatives(
                        SpeechRecognitionAlternative.newBuilder()
                            .setTranscript("这是一段通过非流式识别返回的测试文本。")
                            .setConfidence(0.95f)
                            .build()
                    )
                    .build()
            )
            .build()
        responseObserver.onNext(resp)
        responseObserver.onCompleted()
        println("[Recognize] -> responded")
    }
}
