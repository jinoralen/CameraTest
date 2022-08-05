package com.jinoralen.cameratest.feature.upload

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
fun UploadScreen(navController: NavController) {
    Column() {
        Text("Upload")
        Button(onClick = { navController.popBackStack(Screen.UserId.route, false) }) {
            Text("Click")
        }
    }
}
