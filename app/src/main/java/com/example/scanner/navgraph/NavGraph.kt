package com.example.scanner.navgraph

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.scanner.ui.screens.ocr.OcrScannerHomeScreen
import com.example.scanner.ui.screens.pdfscanner.PdfScannerHomeScreen

@Composable
fun DocScanner(
    modifier: Modifier = Modifier
) {
    ScannerNavigationWrapperUI(
    )

}

@Composable
private fun ScannerNavigationWrapperUI(
) {
    var selectedDestination: ScannerDestination by rememberSaveable {
        mutableStateOf(ScannerDestination.DocScanner)
    }

    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }
    val layoutType = if (windowSize.width >= 1200.dp) {
        NavigationSuiteType.NavigationDrawer
    } else if (windowSize.height < 480.dp) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            currentWindowAdaptiveInfo()
        )
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            ScannerDestination.entries.forEach {
                item(
                    selected = it == selectedDestination,
                    onClick = { selectedDestination = it },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(it.labelRes)
                        )
                    },
                    label = {
                        Text(text = stringResource(it.labelRes))
                    },
                )
            }
        }
    ) {
        when (selectedDestination) {
            ScannerDestination.DocScanner -> PdfScannerHomeScreen() // Display NavHost for PDF Scanner
            ScannerDestination.OcrScanner -> OcrScannerHomeScreen() // Display NavHost for OCR Scanner
        }
    }
}


