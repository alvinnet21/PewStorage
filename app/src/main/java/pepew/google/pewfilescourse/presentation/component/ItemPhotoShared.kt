package pepew.google.pewfilescourse.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto

@Composable
fun ItemPhotoShared(
    sharedStoragePhoto: SharedStoragePhoto,
    modifier: Modifier = Modifier,
) {
    UriImage(
        item = sharedStoragePhoto,
        modifier = modifier
    )
}

@Composable
fun UriImage(
    item: SharedStoragePhoto,
    modifier: Modifier
) {
    AsyncImage(
        modifier = modifier
            .width(150.dp)
            .clickable { },
        model = item.contentUri,
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_image),
        contentDescription = "Shared Photo",
    )
}