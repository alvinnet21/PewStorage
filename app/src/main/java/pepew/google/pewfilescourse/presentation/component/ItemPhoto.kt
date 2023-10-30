package pepew.google.pewfilescourse.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto
import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto
import pepew.google.pewfilescourse.domain.model.StorageType

@Composable
fun ItemPhoto(
    internalStoragePhoto: InternalStoragePhoto? = null,
    sharedStoragePhoto: SharedStoragePhoto? = null,
    storageType: StorageType = StorageType.Internal,
    modifier: Modifier = Modifier
) {
    when (storageType) {
        StorageType.Internal -> {
            if (internalStoragePhoto == null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = storageType.toString()
                )
                return
            }
            BitmapImage(
                item = internalStoragePhoto,
                modifier = modifier
            )
        }

        StorageType.Shared -> {
            if (sharedStoragePhoto == null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = storageType.toString()
                )
                return
            }
            UriImage(
                item = sharedStoragePhoto,
                modifier = modifier
            )
        }
    }
}

@Composable
fun UriImage(
    item: SharedStoragePhoto,
    modifier: Modifier
) {
    AsyncImage(
        modifier = modifier.fillMaxSize(),
        model = item.contentUri,
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_image),
        contentDescription = "Shared Photo",
    )
}

@Composable
fun BitmapImage(
    item: InternalStoragePhoto,
    modifier: Modifier
) {
    Image(
        modifier = modifier.fillMaxSize(),
        bitmap = item.bmp.asImageBitmap(),
        contentDescription = "Internal Storage Photo",
    )
}