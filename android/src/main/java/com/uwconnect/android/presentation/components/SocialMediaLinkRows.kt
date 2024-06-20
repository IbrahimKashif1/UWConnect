package com.uwconnect.android.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uwconnect.android.R

fun openUrl(context: Context, url: String) {
    val validUrl = makeValidWebpage(url)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
    context.startActivity(intent)
}

fun makeValidWebpage(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
}

@Composable
fun SocialMediaLinksRow(instagramLink: String?, discordLink: String?, facebookLink: String?) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.padding(top = 5.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (!instagramLink.isNullOrBlank()) {
            IconButton(onClick = { openUrl(context, instagramLink) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(34.dp),
                    tint = Color.Unspecified
                )
            }
        }
        if (!discordLink.isNullOrBlank()) {
            IconButton(onClick = { openUrl(context, discordLink) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_discord),
                    contentDescription = "Discord",
                    modifier = Modifier.size(34.dp),
                    tint = Color.Unspecified
                )
            }
        }
        if (!facebookLink.isNullOrBlank()) {
            IconButton(onClick = { openUrl(context, facebookLink) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(34.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}
