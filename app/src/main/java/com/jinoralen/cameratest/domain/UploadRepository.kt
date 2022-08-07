package com.jinoralen.cameratest.domain

import arrow.core.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import timber.log.Timber
import java.io.File
import javax.inject.Inject


interface UploadRepository {
    suspend fun upload(image: File): Either<UploadError, UploadSuccess>
}

data class UploadError(val error: String)

object UploadSuccess

class UploadRepositoryImpl @Inject constructor(
    retrofit: Retrofit
): UploadRepository {
    private val uploadService = retrofit.create(UploadService::class.java)

    interface UploadService {
        @PUT
        suspend fun upload(
            @Header("Content-Type") mime: String,
            @Body body: RequestBody
        )
    }

    override suspend fun upload(image: File) = withContext(Dispatchers.IO) {
        try {
            val bytes = image.readBytes()

            Timber.d("Image Size - ${bytes.size}")

            // Upload data somewhere
            // val requestBody = bytes.toRequestBody()
            // uploadService.upload("image/jpeg", requestBody)
            delay(2000L) // simulate loading

            Either.Right(UploadSuccess)
        } catch (e: Exception) {
            Either.Left(UploadError(e.toString()))
        }
    }
}
