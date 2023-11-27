package pepew.google.pewfilescourse.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto

@Composable
fun ItemPhotoShared(
    sharedStoragePhoto: SharedStoragePhoto,
    modifier: Modifier = Modifier,
    deleteSharedStoragePhoto: (SharedStoragePhoto) -> Unit
) {
    UriImage(
        item = sharedStoragePhoto,
        modifier = modifier,
        deleteSharedStoragePhoto
    )
}

@Composable
fun UriImage(
    item: SharedStoragePhoto,
    modifier: Modifier,
    deleteSharedStoragePhoto: (SharedStoragePhoto) -> Unit
) {
    AsyncImage(
        modifier = modifier
            .width(150.dp)
            .clickable { deleteSharedStoragePhoto(item) },
        model = item.contentUri,
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_image),
        contentDescription = "Shared Photo",
    )
}