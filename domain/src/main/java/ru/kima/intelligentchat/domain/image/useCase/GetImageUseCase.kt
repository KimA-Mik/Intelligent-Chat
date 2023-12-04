package ru.kima.intelligentchat.domain.image.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.image.repository.ImageRepository

class GetImageUseCase(
    private val repository: ImageRepository
) {
    operator fun invoke(fileName: String) = flow<Resource<ByteArray>> {
        emit(Resource.Loading())
        try {
            val image = repository.getImage(fileName)
            emit(Resource.Success(image))
        } catch (e: Exception) {
            emit(Resource.Error("Could not load image"))
        }
    }
}