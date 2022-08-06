package com.jinoralen.cameratest.feature.userid

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jinoralen.cameratest.ui.navigation.Screen

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun UserIdScreen(navController: NavController) {
    val controller = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Box(Modifier
        .fillMaxSize()
        .pointerInput(controller) {
            detectTapGestures {
                controller?.hide()
                focusManager.clearFocus()
            }
        }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var userId by remember { mutableStateOf("") }

            TextField(
                value = userId,
                label = { Text(text = "User ID")},
                onValueChange = { userId = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                ),
                keyboardActions = KeyboardActions {
                    controller?.hide()
                    focusManager.clearFocus()
                }
            )

            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = { navController.navigate(Screen.Camera.createPath(userId)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                enabled = userId.isNotEmpty()
            ) {
                Text(
                    text = "Enter",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
