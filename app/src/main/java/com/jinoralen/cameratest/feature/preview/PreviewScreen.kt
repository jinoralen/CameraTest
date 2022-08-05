package com.jinoralen.cameratest.feature.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
fun PreviewScreen(navController: NavController) {
    Column() {
        Text("Preview")
        Button(onClick = { navController.navigate(Screen.Upload.route) }) {
            Text("Click")
        }
    }
}
