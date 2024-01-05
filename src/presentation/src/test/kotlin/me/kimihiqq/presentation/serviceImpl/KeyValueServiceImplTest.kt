package me.kimihiqq.presentation.serviceImpl

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.domain.error.ErrorCode
import me.kimihiqq.domain.error.exception.BusinessException
import me.kimihiqq.grpc.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.*

fun <T> anyNonNull(): T {
    Mockito.any<T>()
    return null as T
}

class KeyValueServiceImplTest {

    private lateinit var keyValueService: KeyValueService
    private lateinit var keyValueServiceImpl: KeyValueServiceImpl
    private lateinit var getResponseObserver: StreamObserver<GetResponse>
    private lateinit var saveResponseObserver: StreamObserver<SaveResponse>

    @BeforeEach
    fun setUp() {
        keyValueService = mock(KeyValueService::class.java)
        keyValueServiceImpl = KeyValueServiceImpl(keyValueService)
        getResponseObserver = mock(StreamObserver::class.java) as StreamObserver<GetResponse>
        saveResponseObserver = mock(StreamObserver::class.java) as StreamObserver<SaveResponse>
    }

    @Test
    fun `get 메서드가 존재하는 키를 찾을 때 정확한 값을 반환한다`() {
        val request = GetRequest.newBuilder().setKey("testKey").build()
        val keyValueDto = KeyValueDto("testKey", "testValue")
        `when`(keyValueService.getKeyValue(anyString())).thenReturn(keyValueDto)

        keyValueServiceImpl.get(request, getResponseObserver)

        val responseCaptor = ArgumentCaptor.forClass(GetResponse::class.java)
        verify(getResponseObserver).onNext(responseCaptor.capture())
        val response = responseCaptor.value
        assertEquals("testKey", response.key)
        assertEquals("testValue", response.value)
    }

    @Test
    fun `get 메서드가 존재하지 않는 키로 실패한다`() {
        val request = GetRequest.newBuilder().setKey("invalidKey").build()
        `when`(keyValueService.getKeyValue(anyString())).thenThrow(BusinessException(ErrorCode.KEY_NOT_FOUND))

        keyValueServiceImpl.get(request, getResponseObserver)

        val errorCaptor = ArgumentCaptor.forClass(StatusRuntimeException::class.java)
        verify(getResponseObserver).onError(errorCaptor.capture())
        val exception = errorCaptor.value
        assertTrue(exception.status.code == Status.Code.NOT_FOUND)
    }

    @Test
    fun `save 메서드가 정상적으로 작동한다`() {
        val request = SaveRequest.newBuilder().setKey("testKey").setValue("testValue").build()
        val keyValueDto = KeyValueDto("testKey", "testValue")
        `when`(keyValueService.saveKeyValue(anyNonNull())).thenReturn(keyValueDto)

        keyValueServiceImpl.save(request, saveResponseObserver)

        val responseCaptor = ArgumentCaptor.forClass(SaveResponse::class.java)
        verify(saveResponseObserver).onNext(responseCaptor.capture())
        val response = responseCaptor.value
        assertEquals("testKey", response.key)
        assertEquals("testValue", response.value)
    }

    @Test
    fun `save 메서드가 유효하지 않은 데이터로 실패한다`() {
        val request = SaveRequest.newBuilder().setKey("invalidKey").setValue("invalidValue").build()
        `when`(keyValueService.saveKeyValue(anyNonNull())).thenThrow(BusinessException(ErrorCode.INVALID_INPUT))

        keyValueServiceImpl.save(request, saveResponseObserver)

        val errorCaptor = ArgumentCaptor.forClass(StatusRuntimeException::class.java)
        verify(saveResponseObserver).onError(errorCaptor.capture())
        val exception = errorCaptor.value
        assertTrue(exception.status.code == Status.Code.INVALID_ARGUMENT)
    }
}
