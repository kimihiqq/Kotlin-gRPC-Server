package me.kimihiqq.presentation.Intercepter

import io.grpc.*
import me.kimihiqq.grpc.DeleteRequest
import me.kimihiqq.grpc.GetRequest
import me.kimihiqq.grpc.SaveRequest

class KeyValidationInterceptor : ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val listener = next.startCall(call, headers)

        return object : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            override fun onMessage(message: ReqT) {
                if (message is GetRequest && message.key.isBlank()) {
                    call.close(Status.INVALID_ARGUMENT.withDescription("Key는 비어 있을 수 없습니다"), headers)
                    return
                }
                if (message is SaveRequest && message.key.isBlank()) {
                    call.close(Status.INVALID_ARGUMENT.withDescription("Key는 비어 있을 수 없습니다"), headers)
                    return
                }
                if (message is DeleteRequest && message.key.isBlank()) {
                    call.close(Status.INVALID_ARGUMENT.withDescription("Key는 비어 있을 수 없습니다"), headers)
                    return
                }

                super.onMessage(message)
            }
        }
    }
}
