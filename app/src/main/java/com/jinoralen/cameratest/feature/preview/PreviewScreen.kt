package com.jinoralen.cameratest.feature.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jinoralen.cameratest.ui.navigation.Screen
import com.jinoralen.cameratest.ui.theme.CameraTestTheme
import timber.log.Timber
import java.io.File

@Preview
@Composable
fun PreviewScreen_Preview() {
    CameraTestTheme() {
        val navController = rememberNavController()
        val file = File("/dev/null")

        PreviewScreen(navController = navController, image = file)
    }
}


@Composable
fun PreviewScreen(navController: NavController, image: File) {
    Box() {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .build(),
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            contentDescription = "")

        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(start = 32.dp, end = 32.dp, bottom = 64.dp)) {

            Button(
                onClick = {
                    image.delete()
                    Timber.d("Deleted cache image: $image")

                    navController.popBackStack()
                },
            ) {
                Text(text = "Retake")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { navController.navigate(Screen.Upload.createPath(image)) }) {
                Text(text = "Continue")
            }
        }
    }
}
