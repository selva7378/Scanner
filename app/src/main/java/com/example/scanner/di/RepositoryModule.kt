package com.example.scanner.di

import com.example.scanner.repository.OfflinePdfDatabaseRepository
import com.example.scanner.repository.PdfDatabaseRepository
import com.example.scanner.roomdb.PdfDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePdfDatabaseRepository(pdfDao: PdfDao): PdfDatabaseRepository {
        return OfflinePdfDatabaseRepository(pdfDao)
    }
}
