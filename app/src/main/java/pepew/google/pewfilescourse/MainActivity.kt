package pepew.google.pewfilescourse

import android.Manifest
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import pepew.google.pewfilescourse.presentation.MainScreen
import pepew.google.pewfilescourse.ui.theme.PewFilesCourseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var contentObserver: ContentObserver
    private val isSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    private val isSdk33 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PewFilesCourseTheme {
                MainScreen()
            }
        }
        initContentObserver()
    }

    private fun initContentObserver() {
        contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
            }
        }
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )
    }

    fun checkPermissions(permission: String): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        val granted = when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                hasPermission || isSdk29
            }

            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                hasPermission || isSdk33
            }

            else -> hasPermission
        }

        return granted
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }

}
