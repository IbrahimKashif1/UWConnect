package com.uwconnect.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.uwconnect.android.presentation.ui.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthenticationButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onButtonClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = backgroundColor,
            disabledContentColor = contentColor
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(15.dp)
            )
        } else {
            Text(
                text = text,
                fontFamily = PoppinsFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
