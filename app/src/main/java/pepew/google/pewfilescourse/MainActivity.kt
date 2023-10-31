package pepew.google.pewfilescourse

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import pepew.google.pewfilescourse.common.sdk29AndUp
import pepew.google.pewfilescourse.presentation.MainScreen
import pepew.google.pewfilescourse.ui.theme.PewFilesCourseTheme
import java.io.IOException

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
