package uz.demo.user

enum class ErrorCode(val code: Int) {
    USER_NOT_FOUND(100),
    USER_FOUND(101)
}