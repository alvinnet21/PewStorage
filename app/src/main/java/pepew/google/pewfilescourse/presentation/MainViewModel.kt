package pepew.google.pewfilescourse.presentation

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import pepew.google.pewfilescourse.common.Resource
import pepew.google.pewfilescourse.common.sdk29AndUp
import pepew.google.pewfilescourse.domain.usecase.getInternalFilesStorage.GetInternalFilesStrorageUseCase
import pepew.google.pewfilescourse.domain.usecase.getSharedFilesStorage.GetSharedFilesStorageUseCase
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val getInternalFilesStrorageUseCase: GetInternalFilesStrorageUseCase,
    private val getSharedFilesStorageUseCase: GetSharedFilesStorageUseCase
) : ViewModel() {

    private val _internalStorageState = mutableStateOf(InternalStorageListState())
    val internalStorageState: State<InternalStorageListState> = _internalStorageState

    private val _sharedStorageState = mutableStateOf(SharedStorageListState())
    val sharedStorageState: State<SharedStorageListState> = _sharedStorageState

    init {
        getInternalStorage()
        getExternalStorage()
    }

    fun getExternalStorage() {
        println("ALVIN ADA BERAPA GET EXTERNAL")
        getSharedFilesStorageUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _sharedStorageState.value =
                        SharedStorageListState(files = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _sharedStorageState.value = SharedStorageListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _sharedStorageState.value = SharedStorageListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getInternalStorage() {
        getInternalFilesStrorageUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _internalStorageState.value =
                        InternalStorageListState(files = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _internalStorageState.value = InternalStorageListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _internalStorageState.value = InternalStorageListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun savePhotoFiles(
        isPrivate: Boolean,
        bmp: Bitmap
    ): Boolean {
        val isSavedSuccessfully = when {
            isPrivate -> savePhotoToInternalStorage(UUID.randomUUID().toString(), bmp)
            else  -> savePhotoToExternalStorage(UUID.randomUUID().toString(), bmp)
        }
        if (isPrivate) {
            getInternalStorage()
        } else {
            getExternalStorage()
        }
        return isSavedSuccessfully
    }

    private suspend fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }

            try {
                application.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    application.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                } ?: throw IOException("Couldn't create MediaStore entry")
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    private suspend fun savePhotoToInternalStorage(filename: String, bmp: Bitmap?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                application.openFileOutput("$filename.jpg", ComponentActivity.MODE_PRIVATE)
                    .use { stream ->
                        if (bmp != null) {
                            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                                throw IOException("Couldn't save bitmap.")
                            }
                        }
                    }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                application.deleteFile(filename)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}