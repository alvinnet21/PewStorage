package pepew.google.pewfilescourse.domain.repository

import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import java.io.File

interface FilesRepository {

    suspend fun getFiles(): List<InternalStoragePhoto>

}