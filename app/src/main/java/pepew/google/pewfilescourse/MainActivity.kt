package pepew.google.pewfilescourse

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint
import pepew.google.pewfilescourse.presentation.MainScreen
import pepew.google.pewfilescourse.ui.theme.PewFilesCourseTheme
import java.io.IOException
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PewFilesCourseTheme {
                MainScreen()
            }
        }
    }
}
