package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkerDto(
    @SerialName("bridge_agent") val bridgeAgent: String,
    @SerialName("contact") val contact: String,
    @SerialName("flagged") val flagged: Boolean,
    @SerialName("forms") val forms: List<String>,
    @SerialName("id") val id: String,
    @SerialName("img2img") val img2img: Boolean,
    @SerialName("info") val info: String,
    @SerialName("ipaddr") val ipaddr: String,
    @SerialName("kudos_details") val kudosDetails: WorkerKudosDetailsDto,
    @SerialName("kudos_rewards") val kudosRewards: Int,
    @SerialName("lora") val lora: Boolean,
    @SerialName("maintenance_mode") val maintenanceMode: Boolean,
    @SerialName("max_context_length") val maxContextLength: Int,
    @SerialName("max_length") val maxLength: Int,
    @SerialName("max_pixels") val maxPixels: Int,
    @SerialName("megapixelsteps_generated") val megapixelStepsGenerated: Int,
    @SerialName("models") val models: List<String>,
    @SerialName("name") val name: String,
    @SerialName("nsfw") val nsfw: Boolean,
    @SerialName("online") val online: Boolean,
    @SerialName("owner") val owner: String,
    @SerialName("painting") val painting: Boolean,
    @SerialName("paused") val paused: Boolean,
    @SerialName("performance") val performance: String,
    @SerialName("post-processing") val postProcessing: Boolean,
    @SerialName("requests_fulfilled") val requestsFulfilled: Int,
    @SerialName("suspicious") val suspicious: Int,
    @SerialName("team") val team: TeamDto,
    @SerialName("threads") val threads: Int,
    @SerialName("tokens_generated") val tokensGenerated: Int,
    @SerialName("trusted") val trusted: Boolean,
    @SerialName("type") val type: String,
    @SerialName("uncompleted_jobs") val uncompletedJobs: Int,
    @SerialName("uptime") val uptime: Int
)