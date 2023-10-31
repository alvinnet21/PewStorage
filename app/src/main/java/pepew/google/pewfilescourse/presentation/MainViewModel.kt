package pepew.google.pewfilescourse.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pepew.google.pewfilescourse.common.Resource
import pepew.google.pewfilescourse.domain.usecase.getInternalFilesStorage.GetInternalFilesStrorageUseCase
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val application: Application,
    private val getInternalFilesStrorageUseCase: GetInternalFilesStrorageUseCase
) : ViewModel() {

    private val _state = mutableStateOf(InternalStorageListState())
    val state: State<InternalStorageListState> = _state

    init {
        getInternalStorage()
    }

    fun getInternalStorage() {
        getInternalFilesStrorageUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = InternalStorageListState(files = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value = InternalStorageListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _state.value = InternalStorageListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap?): Boolean {
        return try {
            Log.e("ALVIN", "savePhotoToInternalStorage: 1")
            application.openFileOutput("$filename.jpg", ComponentActivity.MODE_PRIVATE)
                .use { stream ->
                    if (bmp != null) {
                        if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                            throw IOException("Couldn't save bitmap.")
                        }
                    }
                }
            Log.e("ALVIN", "savePhotoToInternalStorage: 2")
            true
        } catch (e: IOException) {
            Log.e("ALVIN", "savePhotoToInternalStorage: kesini ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return try {
            application.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}