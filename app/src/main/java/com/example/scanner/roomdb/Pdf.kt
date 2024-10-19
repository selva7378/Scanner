package com.example.scanner.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_table")
data class Pdf(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,          // PDF name for display
    val pdfUri: String         // URI of the scanned PDF
)
