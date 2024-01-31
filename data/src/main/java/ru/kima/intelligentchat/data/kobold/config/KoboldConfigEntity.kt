package ru.kima.intelligentchat.data.kobold.config

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.data.KOBOLD_CONFIG_TABLE_NAME
import ru.kima.intelligentchat.data.common.IntListConverter

@Serializable
@Entity(KOBOLD_CONFIG_TABLE_NAME)
@TypeConverters(IntListConverter::class)
data class KoboldConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = String(),
    val temperature: Float = 1f,
    val repetitionPenalty: Float = 1f,
    val repetitionPenaltyRange: Int = 2048,
    val topP: Float = 0.5f,
    val topA: Float = 0.5f,
    val topK: Int = 50,
    val typical: Float = 0.5f,
    val tailFreeSampling: Float = 0.5f,
    val repetitionPenaltySlope: Float = 1f,
    val samplerOrder: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6),
    val mirostat: Int = 0,
    val mirostatTau: Float = 5f,
    val mirostatEta: Float = 0.1f,
    val grammar: String = String()
)
