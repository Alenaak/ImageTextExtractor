package com.example.imageextractor.ui.theme

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@Composable
fun ImageTextExtractor() {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var extractedText by remember { mutableStateOf("") }

    val context = LocalContext.current // Only call LocalContext.current inside a @Composable
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            selectedImage = bitmap
        }
    }

    // Vibrant color scheme
    val buttonColor = Color(0xFF0E1D52) // Dark blue button
    val backgroundColor = Color(0xFF1F2833) // Dark background color
    val textColor = Color.White // White text
    val extractedTextColor = Color(0xFF00C853) // Green text for extracted content
    val buttonTextColor = Color.White // Button text color

    // Create a Scrollable Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState()), // Make the column scrollable
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Select Image Button (Narrowed width)
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.8f) // Reduce button width to 80% of screen
                .height(50.dp) // Make buttons a little taller for better look
        ) {
            Text("Select Image", color = buttonTextColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedImage?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Extract Text Button (Narrowed width)
            Button(
                onClick = {
                    extractTextFromImage(context, bitmap) { result ->
                        extractedText = result
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f) // Reduce button width to 80% of screen
                    .height(50.dp)
            ) {
                Text("Extract Text", color = buttonTextColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (extractedText.isNotEmpty()) {
            Text("Extracted Text:", color = textColor, style = MaterialTheme.typography.h6)
            Text(
                extractedText,
                color = extractedTextColor,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth() // Make sure text takes the full width
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Copy Text Button (Narrowed width)
            Button(
                onClick = {
                    copyTextToClipboard(context, extractedText)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxWidth(0.8f) // Reduce button width to 80% of screen
                    .height(50.dp)
            ) {
                Text("Copy Text", color = buttonTextColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clear Screen Button (Narrowed width)
            Button(
                onClick = {
                    // Reset the state to clear the UI
                    selectedImage = null
                    extractedText = ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 5.dp)
                    .fillMaxWidth(0.8f) // Reduce button width to 80% of screen
                    .height(50.dp)
            ) {
                Text("Clear Screen", color = buttonTextColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Function to copy extracted text to clipboard
fun copyTextToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Extracted Text", text)
    clipboard.setPrimaryClip(clip)
}



fun extractTextFromImage(context: Context, bitmap: Bitmap, callback: (String) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromBitmap(bitmap, 0)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            val extractedText = visionText.text
            if (extractedText.isEmpty()) {
                // Show a Toast message if no text was found
                Toast.makeText(context, "No text found in the image", Toast.LENGTH_SHORT).show()
            }
            callback(extractedText) // Pass the extracted text to the callback
        }
        .addOnFailureListener { e ->
            // Handle the failure case
            callback("Error: ${e.message}")
        }
}


