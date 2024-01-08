package me.kimihiqq.domain.error

enum class ErrorCode(val code: String, val message: String) {

    KEY_NOT_FOUND("K001", "키에 해당하는 값이 존재하지 않습니다."),
    INVALID_INPUT("K002", "입력값이 유효하지 않습니다."),
    DATABASE_ERROR("K003", "데이터베이스 오류가 발생했습니다.")
}
