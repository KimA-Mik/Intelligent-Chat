package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsDto(
    @SerialName("username") val username: String = "",
    @SerialName("id") val id: Int = 0,
    @SerialName("kudos") val kudos: Double = 0.0,
    @SerialName("concurrency") val concurrency: Int = 0,
    @SerialName("worker_invited") val workerInvited: Int = 0,
    @SerialName("moderator") val moderator: Boolean = false,
    @SerialName("kudos_details") val kudosDetails: KudosDetailsDto = KudosDetailsDto(),
    @SerialName("worker_count") val workerCount: Int = 0,
    @SerialName("worker_ids") val workerIds: List<String> = listOf(),
    @SerialName("sharedkey_ids") val sharedkeyIds: List<String> = listOf(),
    @SerialName("trusted") val trusted: Boolean = false,
    @SerialName("flagged") val flagged: Boolean = false,
    @SerialName("vpn") val vpn: Boolean = false,
    @SerialName("service") val service: Boolean = false,
    @SerialName("special") val special: Boolean = false,
    @SerialName("pseudonymous") val pseudonymous: Boolean = false,
    @SerialName("account_age") val accountAge: Int = 0,
    @SerialName("usage") val usage: UsageDto = UsageDto(),
    @SerialName("contributions") val contributions: ContributionsDto = ContributionsDto(),
    @SerialName("records") val records: RecordsDto = RecordsDto()
)