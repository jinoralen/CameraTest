package com.jinoralen.cameratest.feature.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun CameraScreen(navController: NavController, userId: String) {
    Box(Modifier.fillMaxSize()) {
        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.CAMERA
        )

        when (cameraPermissionState.status) {
            PermissionStatus.Granted -> {
                CameraCapture(
                    modifier = Modifier.fillMaxSize(),
                    onImageFile = { image ->
                        image?.let {
                            navController.navigate(Screen.Preview.createPath(image))
                        }
                    },
                    userId = userId
                )
            }

            is PermissionStatus.Denied -> {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val status = cameraPermissionState.status
                    val textToShow = if (status.shouldShowRationale) {
                        "The camera is required for further work."
                    } else {
                        "Camera permission required for taking pictures. Please grant the permission"
                    }

                    Text(
                        text = textToShow,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Request permission")
                    }
                }
            }
        }
    }
}
