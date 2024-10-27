package com.example.scanner.ui.screens.pdfscanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.io.File
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

//@Composable
//fun PdfDetailScreen(
//    context: Context = LocalContext.current
//) {
//    val pdfViewModel = LocalPdfScannerViewModel.current
//    val pdfUri by pdfViewModel.pdfUri.collectAsStateWithLifecycle()
//    if (pdfUri.isEmpty()) {
//        // Display a message asking the user to select a PDF
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Please select any PDF", style = MaterialTheme.typography.bodyLarge)
//        }
//    } else {
//        val pdfFile = rememberSaveable { File(Uri.parse(pdfUri).path!!) } // Convert URI string to File
//        var pageCount by rememberSaveable { mutableStateOf(0) }
//        var currentPage by rememberSaveable { mutableStateOf(0) }
//        val pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
//
//        LaunchedEffect(Unit) {
//            pageCount = pdfRenderer.pageCount // Get total number of pages
//        }
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Render the current page
//            PdfPageRenderer(pdfRenderer = pdfRenderer, currentPage = currentPage)
//
//            Spacer(modifier = Modifier.height(16.dp))
//            // Page navigation controls
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = { if (currentPage > 0) currentPage-- },
//                    enabled = currentPage > 0
//                ) {
//                    Text("Previous")
//                }
//
//                Text("Page ${currentPage + 1} / $pageCount")
//
//                Button(
//                    onClick = { if (currentPage < pageCount - 1) currentPage++ },
//                    enabled = currentPage < pageCount - 1
//                ) {
//                    Text("Next")
//                }
//            }
//        }
//    }
//}

@Composable
fun PdfDetailScreen(
    context: Context = LocalContext.current
) {
    val pdfViewModel = LocalPdfScannerViewModel.current
    val pdfUri by pdfViewModel.pdfUri.collectAsStateWithLifecycle()

    if (pdfUri.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Please select any PDF", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        val pdfFile = remember(pdfUri) { File(Uri.parse(pdfUri).path!!) }

        // Create PdfRenderer within remember and manage its lifecycle
        val pdfRenderer = remember(pdfUri) {
            PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
        }

        var pageCount by rememberSaveable(pdfUri) { mutableStateOf(pdfRenderer.pageCount) }
        var currentPage by rememberSaveable { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PdfPageRenderer(pdfRenderer = pdfRenderer, currentPage = currentPage)

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentPage > 0) currentPage-- },
                    enabled = currentPage > 0
                ) {
                    Text("Previous")
                }

                Text("Page ${currentPage + 1} / $pageCount")

                Button(
                    onClick = { if (currentPage < pageCount - 1) currentPage++ },
                    enabled = currentPage < pageCount - 1
                ) {
                    Text("Next")
                }
            }
        }

        // Close PdfRenderer when this composable leaves the composition
        DisposableEffect(pdfRenderer) {
            Log.i("pdf", "rendere closed")
            onDispose {
                Log.i("pdf", "dispose closed")
//                if (pdfRenderer != null && pdfRenderer.pageCount > 0) {
//                    pdfRenderer.close()
//                }
            }
        }
    }
}


@Composable
fun PdfPageRenderer(
    pdfRenderer: PdfRenderer,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    val page = remember(currentPage) { pdfRenderer.openPage(currentPage) }
    val bitmap = remember { Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888) }

    // Render the current page into the bitmap
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

    // Convert the bitmap to ImageBitmap
    val imageBitmap = remember { bitmap.asImageBitmap() }

    // Display the rendered image using Image composable
    Image(
        bitmap = imageBitmap,
        contentDescription = "Rendered PDF Page",
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat()) // Maintain the aspect ratio
    )

    // Close the page when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            page.close()
        }
    }
}



