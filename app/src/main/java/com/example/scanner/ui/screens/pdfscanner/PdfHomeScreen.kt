package com.example.scanner.ui.screens.pdfscanner

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi


import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import com.example.scanner.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanner.MainActivity
import com.example.scanner.roomdb.Pdf
import com.example.scanner.viewmodels.PdfScannerViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


enum class DocScannerScreen(@StringRes val title: Int) {
    HOME_SCREEN(title = R.string.pdf_scanner),
}

val LocalPdfScannerViewModel = compositionLocalOf<PdfScannerViewModel> {
    error("No ViewModel provided")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfScannerHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    scannerViewModel: PdfScannerViewModel = hiltViewModel() // Inject ViewModel with Hilt
) {
    val context = LocalContext.current
    val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()
    val scanner = GmsDocumentScanning.getClient(options)

    var imageUris by rememberSaveable { mutableStateOf<List<Uri>>(emptyList()) }
    scannerViewModel.getAllPdf()
    val pdfList by scannerViewModel.pdfList.collectAsStateWithLifecycle()
    var pdfUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showNameDialog by rememberSaveable { mutableStateOf(false) }
    var customPdfName by rememberSaveable { mutableStateOf("") }

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = StartIntentSenderForResult(),
        onResult = { resultIntent ->
            resultIntent?.let { intent ->
                val result = GmsDocumentScanningResult.fromActivityResultIntent(intent.data)
                imageUris = result?.pages?.map { it.imageUri } ?: emptyList()

                result?.pdf?.let { pdf ->
                    pdfUri = pdf.uri
                    showNameDialog = true
                }
            }
        }
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    CompositionLocalProvider(LocalPdfScannerViewModel provides scannerViewModel) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scanner.getStartScanIntent(context as MainActivity)
                            .addOnSuccessListener { intentSender ->
                                scannerLauncher.launch(
                                    IntentSenderRequest.Builder(intentSender).build()
                                )
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Scan document"
                    )
                }
            },
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = DocScannerScreen.HOME_SCREEN.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = DocScannerScreen.HOME_SCREEN.name) {
                    ScannerAppContent(pdfList = pdfList)
                }
            }
        }
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Save PDF") },
            text = {
                Column {
                    Text("Enter a name for the PDF:")
                    TextField(
                        value = customPdfName,
                        onValueChange = { customPdfName = it },
                        label = { Text("PDF Name") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showNameDialog = false
                    pdfUri?.let { uri ->
                        val fos: FileOutputStream
                        try {
                            val pdfFileName = "$customPdfName.pdf"
                            val pdfFile = File(context.filesDir, pdfFileName)

                            fos = FileOutputStream(pdfFile)
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                inputStream.copyTo(fos)
                            }

                            // Get the URI of the saved file
                            val savedPdfUri = pdfFile.toUri()

                            // Store the URI of the saved file in Room
                            val pdfEntity = Pdf(
                                name = customPdfName + ".pdf",
                                pdfUri = savedPdfUri.toString()  // Store the new file's URI
                            )

                            scannerViewModel.insertPdf(pdfEntity)

                            Toast.makeText(context, "PDF saved as $pdfFileName", Toast.LENGTH_LONG).show()

                        } catch (e: Exception) {
                            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showNameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ScannerAppContent(
    pdfList: List<Pdf>,
    modifier: Modifier = Modifier
) {
    val pdfViewModel = LocalPdfScannerViewModel.current
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()


    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                PdfListScreen(
                    pdfList = pdfList ,
                    onPdfClick = { uri ->
                        pdfViewModel.setPdfUri(uri)
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                PdfDetailScreen(
                )
            }
        }
    )
}

