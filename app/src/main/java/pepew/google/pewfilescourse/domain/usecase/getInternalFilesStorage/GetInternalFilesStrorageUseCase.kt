package pepew.google.pewfilescourse.domain.usecase.getInternalFilesStorage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pepew.google.pewfilescourse.common.Resource
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.domain.repository.FilesRepository
import java.io.IOException
import javax.inject.Inject

class GetInternalFilesStrorageUseCase @Inject constructor(
    private val repository: FilesRepository
) {
    operator fun invoke(): Flow<Resource<List<InternalStoragePhoto>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getFiles()
            emit(Resource.Success(coins))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data."))
        }
    }
}