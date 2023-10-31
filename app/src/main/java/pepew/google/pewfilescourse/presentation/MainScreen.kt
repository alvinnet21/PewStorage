package pepew.google.pewfilescourse.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.presentation.component.ItemPhotoInternal
import pepew.google.pewfilescourse.presentation.component.ItemPhotoShared

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    var readGranted = false
    var writeGranted = false
    val context = LocalContext.current
    val state = viewModel.state.value
    val switch = remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = false,
                text = "Private"
            )
        )
    }

    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        readGranted =
            permission[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readGranted
        writeGranted =
            permission[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writeGranted
    }
    viewModel.updateOrRequestPermissions(context, readGranted, writeGranted)

    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            savePhoto(
                context,
                viewModel,
                switch.value.isChecked,
                it
            )
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
                items(state.files) { item ->
                    ItemPhotoInternal(
                        internalStoragePhoto = item,
                        deleteInternalStoragePhoto = {
                            deleteInternalPhoto(viewModel, context, it)
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
                items(10) { item ->
                    ItemPhotoShared()
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
                onClick = { takePhoto.launch() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Camera"
                )
            }
            MySwitch(modifier = Modifier.weight(1f), switch)
        }
    }
}

fun savePhoto(
    context: Context,
    viewModel: MainViewModel,
    isPrivate: Boolean,
    it: Bitmap
) {
    val isSavedSuccessfully = viewModel.savePhotoAndCheckPermission(isPrivate, it)
    if (isSavedSuccessfully) {
        viewModel.getInternalStorage()
        Toast.makeText(context, "Photo saved successfully", Toast.LENGTH_SHORT)
            .show()
    } else {
        Toast.makeText(context, "Failed to save Photo", Toast.LENGTH_SHORT)
            .show()
    }
}


fun deleteInternalPhoto(
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

@Composable
fun MySwitch(modifier: Modifier, switch: MutableState<ToggleableInfo>) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = switch.value.text)
        Spacer(modifier = Modifier.width(10.dp))
        Switch(
            checked = switch.value.isChecked,
            onCheckedChange = { isChecked ->
                switch.value = switch.value.copy(isChecked = isChecked)
            })
    }
}

data class ToggleableInfo(
    val isChecked: Boolean,
    val text: String
)

@Composable
fun TitleText(
    text: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}