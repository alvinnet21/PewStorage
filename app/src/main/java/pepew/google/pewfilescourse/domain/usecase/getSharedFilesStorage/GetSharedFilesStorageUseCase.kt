package pepew.google.pewfilescourse.domain.usecase.getSharedFilesStorage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pepew.google.pewfilescourse.common.Resource
import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto
import pepew.google.pewfilescourse.domain.repository.FilesRepository
import java.io.IOException
import javax.inject.Inject

class GetSharedFilesStorageUseCase @Inject constructor(
    private val repository: FilesRepository
) {
    operator fun invoke(): Flow<Resource<List<SharedStoragePhoto>>> = flow {
        try {
            emit(Resource.Loading())
            val photos = repository.getSharedStoragePhotos()
            emit(Resource.Success(photos))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data."))
        }
    }

}