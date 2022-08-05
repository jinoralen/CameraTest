package com.jinoralen.cameratest.feature.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
fun CameraScreen(navController: NavController) {
    Column() {
        Text("Camera")
        Button(onClick = { navController.navigate(Screen.Preview.route) }) {
            Text("Click")
        }
    }
}
