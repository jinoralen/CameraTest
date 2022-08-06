package com.jinoralen.cameratest.feature.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
fun CameraScreen(navController: NavController, userId: String) {
    Column() {
        Text("Camera $userId")
        Button(onClick = { navController.navigate(Screen.Preview.route) }) {
            Text("Click")
        }
    }
}
