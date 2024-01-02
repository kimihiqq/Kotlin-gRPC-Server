package me.kimihiqq.domain

data class KeyValue(
    val key: String,
    var value: String?
) {
    init {
        require(key.isNotBlank()) { "키는 비어 있을 수 없습니다" }
        require(key.length <= 50) { "키 길이는 50자를 넘을 수 없습니다" }
        require(key.matches(Regex("[a-zA-Z0-9_]+"))) { "키는 알파벳, 숫자, 밑줄만 포함할 수 있습니다" }
    }
}
