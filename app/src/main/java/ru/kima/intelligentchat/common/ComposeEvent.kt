package ru.kima.intelligentchat.common

class ComposeEvent<T>(private val _value: T?) {
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