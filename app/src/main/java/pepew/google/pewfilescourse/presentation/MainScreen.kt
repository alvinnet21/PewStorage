package pepew.google.pewfilescourse.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pepew.google.pewfilescourse.R
import pepew.google.pewfilescourse.domain.model.StorageType
import pepew.google.pewfilescourse.presentation.component.ItemPhoto

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TitleText(text = "Your Private Photos")
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(10) { item ->
                    ItemPhoto(
                        storageType = StorageType.Internal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            TitleText(text = "Shared Photos")
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(10) { item ->
                    ItemPhoto(
                        storageType = StorageType.Internal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, false),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = modifier.weight(1f),
                onClick = {
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Camera"
                )
            }
            MySwitch(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MySwitch(modifier: Modifier) {
    var switch by remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = false,
                text = "Private"
            )
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = switch.text)
        Spacer(modifier = Modifier.width(10.dp))
        Switch(
            checked = switch.isChecked,
            onCheckedChange = { isChecked ->
                switch = switch.copy(isChecked = isChecked)
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