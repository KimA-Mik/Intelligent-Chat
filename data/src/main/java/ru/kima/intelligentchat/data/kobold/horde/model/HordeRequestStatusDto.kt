package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HordeRequestStatusDto(
    //True when all jobs in this request are done. Else False.
    @SerialName("done") val done: Boolean = false,
    //True when this request caused an internal server error and could not be completed.
    @SerialName("faulted") val faulted: Boolean = false,
    //The amount of finished jobs in this request.
    @SerialName("finished") val finished: Int = 0,
    @SerialName("generations") val generations: List<GenerationDto> = emptyList(),
    //If False, this request will not be able to be completed with the pool of workers currently available.
    @SerialName("is_possible") val isPossible: Boolean = true,
    //The amount of total Kudos this request has consumed until now.
    @SerialName("kudos") val kudos: Double = 0.0,
    //The amount of still processing jobs in this request.
    @SerialName("processing") val processing: Int = 0,
    //The position in the requests queue. This position is determined by relative Kudos amounts.
    @SerialName("queue_position") val queuePosition: Int = 0,
    //The amount of jobs that timed out and had to be restarted or were reported as failed by a worker.
    @SerialName("restarted") val restarted: Int = 0,
    //The expected amount to wait (in seconds) to generate all jobs in this request.
    @SerialName("wait_time") val waitTime: Int = 0,
    //The amount of jobs waiting to be picked up by a worker.
    @SerialName("waiting") val waiting: Int = 0
)

@Serializable
data class GenerationDto(
    @SerialName("gen_metadata") val metadata: List<GenerationMetadataDto> = listOf(),
    //The model which generated this image.
    @SerialName("model") val model: String = String(),
    //The seed which generated this text.
    @SerialName("seed") val seed: Int = 0,
    //OBSOLETE (Use the gen_metadata field). The state of this generation.
    @SerialName("state") val state: String = String(),
    //The generated text.
    @SerialName("text") val text: String = String(),
    //The UUID of the worker which generated this image.
    @SerialName("worker_id") val workerId: String = String(),
    //The name of the worker which generated this image.
    @SerialName("worker_name") val workerName: String = String()
)

@Serializable
data class GenerationMetadataDto(
    //Optionally a reference for the metadata (e.g. a lora ID)
    @SerialName("ref") val ref: String = String(),
    //The relevance of the metadata field
    @SerialName("type") val type: String = String(),
    //The value of the metadata field
    @SerialName("value") val value: String = String()
)