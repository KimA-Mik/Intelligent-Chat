package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordsDto(
    @SerialName("usage") val usage: UsageDto = UsageDto(),
    @SerialName("contribution") val contribution: ContributionDto = ContributionDto(),
    @SerialName("fulfillment") val fulfillment: FulfillmentDto = FulfillmentDto(),
    @SerialName("request") val request: RequestDto = RequestDto()
)