package me.kimihiqq.domain.error.exception

import me.kimihiqq.domain.error.ErrorCode

class BusinessException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
