package ru.kima.intelligentchat.common

//simple one time event
class Event<T>(private val value: T?) {
    private var consumed = false
    fun consume(body: (T?) -> Unit) {
        if (!consumed) {
            body(value)
        }
        consumed = true
    }
}