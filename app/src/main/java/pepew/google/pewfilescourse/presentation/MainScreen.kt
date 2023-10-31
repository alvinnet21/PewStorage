package pepew.google.pewfilescourse.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import pepew.google.pewfilescourse.MainActivity
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.domain.model.ToggleableInfo
import pepew.google.pewfilescourse.presentation.component.ItemPhotoInternal
import pepew.google.pewfilescourse.presentation.component.ItemPhotoShared
import pepew.google.pewfilescourse.presentation.component.MySwitch
import pepew.google.pewfilescourse.presentation.component.TitleText


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = LocalContext.current as MainActivity

    val permissionLaunch = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        if(permission.isEmpty()) return@rememberLauncherForActivityResult
        val next = permission.entries.iterator().next()
        if (next.key == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (!next.value) {
                Toast.makeText(context, "Can't save shared photos", Toast.LENGTH_LONG)
                    .show()
            }
        }

        val read = permission[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val readImages = permission[Manifest.permission.READ_MEDIA_IMAGES] ?: false
            if (readImages && read) viewModel.getExternalStorage()
            else Toast.makeText(context, "Can't load shared photos", Toast.LENGTH_LONG).show()
        } else {
            if (!read) Toast.makeText(context, "Can't load shared photos", Toast.LENGTH_LONG).show()
        }
    }

    val scope = rememberCoroutineScope()
    val internalStorageState = viewModel.internalStorageState.value
    val sharedStorageState = viewModel.sharedStorageState.value
    val switch = remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = false,
                text = "Private"
            )
        )
    }

    val isGrantedReadExternal = activity.checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
    val listPermission = mutableListOf<String>()
    if (!isGrantedReadExternal) listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val isGrantedReadImages = activity.checkPermissions(Manifest.permission.READ_MEDIA_IMAGES)
        if (!isGrantedReadImages) listPermission.add(Manifest.permission.READ_MEDIA_IMAGES)
    }
    SideEffect { permissionLaunch.launch(listPermission.toTypedArray()) }


    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            scope.launch {
                savePhoto(
                    context,
                    viewModel,
                    switch.value.isChecked,
                    it
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TitleText(text = "Your Private Photos")
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(internalStorageState.files) { item ->
                    ItemPhotoInternal(
                        internalStoragePhoto = item,
                        deleteInternalStoragePhoto = {
                            scope.launch {
                                deleteInternalPhoto(viewModel, context, it)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TitleText(text = "Shared Photos")
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(sharedStorageState.files) { item ->
                    ItemPhotoShared(sharedStoragePhoto = item)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = modifier.weight(1f),
                onClick = {
                    if (!switch.value.isChecked) {
                        val isGranted =
                            activity.checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        if (!isGranted) {
                            val listPermission = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            permissionLaunch.launch(listPermission.toTypedArray())
                        }
                    }
                    takePhoto.launch()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Camera"
                )
            }
            MySwitch(modifier = Modifier.weight(1f), switch)
        }
    }
}

suspend fun savePhoto(
    context: Context,
    viewModel: MainViewModel,
    isPrivate: Boolean,
    it: Bitmap
) {
    val isSavedSuccessfully = viewModel.savePhotoFiles(isPrivate, it)
    if (isSavedSuccessfully) {
        Toast.makeText(context, "Photo saved successfully", Toast.LENGTH_SHORT)
            .show()
    } else {
        Toast.makeText(context, "Failed to save Photo", Toast.LENGTH_SHORT)
            .show()
    }
}

suspend fun deleteInternalPhoto(
    viewModel: MainViewModel,
    context: Context,
    internalStoragePhoto: InternalStoragePhoto
) {
    val isDeleteSuccessfull =
        viewModel.deletePhotoFromInternalStorage(internalStoragePhoto.name)
    if (isDeleteSuccessfull) {
        viewModel.getInternalStorage()
        Toast.makeText(context, "Photo successfully deleted", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Failed to delete Photo", Toast.LENGTH_SHORT).show()
    }
}