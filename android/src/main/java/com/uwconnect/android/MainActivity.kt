package com.uwconnect.android

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.presentation.ui.Black
import com.uwconnect.android.util.Navigation
import com.uwconnect.android.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import android.Manifest
import com.uwconnect.android.util.NotificationManager

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Black.toArgb()
        window.navigationBarColor = Black.toArgb()
        TokenManager.init(this)
        NotificationManager.init { askNotificationPermission() }
        runBlocking {
            val jwt = TokenManager.getJwt()
            var loginType = if (jwt != "null" && jwt.isNotBlank()) TokenManager.parseJwt(jwt).type.uppercase() else ""

            try {
                if (loginType == "MEMBER") {
                    ApiClient.apiService.getUserProfile()
                } else {
                    ApiClient.apiService.getClubProfile()
                }
            } catch (_: Exception) {
                loginType = ""
            }

            setContent {
                Navigation(loginType)
            }
        }
    }
}

