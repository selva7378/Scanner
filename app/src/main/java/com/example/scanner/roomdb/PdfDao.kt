package com.example.scanner.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdfEntity: Pdf)

    @Query("SELECT * FROM pdf_table")
    fun getAllPdfs(): Flow<List<Pdf>>
}
