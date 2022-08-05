package com.jinoralen.cameratest.feature.userid

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
fun UserIdScreen(navController: NavController) {
    Column() {
        Text("UserId")
        Button(onClick = { navController.navigate(Screen.Camera.route) }) {
            Text("Click")
        }
    }
}
