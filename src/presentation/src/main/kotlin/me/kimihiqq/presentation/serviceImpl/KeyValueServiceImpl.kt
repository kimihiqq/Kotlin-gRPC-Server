package me.kimihiqq.presentation.serviceImpl

import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.stub.StreamObserver
import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.grpc.*


class KeyValueServiceImpl(
    private val keyValueService: KeyValueService
) : KeyValueServiceGrpc.KeyValueServiceImplBase() {

    override fun get(request: GetRequest, responseObserver: StreamObserver<GetResponse>) {
        val keyValueDto = keyValueService.getKeyValue(request.key)
        val responseBuilder = GetResponse.newBuilder().setKey(request.key)

        if (keyValueDto != null) {
            responseBuilder.setValue(keyValueDto.value ?: "")
        } else {
            responseBuilder.setNoValue(Empty.getDefaultInstance())
        }

        responseObserver.onNext(responseBuilder.build())
        responseObserver.onCompleted()
    }

    override fun save(request: SaveRequest, responseObserver: StreamObserver<SaveResponse>) {
        val keyValueDto = KeyValueDto(request.key, request.value)
        val savedKeyValue = keyValueService.saveKeyValue(keyValueDto)

        if (savedKeyValue != null) {
            val response = SaveResponse.newBuilder()
                .setKey(savedKeyValue.key)
                .setValue(savedKeyValue.value ?: "")
                .build()
            responseObserver.onNext(response)
        } else {
            responseObserver.onError(Status.INTERNAL.withDescription("데이터베이스 오류 발생").asRuntimeException())
        }
        responseObserver.onCompleted()
    }

    override fun delete(request: DeleteRequest, responseObserver: StreamObserver<DeleteResponse>) {
        val result = keyValueService.deleteKeyValue(request.key)

        if (result) {
            val response = DeleteResponse.newBuilder()
                .setKey(request.key)
                .setSuccess(true)
                .build()
            responseObserver.onNext(response)
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("키를 찾을 수 없음").asRuntimeException())
        }
        responseObserver.onCompleted()
    }
}
