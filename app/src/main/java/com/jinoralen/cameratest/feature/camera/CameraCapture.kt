package com.jinoralen.cameratest.feature.camera

import android.content.Context
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    @ImageCapture.CaptureMode captureMode: Int = CAPTURE_MODE_MAXIMIZE_QUALITY,
    userId: String,
    onImageFile: (File?) -> Unit = { }
) {
    Box(modifier = modifier) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()
        var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
        val imageCaptureUseCase by remember {
            mutableStateOf(
                ImageCapture.Builder()
                    .setCaptureMode(captureMode)
                    .build()
            )
        }
        Box {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                }
            )
            CameraButton(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    coroutineScope.launch {
                        val file = imageCaptureUseCase.takePicture(context.executor, userId, context.cacheDir)

                        onImageFile(file)
                    }
                }
            )
        }

        LaunchedEffect(previewUseCase) {
            val cameraProvider = context.getCameraProvider()
            try {
                // Must unbind the use-cases before rebinding them.
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                )
            } catch (ex: Exception) {
                Timber.e("Failed to bind camera use cases", ex)
            }
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            executor
        )
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

suspend fun ImageCapture.takePicture(executor: Executor, userId: String, cacheDir: File): File? {
    val photoFile = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            val timestamp = System.currentTimeMillis()
            File.createTempFile("$userId-$timestamp", ".jpg", cacheDir)
        }.getOrElse { ex ->
            Timber.e( "Failed to create temporary file", ex)
            null
        }
    }

    return photoFile?.let { file ->
        suspendCoroutine { continuation ->
            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            takePicture(
                outputOptions, executor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        continuation.resume(file)
                    }

                    override fun onError(ex: ImageCaptureException) {
                        Timber.e("Image capture failed", ex)
                        continuation.resumeWithException(ex)
                    }
                }
            )
        }
    }
}
