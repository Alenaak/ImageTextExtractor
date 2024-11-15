package com.example.imageextractor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.imageextractor.ui.theme.ImageExtractorTheme
import com.example.imageextractor.ui.theme.ImageTextExtractor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageExtractorTheme{
                Surface(color = MaterialTheme.colors.background) {
                    if (isPermissionGranted()) {
                        ImageTextExtractor()
                    } else {
                        RequestPermissionUI { requestManageExternalStoragePermission() }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recheck permission status when the app comes back to the foreground
        if (isPermissionGranted()) {
            setContent {
                ImageExtractorTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        ImageTextExtractor()  // If permission is granted, show image extractor screen
                    }
                }
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + applicationContext.packageName)
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, refresh UI
                setContent {
                    ImageExtractorTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            ImageTextExtractor() // Show image extractor screen after permission
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun RequestPermissionUI(onRequestPermission: () -> Unit) {
    // Vibrant color scheme
    val buttonColor = Color(0xFF0E1D52) // Dark blue button
    val backgroundColor = Color(0xFF1F2833) // Dark background color
    val textColor = Color.White // White text

    val buttonTextColor = Color.White // Button text color
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Storage permission is required to select an image.", color = textColor)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onRequestPermission() },colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),) {
            Text("Grant Permission",color = buttonTextColor)
        }
    }
}
