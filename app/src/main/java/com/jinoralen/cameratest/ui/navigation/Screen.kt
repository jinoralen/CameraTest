package com.jinoralen.cameratest.ui.navigation

sealed class Screen(val route: String) {
    object UserId: Screen("userId")

    object Camera: Screen("camera")

    object Preview: Screen("preview")

    object Upload: Screen("upload")
}
