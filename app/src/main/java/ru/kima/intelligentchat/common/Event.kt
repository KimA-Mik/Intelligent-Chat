package ru.kima.intelligentchat.common

//simple one time event
data class Event<T>(
    private val value: T?,
    private val timeOfOccurrence: Long = System.currentTimeMillis()
) {
    private var consumed = false
    fun consume(body: (T) -> Unit) {
        if (!consumed) {
            consumed = true
            value?.let {
                body(value)
            }
        }
    }
}