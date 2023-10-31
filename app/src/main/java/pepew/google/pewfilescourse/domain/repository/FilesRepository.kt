package pepew.google.pewfilescourse.domain.repository

import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto

interface FilesRepository {

    suspend fun getInternalStoragePhotos(): List<InternalStoragePhoto>

    suspend fun getSharedStoragePhotos(): List<SharedStoragePhoto>

}