package com.example.scanner.navgraph

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.scanner.R


/** Navigation destinations in the app. */
enum class ScannerDestination(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    DocScanner(R.string.tab_doc_scanner, Icons.Default.AccountBox),

    OcrScanner(R.string.tab_ocr_scanner, Icons.Default.AccountCircle),
}