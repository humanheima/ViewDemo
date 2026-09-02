package com.hm.viewdemo.grpc

import android.util.Log
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.okhttp.OkHttpChannelBuilder
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * gRPC ManagedChannel 单例。
 *
 * 生产建议走 mTLS 域名（useTransportSecurity），
 * 联调阶段可通过 [GrpcConfig] 切换到 `usePlaintext` 的内网地址。
 */
object GrpcChannelProvider {

    private const val TAG = "GrpcChannel"

    @Volatile
    private var channel: ManagedChannel? = null

    fun get(): ManagedChannel = channel ?: synchronized(this) {
        channel ?: buildChannel().also { channel = it }
    }

    /** 手动重建（token 变更 / 网络切换等场景） */
    fun reset() {
        shutdown()
        get()
    }

    fun shutdown() {
        channel?.shutdownNow()
        channel = null
    }

    private fun buildChannel(): ManagedChannel {
        val builder = if (GrpcConfig.usePlaintext) {
            OkHttpChannelBuilder
                .forAddress(GrpcConfig.host, GrpcConfig.port)
                .usePlaintext()
        } else {
            OkHttpChannelBuilder
                .forAddress(GrpcConfig.host, GrpcConfig.port)
                .useTransportSecurity()
        }
        Log.d(TAG, "build channel: ${GrpcConfig.host}:${GrpcConfig.port} plaintext=${GrpcConfig.usePlaintext}")
        return builder
            .keepAliveTime(30, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .keepAliveWithoutCalls(true)
            .maxInboundMessageSize(16 * 1024 * 1024)
            .intercept(GrpcMetadataInterceptor())
            .build()
    }
}

/** gRPC 连接地址配置。
 *
 * 当前默认指向本机 Mock Server（./gradlew :mockserver:run）。
 * - Android 模拟器：10.0.2.2 → 宿主机 localhost
 * - 真机：改成宿主机局域网 IP，或部署后改为真实域名
 */
object GrpcConfig {
    var host: String = "10.0.2.2"
    var port: Int = 50051
    var usePlaintext: Boolean = true
}

/** 简单的 token / 用户信息仓库，接入真实账号体系时替换实现即可 */
object AuthRepo {
    @Volatile
    var token: String? = null

    @Volatile
    var userId: String? = null
}

/**
 * 给每次 gRPC 调用注入：
 * - x-auth-token：登录态（服务端鉴权）
 * - x-trace-id：链路追踪 ID
 */
class GrpcMetadataInterceptor : ClientInterceptor {

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        val origin = next.newCall(method, callOptions)
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(origin) {
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                val authKey = Metadata.Key.of("x-auth-token", Metadata.ASCII_STRING_MARSHALLER)
                val traceKey = Metadata.Key.of("x-trace-id", Metadata.ASCII_STRING_MARSHALLER)
                val uidKey = Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER)
                headers.put(authKey, AuthRepo.token.orEmpty())
                headers.put(traceKey, UUID.randomUUID().toString())
                AuthRepo.userId?.let { headers.put(uidKey, it) }
                super.start(responseListener, headers)
            }
        }
    }
}
