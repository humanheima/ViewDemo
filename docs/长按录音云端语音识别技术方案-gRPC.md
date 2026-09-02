# 长按录音与云端语音识别技术方案（gRPC 流式）

> 适用场景：聊天输入框长按录音 → 端流式上传 PCM 音频 → 云端 ASR 实时/最终识别 → 文本回填输入框
> 协议：gRPC Bidirectional Streaming + Protobuf (proto3)
> 平台：Android 端（Kotlin） + gRPC Service（Java/Kotlin/Go 等）
> 日期：2026-09-02

***

## 1. 方案概述

### 1.1 目标

在现有聊天输入框（`activity_ai_bot.xml` 的 `@id/ai_input_area`）上实现：

- **长按输入框区域** → 开始录音，弹出录音浮层（音量波形、计时、取消上滑提示）

- **松手可取消**：上滑至取消区域松手 → 放弃录音

- **正常松手** → 停止录音，将音频通过 **gRPC 双向流式 RPC** 实时分片上传至云端 ASR 服务

- **流式实时返回** 识别文字，边录边返，结果回填到 `et_ai_message` 输入框

- 录音超时：最长 60s，到时自动停止并识别

### 1.2 技术选型

| 模块               | 选型                                                                            | 说明                                                       |
| ---------------- | ----------------------------------------------------------------------------- | -------------------------------------------------------- |
| 手势交互             | `View.setOnTouchListener` + 自定义长按时序                                           | 区分点击（聚焦输入）与长按（录音）、上滑取消                                   |
| 音频采集             | `AudioRecord` (PCM)                                                           | 原生低延迟，便于分片流式发送；不建议 `MediaRecorder`（已封装格式不利于流式切片）         |
| 音频格式             | **16kHz / 16bit / MONO PCM**                                                  | 业界 ASR 标准输入，阿里云/讯飞/Whisper 均原生支持                         |
| 通信协议             | **gRPC Bidirectional Streaming**                                              | 客户端边录边推 `AudioChunk`，服务端边算边推 `RecognitionResult`，低延迟、带流控 |
| 序列化              | Protobuf (`proto3`)                                                           | 见 §3                                                     |
| gRPC Android 客户端 | `grpc-okhttp` + `grpc-protobuf-lite` + `grpc-stub` + `protobuf-gradle-plugin` | 移动端使用 OkHttp transport 省电省流量，lite 运行时避免 Guava 冲突         |
| 录音 UI 浮层         | 自定义 `VoiceRecordingOverlay` + Lottie 动画                                       | 复用项目已有 `lottie` 依赖 `2.7.0`                               |
| 权限               | `RECORD_AUDIO` + `INTERNET` + `VIBRATE`                                       | 运行时申请，使用现有 `easypermissions`                             |

***

## 2. 总体架构与交互流程

```
┌─────────────────────────────────────────────────────────────┐
│                     Android Client                           │
│                                                              │
│  ┌──────────────┐   ACTION_DOWN   ┌──────────────────────┐   │
│  │ Input Layout │────────────────▶│  GestureDetector     │   │
│  │ (ai_input_   │   (long press)  │  + State Machine     │   │
│  │   area)      │◀────────────────│                      │   │
│  │  +EditText   │   回填识别结果   └─────────┬────────────┘   │
│  └──────┬───────┘                           │                │
│         │ startRecording / stop             │ touch events   │
│         ▼                                   ▼                │
│  ┌──────────────┐  PCM chunk[]   ┌──────────────────────┐   │
│  │ AudioRecord  │───────────────▶│  gRPC Bidi Stream     │   │
│  │ (16k/16/MONO)│                │  (AsyncStub)          │   │
│  └──────────────┘                │  SpeechRecognizer    │   │
│                                  └─────────┬────────────┘   │
│                                            │ stream via TLS │
└────────────────────────────────────────────┼────────────────┘
                                             │
                                     HTTP/2 + mTLS
                                             │
┌────────────────────────────────────────────┼────────────────┐
│                 gRPC Server (云端)         │                │
│                                            ▼                │
│  ┌──────────────────────┐  StreamObserver<AudioChunk>      │
│  │  SpeechServiceImpl   │───────────────────────────       │
│  │                      │                                   │
│  │  1. 流式拼接 PCM     │                                   │
│  │  2. VAD 端点检测     │   onNext(RecognitionResult)       │
│  │  3. 调用 ASR Engine ├──────────────────────────▶        │
│  │     (阿里/讯飞/自研) │                                   │
│  └──────────────────────┘                                   │
└─────────────────────────────────────────────────────────────┘
```

### 2.1 状态机

```
IDLE ──长按(>300ms)──▶ RECORDING
  │                         │
  │                    ACTION_UP 松手
  │                    ┌────┴────┐
  │                    ▼         ▼
  │               (在取消区)   (在录音区)
  │              CANCELLED    STOPPING → RECOGNIZING → SUCCESS
  │                                    (中途可 partial 返回)
  │
  └─ACTION_DOWN<300ms→ ACTION_UP → 视为普通点击，聚焦输入框
```

### 2.2 时序（关键步骤）

```
User            Input Area       AudioRecord     gRPC Stream        Server
 │ACTION_DOWN(L)     │                │               │                │
 │──────────────────▶│                │               │                │
 │                   │ check longPress│               │                │
 │              [300ms]  startRec()   │               │                │
 │                   │───────────────▶│               │                │
 │                   │    show Overlay│               │                │
 │                   │                │  onReady(meta)│ Recognize()     │
 │                   │                │──────────────▶│───────────────▶│
 │                   │                │               │  VAD→ASR init  │
 │  说话中            │                │  read(buffer) │  AudioChunk(n) │
 │                   │                │  loop every   │───────────────▶│
 │                   │                │  20ms~40ms    │                │
 │                   │                │  → chunk40ms  │  partial result │
 │                   │                │──────────────▶│◀───────────────│
 │                   │  显示临时识别文本◀───────────────│                │
 │                   │                │               │   ...          │
 │ ACTION_UP          │                │               │                │
 │──────────────────▶│ stopRec()      │               │                │
 │                   │──────┐         │               │                │
 │                   │      │ release │ AudioChunk    │                │
 │                   │      ▼         │ +EOF(true)    │ final result   │
 │                   │   hideOverlay  │──────────────▶│◀───────────────│
 │                   │◀───────────────│◀──────────────│ close stream   │
 │                   │  回填最终文本   │               │                │
```

***

## 3. Protobuf 接口定义（proto3）

> 文件建议位置：`app/src/main/proto/speech/v1/speech_service.proto`

```protobuf
syntax = "proto3";

package speech.v1;

option java_package = "com.hm.viewdemo.grpc.speech.v1";
option java_multiple_files = true;
option java_outer_classname = "SpeechProto";
option objc_class_prefix = "SPE";

// ============= 请求：客户端 → 服务端，双向流首包元信息 + 后续分片 =============
message StreamingRecognizeRequest {
  oneof payload {
    // 首包：携带配置，必须在第一个 AudioChunk 之前发送
    RecognitionConfig config = 1;
    // 后续包：PCM 分片
    AudioChunk audio_chunk = 2;
  }
}

message RecognitionConfig {
  enum AudioEncoding {
    ENCODING_UNSPECIFIED = 0;
    LINEAR16 = 1;              // 16-bit signed PCM (本方案默认)
  }
  AudioEncoding encoding = 1;
  int32 sample_rate_hertz = 2; // 默认 16000
  int32 audio_channel_count = 3; // 默认 1
  string language_code = 4;     // "zh-CN", "en-US"
  bool enable_interim_results = 5; // 是否返回中间结果(默认 true)
  bool enable_word_time_offsets = 6; // 可选：词级时间戳
  string session_id = 7;         // 用于链路追踪，客户端 UUID
  string user_id = 8;            // 可选：用户标识
}

message AudioChunk {
  bytes content = 1;             // PCM 分片，建议每包 20~100ms 数据
  // 16kHz 16bit mono: 20ms = 16000*2*0.02 = 640 字节
  int64 timestamp_ms = 2;        // 采集相对时间(ms)，用于抖动重排
  bool is_last = 3;              // true 表示结束，服务端可触发最终识别
}

// ============= 响应：服务端 → 客户端，流式返回一次或多次 =============
message StreamingRecognizeResponse {
  repeated StreamingRecognitionResult results = 1;
  SpeechError error = 2;
}

message StreamingRecognitionResult {
  repeated SpeechRecognitionAlternative alternatives = 1;
  bool is_final = 2;             // false=中间结果，true=最终结果
  int32 channel_tag = 3;
}

message SpeechRecognitionAlternative {
  string transcript = 1;         // 识别文本
  float confidence = 2;          // 0~1
  repeated WordInfo words = 3;   // 可选
}

message WordInfo {
  int32 start_ms = 1;
  int32 end_ms = 2;
  string word = 3;
  float confidence = 4;
}

message SpeechError {
  int32 code = 1;                // gRPC Status Code 映射
  string message = 2;
}

// ============= gRPC 服务 =============
service SpeechService {
  // 双向流式：客户端推音频分片，服务端推识别结果
  rpc StreamingRecognize(stream StreamingRecognizeRequest) returns (stream StreamingRecognizeResponse);

  // 可选：单通道非流式，用于短音频(<15s) 一次性上传
  rpc Recognize(RecognizeRequest) returns (RecognizeResponse);
}

message RecognizeRequest {
  RecognitionConfig config = 1;
  bytes audio = 2;               // 完整 PCM 数据
}
message RecognizeResponse {
  repeated SpeechRecognitionResult results = 1;
  SpeechError error = 2;
}
message SpeechRecognitionResult {
  repeated SpeechRecognitionAlternative alternatives = 1;
}
```

***

## 4. Android 客户端实现

### 4.1 依赖引入（build.gradle 变更）

**Project 根** **`build.gradle`** **增加 protobuf 插件 classpath**：

```groovy
buildscript {
    dependencies {
        classpath "com.google.protobuf:protobuf-gradle-plugin:0.9.4"
    }
}
```

**App** **`build.gradle`**：

```groovy
apply plugin: 'com.google.protobuf'

android {
    // ...
    sourceSets {
        main {
            proto {
                srcDir 'src/main/proto'
            }
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.59.0'
        }
    }
    generateProtoTasks {
        all().each { task ->
            task.builtins {
                java { option 'lite' }
            }
            task.plugins {
                grpc { option 'lite' }
            }
        }
    }
}

dependencies {
    // === gRPC ===
    // Android 推荐 OkHttp transport，省电省流量；如需 netty 则换成 grpc-netty-shaded
    implementation 'io.grpc:grpc-okhttp:1.59.0'
    implementation 'io.grpc:grpc-protobuf-lite:1.59.0'
    implementation 'io.grpc:grpc-stub:1.59.0'

    // === 协程 ===
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

    // 权限库已存在 easypermissions，无需新增
}
```

### 4.2 目录结构建议

```
app/src/main/java/com/hm/viewdemo/
  aibot/
    AiBotActivity.kt                     ← 改造点：绑定录音手势
    voice/
      VoiceRecordingOverlay.kt           ← 录音浮层 UI（波形/计时/取消区）
      AudioRecorderManager.kt            ← AudioRecord 封装 + PCM 分片回调
      VoiceInputController.kt            ← 手势 + 状态机 + 编排上层
    grpc/
      GrpcChannelProvider.kt             ← ManagedChannel 单例（含 TLS、拦截器、重试）
      GrpcMetadataInterceptor.kt         ← Header 注入 token/trace-id
      speech/
        SpeechRecognizerClient.kt        ← StreamingRecognize 封装
```

```
app/src/main/proto/speech/v1/
  speech_service.proto
```

### 4.3 核心类设计

#### 4.3.1 `AudioRecorderManager` — 录音核心

```kotlin
/**
 * 使用 AudioRecord 采集 16kHz/16bit/MONO PCM，每 CHUNK_MS 毫秒回调一次分片。
 * 不做任何本地压缩/编码，直接原始字节供给 gRPC。
 */
class AudioRecorderManager(
    private val sampleRate: Int = 16000,
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO,
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
    private val chunkMs: Int = 40, // 每 40ms 回调一片，和 ASR 窗口对齐
) {
    interface Callback {
        fun onReady(bufferSize: Int)
        fun onChunk(bytes: ByteArray, size: Int, timestampMs: Long)
        fun onError(code: Int, msg: String)
        fun onStopped()
    }

    @Volatile private var isRecording = false
    private var audioRecord: AudioRecord? = null
    private var recordThread: Thread? = null

    fun start(callback: Callback) {
        val minBuf = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        val bufferSize = max(minBuf, sampleRate * 2 * chunkMs / 1000 * 4)
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION,
                sampleRate, channelConfig, audioFormat, bufferSize
            ).also {
                check(it.state == AudioRecord.STATE_INITIALIZED) { "AudioRecord init failed" }
                it.startRecording()
            }
        } catch (t: Throwable) {
            callback.onError(-1, "启动录音失败: ${t.message}")
            return
        }
        isRecording = true
        callback.onReady(bufferSize)
        val chunkBytes = sampleRate * 2 * chunkMs / 1000 // 40ms=1280B
        recordThread = thread(start = true, name = "audio-record") {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            val buffer = ByteArray(chunkBytes)
            val startTs = SystemClock.elapsedRealtime()
            try {
                while (isRecording && !Thread.interrupted()) {
                    val n = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (n > 0) {
                        val ts = SystemClock.elapsedRealtime() - startTs
                        callback.onChunk(buffer.copyOf(n), n, ts)
                    }
                }
            } catch (_: InterruptedException) {
            } finally {
                releaseRecord()
                callback.onStopped()
            }
        }
    }

    /**
     * stop = 停止采集并立即回调 onStopped；调用方在 onStopped 之前可再发一帧 is_last=true
     */
    fun stop() {
        isRecording = false
        recordThread?.interrupt()
        recordThread = null
    }

    private fun releaseRecord() {
        audioRecord?.apply {
            runCatching { stop() }
            release()
        }
        audioRecord = null
    }

    /** 计算 RMS(均方根) 转 dB，用于音量波形动画（浮层显示） */
    fun calcRmsDb(bytes: ByteArray, size: Int): Float {
        if (size <= 0) return 0f
        var sum = 0.0
        val step = 2
        var i = 0
        while (i < size) {
            val sample = (bytes[i].toInt() and 0xFF) or (bytes[i + 1].toInt() shl 8)
            sum += (sample * sample).toDouble()
            i += step
        }
        val rms = kotlin.math.sqrt(sum / (size / step))
        val db = 20 * kotlin.math.log10(rms / 32768.0 + 1e-10)
        return db.coerceIn(-60.0, 0.0).toFloat()
    }
}
```

#### 4.3.2 `GrpcChannelProvider` — Channel 单例

```kotlin
object GrpcChannelProvider {
    // 云端地址配置；生产建议走 mTLS + 域名
    private const val HOST = "asr.example.com"
    private const val PORT = 443

    @Volatile private var channel: ManagedChannel? = null

    fun get(): ManagedChannel = channel ?: synchronized(this) {
        channel ?: OkHttpChannelBuilder
            .forAddress(HOST, PORT)
            .useTransportSecurity()           // 生产必开；联调可用 usePlaintext()
            .keepAliveTime(30, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .maxInboundMessageSize(16 * 1024 * 1024)
            .intercept(GrpcMetadataInterceptor())
            .build()
            .also { channel = it }
    }

    fun shutdown() {
        channel?.shutdownNow()
        channel = null
    }
}
```

```kotlin
class GrpcMetadataInterceptor : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        val origin = next.newCall(method, callOptions)
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(origin) {
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                headers.put(Metadata.Key.of("x-auth-token", Metadata.ASCII_STRING_MARSHALLER),
                    AuthRepo.token.orEmpty())
                headers.put(Metadata.Key.of("x-trace-id", Metadata.ASCII_STRING_MARSHALLER),
                    UUID.randomUUID().toString())
                super.start(responseListener, headers)
            }
        }
    }
}
```

#### 4.3.3 `SpeechRecognizerClient` — 流式 ASR gRPC 客户端

```kotlin
class SpeechRecognizerClient(
    private val channel: ManagedChannel = GrpcChannelProvider.get()
) {
    private val stub = SpeechServiceStub.newStub(channel)

    interface Listener {
        fun onPartial(text: String, confidence: Float)   // 中间结果，可多次
        fun onFinal(text: String, confidence: Float)     // 最终结果，1 次
        fun onError(code: Int, msg: String)
        fun onCompleted()
    }

    private var requestObserver: StreamObserver<StreamingRecognizeRequest>? = null

    /**
     * 开启一次识别会话。返回一个 sender：调用者用它发 config / chunk / last。
     */
    fun startSession(
        language: String = "zh-CN",
        listener: Listener,
        onReady: (sender: Sender) -> Unit
    ) {
        val sessionId = UUID.randomUUID().toString()
        val responseObserver = object : StreamObserver<StreamingRecognizeResponse> {
            override fun onNext(value: StreamingRecognizeResponse) {
                value.resultsList.forEach { result ->
                    val alt = result.alternativesList.firstOrNull() ?: return@forEach
                    if (result.isFinal) listener.onFinal(alt.transcript, alt.confidence)
                    else listener.onPartial(alt.transcript, alt.confidence)
                }
                if (value.hasError()) listener.onError(value.error.code, value.error.message)
            }
            override fun onError(t: Throwable) {
                val st = Status.fromThrowable(t)
                listener.onError(st.code.value(), st.description ?: t.message.orEmpty())
            }
            override fun onCompleted() = listener.onCompleted()
        }
        requestObserver = stub.streamingRecognize(responseObserver)

        // 先发送 config 首包
        val config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setSampleRateHertz(16000)
            .setAudioChannelCount(1)
            .setLanguageCode(language)
            .setEnableInterimResults(true)
            .setSessionId(sessionId)
            .build()
        requestObserver!!.onNext(
            StreamingRecognizeRequest.newBuilder().setConfig(config).build()
        )
        onReady(Sender())
    }

    inner class Sender internal constructor() {
        fun sendChunk(bytes: ByteArray, timestampMs: Long, isLast: Boolean = false) {
            val chunk = AudioChunk.newBuilder()
                .setContent(ByteString.copyFrom(bytes))
                .setTimestampMs(timestampMs)
                .setIsLast(isLast)
                .build()
            requestObserver?.onNext(
                StreamingRecognizeRequest.newBuilder().setAudioChunk(chunk).build()
            )
            if (isLast) requestObserver?.onCompleted()
        }

        fun cancel(reason: String = "user cancel") {
            requestObserver?.onError(
                Status.CANCELLED.withDescription(reason).asRuntimeException()
            )
            requestObserver = null
        }
    }
}
```

#### 4.3.4 `VoiceInputController` — 手势 + 状态机 + 业务编排

```kotlin
/**
 * 绑定到 inputRoot (ai_input_area)，处理：
 *   - 短点击 → 走原聚焦逻辑
 *   - 长按 300ms → 进入录音，弹出 overlay
 *   - 滑动位移超过 cancelThreshold 且 y<0 → 松手取消
 *   - 正常松手 → 发送最后一帧 is_last=true，等待 final 回填输入框
 */
class VoiceInputController(
    private val context: Context,
    private val inputRoot: View,                  // ai_input_area
    private val editText: EditText,               // et_ai_message
    private val overlayHost: ViewGroup,           // android.R.content / decorView
    private val lifecycleOwner: LifecycleOwner,
    private val requirePermission: (onGranted: () -> Unit) -> Unit
) {
    enum class State { IDLE, PRESSING, RECORDING, RECOGNIZING, CANCELLED }

    private var state = State.IDLE
    private val longPressMs = 300L
    private val cancelDy = -200   // 上滑超过 200px 松手取消
    private val cancelDx = -200   // 或左滑超过 200px
    private var pressStartX = 0f; private var pressStartY = 0f
    private var currentX = 0f;  private var currentY = 0f

    private val recorder = AudioRecorderManager()
    private val asr = SpeechRecognizerClient()
    private var sender: SpeechRecognizerClient.Sender? = null
    private var overlay: VoiceRecordingOverlay? = null

    private val longPressRunnable = Runnable { enterRecording() }
    private val handler = Handler(Looper.getMainLooper())

    fun attach() {
        inputRoot.setOnTouchListener { _, e -> handleTouch(e) }
    }

    private fun handleTouch(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressStartX = e.rawX; pressStartY = e.rawY
                currentX = e.rawX; currentY = e.rawY
                state = State.PRESSING
                handler.postDelayed(longPressRunnable, longPressMs)
                return true  // 主动消费，保证能收 MOVE / UP
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = e.rawX; currentY = e.rawY
                if (state == State.RECORDING) {
                    val dx = currentX - pressStartX
                    val dy = currentY - pressStartY
                    val inCancel = dy < cancelDy || dx < cancelDx
                    overlay?.setCancelHint(inCancel)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(longPressRunnable)
                when (state) {
                    State.PRESSING -> {
                        state = State.IDLE
                        editText.requestFocus()
                        showIme(editText)
                    }
                    State.RECORDING -> {
                        val dx = currentX - pressStartX
                        val dy = currentY - pressStartY
                        val inCancel = dy < cancelDy || dx < cancelDx
                        if (inCancel) cancelRecording()
                        else stopRecordingAndRecognize()
                    }
                    else -> {}
                }
                state = State.IDLE
            }
        }
        return false
    }

    private fun enterRecording() {
        requirePermission {
            state = State.RECORDING
            overlay = VoiceRecordingOverlay(context).also { overlayHost.addView(it) }
            startRecorderAndStream()
            vibrate(50)
        }
    }

    private fun startRecorderAndStream() {
        asr.startSession(language = "zh-CN", listener = object : SpeechRecognizerClient.Listener {
            override fun onPartial(text: String, confidence: Float) {
                overlay?.updatePartialText(text)
            }
            override fun onFinal(text: String, confidence: Float) {
                val cursor = editText.selectionStart.coerceAtLeast(0)
                val editable = editText.text ?: SpannableStringBuilder()
                editable.insert(cursor, text + " ")
                editText.text = editable
                editText.setSelection(
                    (cursor + text.length + 1).coerceAtMost(editable.length)
                )
            }
            override fun onError(code: Int, msg: String) {
                overlay?.showError(msg)
                lifecycleOwner.lifecycleScope.launch { delay(1000); hideOverlay() }
            }
            override fun onCompleted() = hideOverlay()
        }) { s ->
            sender = s
            recorder.start(object : AudioRecorderManager.Callback {
                override fun onReady(bufferSize: Int) {}
                override fun onChunk(bytes: ByteArray, size: Int, timestampMs: Long) {
                    sender?.sendChunk(bytes, timestampMs, isLast = false)
                    val db = recorder.calcRmsDb(bytes, size)
                    handler.post { overlay?.updateVolume(db) }
                }
                override fun onError(code: Int, msg: String) {
                    sender?.cancel("record_error")
                    handler.post { hideOverlay() }
                }
                override fun onStopped() {}
            })
        }
    }

    private fun stopRecordingAndRecognize() {
        state = State.RECOGNIZING
        overlay?.showRecognizing()
        recorder.stop()
        sender?.sendChunk(ByteArray(0), SystemClock.elapsedRealtime(), isLast = true)
    }

    private fun cancelRecording() {
        state = State.CANCELLED
        recorder.stop()
        sender?.cancel("user_cancel")
        hideOverlay()
    }

    private fun hideOverlay() {
        overlay?.let { overlayHost.removeView(it) }; overlay = null
    }

    private fun vibrate(ms: Long) =
        (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?)?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                it.vibrate(
                    VibrationEffect.createOneShot(
                        ms,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            else it.vibrate(ms)
        }

    private fun showIme(v: View) =
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}
```

权限申请示例（复用 `easypermissions`）：

```kotlin
// AiBotActivity 中
private fun withRecordPermission(block: () -> Unit) {
    val perms = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.INTERNET
    )
    if (EasyPermissions.hasPermissions(this, *perms)) block()
    else EasyPermissions.requestPermissions(
        this, "需要麦克风权限进行语音输入", 1001, *perms
    )
}
```

#### 4.3.5 `VoiceRecordingOverlay` — 录音浮层 UI（简要）

```kotlin
class VoiceRecordingOverlay @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null
) : FrameLayout(ctx, attrs) {
    private val waveView: LottieAnimationView
    private val tvTimer: TextView
    private val tvPartial: TextView
    private val tvHint: TextView

    init {
        setBackgroundColor(Color.parseColor("#99000000"))
        // inflate 自定义布局：居中卡片 + 波形动画 + 计时/提示/临时文本
    }

    /** 音量 dB: -60..0，映射到 0..1 控制 lottie 进度或 scale */
    fun updateVolume(db: Float) {
        val ratio = (db + 60f) / 60f
        waveView.progress = ratio.coerceIn(0f, 1f)
    }

    fun setCancelHint(inCancel: Boolean) {
        tvHint.text = if (inCancel) "松手取消" else "上滑取消"
        tvHint.setTextColor(if (inCancel) Color.RED else Color.WHITE)
    }

    fun updatePartialText(text: String) { tvPartial.text = text }
    fun showRecognizing() { tvHint.text = "识别中..." }
    fun showError(msg: String) { tvHint.text = "出错: $msg" }
}
```

> Layout 建议单独放在 `layout/view_voice_recording_overlay.xml`，复用项目现有 Lottie loading 动画。

### 4.4 接入 `AiBotActivity`

在 `AiBotActivity.bindEvent()` 末尾追加：

```kotlin
override fun bindEvent() {
    // ... 原有 send / editor / textWatcher 不变

    val decorView = window.decorView.findViewById<ViewGroup>(android.R.id.content)
    val voiceController = VoiceInputController(
        context = this,
        inputRoot = binding.aiInputArea,          // LinearLayout ai_input_area
        editText = binding.etAiMessage,
        overlayHost = decorView,
        lifecycleOwner = this,
        requirePermission = { block -> withRecordPermission(block) }
    )
    voiceController.attach()
}
```

***

## 5. gRPC 服务端实现要点（供后端参考）

### 5.1 语言选型

推荐 **Java / Kotlin (Quarkus 或 gRPC Spring Boot Starter)** 或 **Go**；Python 仅适合原型。

### 5.2 服务端核心逻辑

```
onCall(requestStream) {
  1. 等待首个 message，必须是 RecognitionConfig；超时 3s 否则 DEADLINE_EXCEEDED
  2. 根据 language_code 加载对应 ASR pipeline（VAD + 解码器）
  3. 初始化会话上下文（session_id 映射内存结构）
  4. for each chunk in requestStream:
       a. 把 PCM 追加到会话 ring buffer
       b. 按 20~40ms 滑窗做 VAD + 特征
       c. 送入解码器：
          - 有中间输出 → onNext(RecognitionResult{is_final=false})
          - 检测到句尾/静音/IS_LAST → onNext(RecognitionResult{is_final=true})
     end
  5. requestStream onCompleted
     → 刷出最后一帧 final 结果
     → responseStream.onCompleted()
  6. onError → 清理资源；返回 SpeechError
}
```

### 5.3 与第三方 ASR 引擎集成

| 厂商                                             | 对接方式                    | 说明                          |
| ---------------------------------------------- | ----------------------- | --------------------------- |
| 阿里云一句话识别（实时）                                   | 已有 WebSocket / gRPC SDK | 可用 gRPC 转调它们的 SDK           |
| 讯飞实时语音转写                                       | WebSocket               | 同上                          |
| 自研 Whisper.cpp / FunASR                        | 本机进程内调用                 | 最灵活，`LINEAR16 16k mono` 直接喂 |
| Google Cloud Speech-to-Text StreamingRecognize | gRPC 原生                 | 接口几乎和本 proto 一模一样，可 1:1 透传  |

**推荐方案**：服务端 SpeechService 作为「统一网关」，内部路由到合适 ASR 引擎；对外保持本 proto 稳定。

### 5.4 生产级服务端非功能需求

- **背压**：客户端采集 16kHz×2×1 = 32KB/s，非常低；但服务端必须配置每个 stream 最大 inflight（建议 2MB）

- **超时**：`StreamingRecognize` 单次 stream 最大 **120s**；客户端到点自动 onCompleted

- **VAD 静音超时**：连续 3s 无语音 → 自动返回最终结果并 `onCompleted`

- **幂等/重试**：`session_id` + `timestamp_ms` 做去重；客户端重试时用同一 session\_id 即可

- **可观测**：每 session 日志、链路 trace、识别耗时 P99、错误码分布

- **安全**：mTLS、JWT/OAuth2 `x-auth-token`、IP 白名单、WAF

***

## 6. 性能与体验指标

| 指标                        | 目标值               | 备注                                               |
| ------------------------- | ----------------- | ------------------------------------------------ |
| 端上首字时延（长按→第一个 partial 返回） | ≤ 600ms           | 含 +gRPC 握手 + VAD + 首片解码                          |
| 端上最终结果时延（松手→final 到达）     | ≤ 500ms           | 短尾句                                              |
| 录音中断 / 丢片率                | ＜ 0.1%            | AudioRecord 线程优先级 `THREAD_PRIORITY_URGENT_AUDIO` |
| 功耗                        | 录音 + 流式 1 分钟 ≤ 2% | 使用 `grpc-okhttp`；避免前台不必要重渲染                      |
| 内存增量（整个特性）                | ≤ 8 MB            | 浮层 + Lottie + gRPC channel 共用                    |

***

## 7. 错误码与降级

| 场景                                        | 客户端表现                | 策略                                     |
| ----------------------------------------- | -------------------- | -------------------------------------- |
| 无 RECORD\_AUDIO 权限                        | Toast「请开启录音权限」+ 跳转设置 | easypermissions 永久拒绝提示                 |
| 无网络                                       | Toast「无网络连接」         | `ConnectivityManager` 预检               |
| gRPC UNavailable / DEADLINE\_EXCEEDED     | 「连接语音服务失败，请重试」       | 指数退避 1s/2s/4s 重连                       |
| gRPC PERMISSION\_DENIED / UNAUTHENTICATED | 「登录态已失效」             | 触发 token 刷新后重试                         |
| 识别结果 confidence < 0.5                     | 浮层标黄「识别结果可能不准确」      | 仍回填，保留让用户编辑                            |
| 录音超过 60s                                  | 自动停止并触发最终识别          | 客户端 `AudioRecorderManager` 内置 watchdog |
| 上滑取消                                      | 不回填、无提示              | 直接 discard                             |

***

## 8. 风险与 Open Questions

1. **gRPC + OkHttp 在 Android 7.0 以下 TLS 兼容**：若 `minSdkVersion` < 24，需配置 **Conscrypt** 或启用 TLS fallbacks。
   → 行动项：确认根 `build.gradle` 的 `minSdkVersion`，如需支持 21-23 则引入 `conscrypt-android`。
2. **后台录音限制**：Android 11+ 对麦克风后台访问有限制；当前场景是用户显式长按，属于前台，可接受；但如果 UX 允许"切后台继续录"，需引入前台服务 `ForegroundServiceType = microphone`。
3. **流量成本**：1 分钟 PCM 原始流约 32KB/s × 60 = **1.9 MB**，gRPC 头开销 < 1%，基本可接受；如需降到 1/4，可改为 OPUS（需 proto 加 `OPUS` encoding，Android 端引入 `libopus` JNI）。
4. **服务端 ASR 选型**：是接厂商 SDK 还是部署自研 FunASR，需要后端和成本评估确认。
5. **合规**：语音数据是否需用户明示授权、是否存储、是否出境，需要法务确认 `user_id` 与录音日志留存策略。

***

## 9. 交付里程碑

| 阶段           | 交付物                                                                                      | 预估工时 |
| ------------ | ---------------------------------------------------------------------------------------- | ---- |
| M1 接口确认      | proto 评审通过 + 服务端 Mock 实现                                                                 | 0.5d |
| M2 端上录音+手势   | `AudioRecorderManager` + `VoiceRecordingOverlay` + `VoiceInputController` 可在真机演示录音/取消/波形 | 2d   |
| M3 gRPC 打通   | `SpeechRecognizerClient` + Mock 服务端双向流，final 文本回填成功                                      | 1.5d |
| M4 真实 ASR 接入 | 服务端接入 Whisper/阿里云/讯飞，端到端一次通过，含错误码与权限                                                     | 2d   |
| M5 性能与边界     | 断网/弱网/超时/低权限/60s 到时/切后台各 case 全走通                                                        | 1.5d |

***

## 10. 与现有项目文件的最小侵入改动清单

| 位置                                                       | 改动类型                      | 说明                                       |
| -------------------------------------------------------- | ------------------------- | ---------------------------------------- |
| 根 `build.gradle`                                         | 新增 classpath              | protobuf 插件                              |
| `app/build.gradle`                                       | dependencies + protobuf 块 | gRPC、protobuf-lite、协程                    |
| `AndroidManifest.xml`                                    | 新增 `<uses-permission>`    | `RECORD_AUDIO`、`INTERNET`、`VIBRATE`（若没有） |
| `AiBotActivity.kt`                                       | 末尾追加 \~10 行               | 绑定 `VoiceInputController`                |
| 新增 5\~6 个 Kotlin 文件                                      | 新增                        | §4.2 目录结构中的类                             |
| 新增 `src/main/proto/...proto`                             | 新增                        | §3 proto                                 |
| 新增 `layout/view_voice_recording_overlay.xml` + lottie 资源 | 新增                        | 浮层 UI                                    |

