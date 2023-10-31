package pepew.google.pewfilescourse.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto

@Composable
fun ItemPhotoInternal(
    internalStoragePhoto: InternalStoragePhoto,
    modifier: Modifier = Modifier,
    deleteInternalStoragePhoto: (InternalStoragePhoto) -> Unit
) {
    BitmapImage(
        modifier = modifier,
        item = internalStoragePhoto,
        deleteInternalStoragePhoto = deleteInternalStoragePhoto
    )
}

@Composable
fun BitmapImage(
    item: InternalStoragePhoto,
    modifier: Modifier,
    deleteInternalStoragePhoto: (InternalStoragePhoto) -> Unit
) {
    Image(
        modifier = modifier
            .width(200.dp)
            .clickable { deleteInternalStoragePhoto(item) },
        bitmap = item.bmp.asImageBitmap(),
        contentDescription = "Internal Storage Photo",
        contentScale = ContentScale.Crop
    )
}