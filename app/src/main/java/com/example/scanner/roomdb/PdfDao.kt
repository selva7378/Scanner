package com.example.scanner.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PdfDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdfEntity: Pdf)

    @Query("SELECT * FROM pdf_table")
    suspend fun getAllPdfs(): List<Pdf>
}
