package pepew.google.pewfilescourse.presentation

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pepew.google.pewfilescourse.common.Resource
import pepew.google.pewfilescourse.common.sdk29AndUp
import pepew.google.pewfilescourse.domain.usecase.getInternalFilesStorage.GetInternalFilesStrorageUseCase
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val getInternalFilesStrorageUseCase: GetInternalFilesStrorageUseCase
) : ViewModel() {

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

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

    fun savePhotoAndCheckPermission(isPrivate: Boolean, bmp: Bitmap): Boolean {
        val isSavedSuccessfully = when {
            isPrivate -> savePhotoToInternalStorage(UUID.randomUUID().toString(), bmp)
            writePermissionGranted -> savePhotoToExternalStorage(UUID.randomUUID().toString(), bmp)
            else -> false
        }
        if (isPrivate) {
            getInternalStorage()
        }
        return isSavedSuccessfully
    }

    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return try {
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

    fun updateOrRequestPermissions(context: Context, readGranted: Boolean, writeGranted: Boolean) {
        readPermissionGranted = readGranted
        writePermissionGranted = writeGranted
        val hasReadPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val minSdk33 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        readPermissionGranted = hasReadPermission || minSdk33
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionToRequest = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionToRequest.toTypedArray())
        }
    }

    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap?): Boolean {
        return try {
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

    fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return try {
            application.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}