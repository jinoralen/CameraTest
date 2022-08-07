package com.jinoralen.cameratest.domain

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import arrow.core.Either
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

interface StoreRepository {
    suspend fun storeImage(file: File): Either<StoreError, StoreSuccess>
}


data class StoreError(val error: String)

object StoreSuccess

class StoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): StoreRepository {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun storeImage(file: File): Either<StoreError, StoreSuccess> = withContext(Dispatchers.IO) {
        try {
            val uri = createImageUri(context, file.name)
                ?: return@withContext Either.Left(StoreError("Couldn't create image at Images"))

            // copy content from cache to destination
            context.contentResolver.openOutputStream(uri, "w")?.use { outputStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            Either.Right(StoreSuccess)
        } catch (e:Exception) {
            Either.Left(StoreError(e.toString()))
        }
    }

    private suspend fun createImageUri(context: Context, filename: String): Uri? {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        return withContext(Dispatchers.IO) {
            val newImage = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            }

            return@withContext context.contentResolver.insert(imageCollection, newImage)
        }
    }
}

