package com.jinoralen.cameratest.ui.navigation

import android.content.Context
import androidx.navigation.NavBackStackEntry
import java.io.File

const val USER_ID = "userId"
const val IMAGE = "image"

sealed class Screen(val route: String) {
    object UserId: Screen("userId")

    object Camera: Screen("camera/{$USER_ID}") {
        fun createPath(userId: String): String {
            return "camera/$userId"
        }

        fun userId(backStackEntry: NavBackStackEntry): String? {
            return backStackEntry.arguments?.getString(USER_ID)
        }
    }

    object Preview: Screen("preview")

    object Upload: Screen("upload")
}
