package com.example.scanner.repository

import com.example.scanner.roomdb.Pdf
import com.example.scanner.roomdb.PdfDao
import kotlinx.coroutines.flow.Flow

interface PdfDatabaseRepository {
    suspend fun insertPdf(pdfEntity: Pdf)
    fun getAllPdfs(): Flow<List<Pdf>>
}

class OfflinePdfDatabaseRepository(private val pdfDao: PdfDao): PdfDatabaseRepository {
    override suspend fun insertPdf(pdfEntity: Pdf) = pdfDao.insertPdf(pdfEntity)

    override fun getAllPdfs(): Flow<List<Pdf>> = pdfDao.getAllPdfs()
}