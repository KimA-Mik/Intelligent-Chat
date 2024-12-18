package ru.kima.intelligentchat.domain.horde.model


data class ModelInfo(
    val name: String = String(),
    val count: Int = 0,
    val performance: Double = 0.0,
    val queued: Double = 0.0,
    val eta: Int = 0,
    val maxContextSize: Int = 0,
    val maxResponseLength: Int = 0
) {
    companion object {
        fun fromActiveModel(activeModel: ActiveModel): ModelInfo {
            return ModelInfo(
                name = activeModel.name,
                count = activeModel.count,
                performance = activeModel.performance,
                queued = activeModel.queued,
                eta = activeModel.eta,
                maxContextSize = Int.MAX_VALUE,
                maxResponseLength = Int.MAX_VALUE
            )
        }
    }
}
