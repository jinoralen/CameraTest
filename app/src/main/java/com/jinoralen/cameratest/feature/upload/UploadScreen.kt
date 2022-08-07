package com.jinoralen.cameratest.feature.upload

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jinoralen.cameratest.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.io.File

@Composable
@ExperimentalPermissionsApi
fun UploadScreen(navController: NavController, image: File, viewModel: UploadViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        StoragePermissions {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 32.dp, end = 32.dp, bottom = 128.dp),
                onClick = { viewModel.uploadImage(image) }
            ) {
                Text("Upload")
            }

            HandleUploadEvents(viewModel, navController)
        }
    }
}

@Composable
@ExperimentalPermissionsApi
private fun BoxScope.StoragePermissions(content: @Composable BoxScope.() -> Unit) {
    // Request permissions for storage if it's needed
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val storePermissionsState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )

        if (storePermissionsState.allPermissionsGranted) {
            content()
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textToShow = if (storePermissionsState.shouldShowRationale) {
                    "The storage is required for further work."
                } else {
                    "Storage permission required for saving pictures. Please grant the permission"
                }

                Text(
                    text = textToShow,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { storePermissionsState.launchMultiplePermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    } else {
        content()
    }
}

@Composable
private fun HandleUploadEvents(
    viewModel: UploadViewModel,
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(true) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.eventFlow.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect {
                when (it) {
                    is UploadViewModel.UploadViewModelEvent.Error -> {
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                    }

                    UploadViewModel.UploadViewModelEvent.UploadSuccessful -> {
                        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                    }

                    UploadViewModel.UploadViewModelEvent.StoreSuccessful -> {
                        Toast.makeText(context, "Store Successful", Toast.LENGTH_SHORT).show()
                    }

                    UploadViewModel.UploadViewModelEvent.Success -> {
                        navController.popBackStack(Screen.UserId.route, false)
                    }
                }
            }
        }
    }
}
