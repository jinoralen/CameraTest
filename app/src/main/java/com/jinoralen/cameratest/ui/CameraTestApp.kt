package com.jinoralen.cameratest.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinoralen.cameratest.feature.camera.CameraScreen
import com.jinoralen.cameratest.feature.preview.PreviewScreen
import com.jinoralen.cameratest.feature.upload.UploadScreen
import com.jinoralen.cameratest.feature.userid.UserIdScreen
import com.jinoralen.cameratest.ui.navigation.Screen
import com.jinoralen.cameratest.ui.theme.CameraTestTheme

@Composable
fun CameraTestApp() {
    CameraTestTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.UserId.route) {
                composable(Screen.UserId.route) {
                    UserIdScreen(navController)
                }

                composable(Screen.Camera.route) {
                    CameraScreen(navController)
                }

                composable(Screen.Preview.route) {
                    PreviewScreen(navController)
                }

                composable(Screen.Upload.route) {
                    UploadScreen(navController)
                }
            }
        }
    }
}
