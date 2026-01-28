package ru.kima.intelligentchat.common

data class ComposeEvent<T>(
    private val _value: T?,
    private val timestamp: Long = System.currentTimeMillis()
) {
    private var consumed = false
    val value: T?
        get() {
            if (consumed) {
                return null
            }
            consumed = true
            return _value
        }
}