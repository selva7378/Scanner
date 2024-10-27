package com.example.scanner.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.repository.PdfDatabaseRepository
import com.example.scanner.roomdb.Pdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfScannerViewModel @Inject constructor(
    private val scannerDb: PdfDatabaseRepository,
) : ViewModel() {

    private var _pdfUri: MutableStateFlow<String> = MutableStateFlow("")
    val pdfUri: StateFlow<String> = _pdfUri

    private var _pdfList: MutableStateFlow<List<Pdf>> = MutableStateFlow(listOf())
    val pdfList: StateFlow<List<Pdf>> = _pdfList

    fun insertPdf(pdf: Pdf) = viewModelScope.launch {
        scannerDb.insertPdf(pdf)
    }

    fun getAllPdf() = viewModelScope.launch {
        scannerDb.getAllPdfs().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        ).collect{
            _pdfList.value = it
        }
    }

    fun setPdfUri(uri: String) {
        _pdfUri.value = uri
    }
}