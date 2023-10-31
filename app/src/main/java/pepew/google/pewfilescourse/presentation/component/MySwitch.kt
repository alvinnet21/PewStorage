package pepew.google.pewfilescourse.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pepew.google.pewfilescourse.domain.model.ToggleableInfo

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