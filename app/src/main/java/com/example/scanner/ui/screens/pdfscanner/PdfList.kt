package com.example.scanner.ui.screens.pdfscanner

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.scanner.MainActivity
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.io.FileOutputStream

//
//@Composable
//fun PdfListScreen() {
//    val options = GmsDocumentScannerOptions.Builder()
//        .setGalleryImportAllowed(true)
//        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
//        .setScannerMode(SCANNER_MODE_FULL)
//        .build()
//    val scanner = GmsDocumentScanning.getClient(options)
//    Surface {
//        var imageUris by rememberSaveable {
//            mutableStateOf<List<Uri>>(emptyList())
//        }
//        val scannerLauncher = rememberLauncherForActivityResult(
//            contract = StartIntentSenderForResult(),
//            onResult = {
//                val result =
//                    GmsDocumentScanningResult.fromActivityResultIntent(it.data)
//                imageUris = result?.pages?.map {it.imageUri} ?: emptyList()
//
//                result?.pdf?.let { pdf->
//                    val fos = FileOutputStream(File(filesDir, "Scan.pdf"))
//                    contentResolver.openInputStream(pdf.uri)?.use { inputStream ->
//                        inputStream.copyTo(fos)
//                    }
//                }
//            }
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            imageUris.forEach { uri ->
//                AsyncImage(
//                    model = uri,
//                    contentDescription = null,
//                    contentScale = ContentScale.FillWidth,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//            Button(
//                onClick = {
//                    scanner.getStartScanIntent(this@MainActivity)
//                        .addOnSuccessListener { intentSender ->
//                            scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
//                        }
//                        .addOnFailureListener {
//                            Toast.makeText(
//                                applicationContext,
//                                it.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                }
//            ) {
//                Text("SCAN PDF")
//            }
//        }
//    }
//}


@Composable
fun PdfListScreen(
    imageUris: List<Uri> // Accept imageUris parameter
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUris.forEach { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


