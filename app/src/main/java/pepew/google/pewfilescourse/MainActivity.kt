package pepew.google.pewfilescourse

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pepew.google.pewfilescourse.presentation.MainScreen
import pepew.google.pewfilescourse.ui.theme.PewFilesCourseTheme
import java.io.IOException

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
