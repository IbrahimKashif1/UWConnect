package com.uwconnect.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uwconnect.android.presentation.ui.*

@Composable
fun TextEntryModule(
    description: String,
    placeholder: String,
    leadingIcon: ImageVector,
    textValue: String,
    textColor: Color,
    cursorColor: Color,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = description,
            color = textColor,
            fontSize = 12.sp,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
                .border(2.dp, SkyBlue, RoundedCornerShape(10.dp))
                .height(50.dp),
            value = textValue,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = White,
                cursorColor = cursorColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = { newText ->
                if (newText.length <= 60) {
                    onValueChanged(newText)
                }
            },
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = cursorColor
                )
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = cursorColor,
                        modifier = Modifier.clickable {
                            onTrailingIconClick?.invoke()
                        }
                    )
                }
            },
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.body2,
                )
            },
            textStyle = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun MultiLineTextEntryModule(
    description: String,
    placeholder: String,
    leadingIcon: ImageVector,
    textValue: String,
    textColor: Color,
    cursorColor: Color,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 5,
    maxCharCount: Int = 120
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = description,
            color = textColor,
            fontSize = 12.sp,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.Medium
        )
        val updatedValue = if (textValue.length > maxCharCount) textValue.substring(0, maxCharCount) else textValue
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
                .border(2.dp, SkyBlue, RoundedCornerShape(10.dp))
                .heightIn(min = 100.dp, max = 100.dp),
            value = updatedValue,
            onValueChange = { newValue ->
                if (newValue.length <= maxCharCount) {
                    onValueChanged(newValue)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = White,
                cursorColor = cursorColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(15.dp),
            singleLine = false,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Leading Icon",
                    tint = cursorColor
                )
            },
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.body2,
                )
            },
            textStyle = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            maxLines = maxLines,
            visualTransformation = VisualTransformation.None
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
            Text(
                text = "${updatedValue.length} / $maxCharCount",
                color = if (updatedValue.length >= maxCharCount) Color.Red else textColor,
                fontSize = 8.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
