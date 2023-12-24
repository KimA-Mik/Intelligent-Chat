package ru.kima.intelligentchat.data.card.mappers

import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.kima.intelligentchat.data.card.entities.CardListItemEntity
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.card.model.CardEntry

suspend fun CardListItemEntity.toEntry(imageStorage: ImageStorage): CardEntry {
    return CardEntry(
        id = id,
        photoBytes = photoFilePath?.let {
            coroutineScope {
                val job = async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
                    val image = imageStorage.getImage(it)
                    BitmapFactory.decodeByteArray(image, 0, image.size)
                }
                job.await()
            }
        },
        name = name,
        creatorNotes = creatorNotes,
        creator = creator,
        characterVersion = characterVersion
    )
}
