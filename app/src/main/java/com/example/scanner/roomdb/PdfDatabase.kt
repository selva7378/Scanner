package com.example.scanner.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pdf::class], version = 1, exportSchema = false)
abstract class PdfDatabase : RoomDatabase() {
    abstract fun pdfDao(): PdfDao

    companion object {
        @Volatile
        private var Instance: PdfDatabase? = null

        fun getDatabase(context: Context): PdfDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PdfDatabase::class.java, "news_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

