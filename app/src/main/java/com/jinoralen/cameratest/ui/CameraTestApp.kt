package com.jinoralen.cameratest.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.jinoralen.cameratest.feature.camera.CameraScreen
import com.jinoralen.cameratest.feature.preview.PreviewScreen
import com.jinoralen.cameratest.feature.upload.UploadScreen
import com.jinoralen.cameratest.feature.userid.UserIdScreen
import com.jinoralen.cameratest.ui.navigation.IMAGE
import com.jinoralen.cameratest.ui.navigation.Screen
import com.jinoralen.cameratest.ui.navigation.USER_ID
import com.jinoralen.cameratest.ui.theme.CameraTestTheme

@Composable
@ExperimentalPermissionsApi
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

                composable(
                    route = Screen.Camera.route,
                    arguments = listOf(navArgument(USER_ID){ type = NavType.StringType })
                ) {
                    Screen.Camera.userId(it)?.let { userId ->
                        CameraScreen(navController, userId)
                    }
                }

                composable(
                    route = Screen.Preview.route,
                    arguments = listOf(navArgument(IMAGE){ type = NavType.StringType })
                ) {
                    Screen.Preview.image(it, LocalContext.current)?.let { image ->
                        PreviewScreen(navController, image)
                    }
                }

                composable(
                    route = Screen.Upload.route,
                    arguments = listOf(navArgument(IMAGE){ type = NavType.StringType })
                ) {
                    Screen.Upload.image(it, LocalContext.current)?.let { image ->
                        UploadScreen(navController, image)
                    }
                }
            }
        }
    }
}
