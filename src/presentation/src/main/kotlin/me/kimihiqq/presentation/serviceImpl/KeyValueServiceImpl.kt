package me.kimihiqq.presentation.serviceImpl

import io.grpc.Status
import io.grpc.stub.StreamObserver
import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.domain.error.exception.BusinessException
import me.kimihiqq.grpc.*

class KeyValueServiceImpl(
    private val keyValueService: KeyValueService
) : KeyValueServiceGrpc.KeyValueServiceImplBase() {

    override fun get(request: GetRequest, responseObserver: StreamObserver<GetResponse>) {
        try {
            val keyValueDto = keyValueService.getKeyValue(request.key)
            val response = GetResponse.newBuilder()
                .setKey(request.key)
                .setValue(keyValueDto?.value ?: "")
                .build()
            responseObserver.onNext(response)
        } catch (e: BusinessException) {
            responseObserver.onError(
                Status.fromCode(Status.Code.NOT_FOUND)
                    .withDescription(e.errorCode.message)
                    .asRuntimeException()
            )
        } finally {
            responseObserver.onCompleted()
        }
    }

    override fun save(request: SaveRequest, responseObserver: StreamObserver<SaveResponse>) {
        try {
            val keyValueDto = KeyValueDto(request.key, request.value)
            val savedKeyValue = keyValueService.saveKeyValue(keyValueDto)
            val response = SaveResponse.newBuilder()
                .setKey(savedKeyValue.key)
                .setValue(savedKeyValue.value ?: "")
                .build()
            responseObserver.onNext(response)
        } catch (e: BusinessException) {
            responseObserver.onError(
                Status.fromCode(Status.Code.INVALID_ARGUMENT)
                    .withDescription(e.errorCode.message)
                    .asRuntimeException()
            )
        } finally {
            responseObserver.onCompleted()
        }
    }
}
