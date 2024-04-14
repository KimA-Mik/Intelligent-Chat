package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkerDto(
    @SerialName("bridge_agent") val bridgeAgent: String = String(),
    @SerialName("contact") val contact: String = String(),
    @SerialName("flagged") val flagged: Boolean = false,
    @SerialName("forms") val forms: List<String> = emptyList(),
    @SerialName("id") val id: String = String(),
    @SerialName("img2img") val img2img: Boolean = false,
    @SerialName("info") val info: String = String(),
    @SerialName("ipaddr") val ipaddr: String = String(),
    @SerialName("kudos_details") val kudosDetails: WorkerKudosDetailsDto = WorkerKudosDetailsDto(),
    @SerialName("kudos_rewards") val kudosRewards: Double = 0.0,
    @SerialName("lora") val lora: Boolean = false,
    @SerialName("maintenance_mode") val maintenanceMode: Boolean = false,
    @SerialName("max_context_length") val maxContextLength: Int = 0,
    @SerialName("max_length") val maxLength: Int = 0,
    @SerialName("max_pixels") val maxPixels: Int = 0,
    @SerialName("megapixelsteps_generated") val megapixelStepsGenerated: Double = 0.0,
    @SerialName("models") val models: List<String> = emptyList(),
    @SerialName("name") val name: String = String(),
    @SerialName("nsfw") val nsfw: Boolean = false,
    @SerialName("online") val online: Boolean = false,
    @SerialName("owner") val owner: String = String(),
    @SerialName("painting") val painting: Boolean = false,
    @SerialName("paused") val paused: Boolean = false,
    @SerialName("performance") val performance: String = String(),
    @SerialName("post-processing") val postProcessing: Boolean = false,
    @SerialName("requests_fulfilled") val requestsFulfilled: Int = 0,
    @SerialName("suspicious") val suspicious: Int = 0,
    @SerialName("team") val team: TeamDto = TeamDto(),
    @SerialName("threads") val threads: Int = 0,
    @SerialName("tokens_generated") val tokensGenerated: Double = 0.0,
    @SerialName("trusted") val trusted: Boolean = false,
    @SerialName("type") val type: String = String(),
    @SerialName("uncompleted_jobs") val uncompletedJobs: Int = 0,
    @SerialName("uptime") val uptime: Int = 0
)