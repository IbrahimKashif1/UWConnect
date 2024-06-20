package com.uwconnect.android.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uwconnect.android.presentation.ui.Gray

@Composable
fun ColorPicker(colors: List<Color>, selectedColor: MutableState<Color>) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        items(colors.size) { index ->
            val color = colors[index]
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(35.dp)
                    .padding(4.dp)
                    .clickable {
                        selectedColor.value = color
                        Log.d("selected_color", color.value.toString())
                    }
            ) {
                val border = if (selectedColor.value == color) {
                    BorderStroke(2.dp, Gray)
                } else {
                    BorderStroke(1.dp, Color.Transparent)
                }
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = color,
                    border = border
                ) {}
            }
        }
    }
}