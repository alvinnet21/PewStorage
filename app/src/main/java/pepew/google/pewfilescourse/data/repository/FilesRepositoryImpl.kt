package pepew.google.pewfilescourse.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.domain.repository.FilesRepository
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(
    private val applicationContext: Context
) : FilesRepository {

    override suspend fun getFiles(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = applicationContext.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

}