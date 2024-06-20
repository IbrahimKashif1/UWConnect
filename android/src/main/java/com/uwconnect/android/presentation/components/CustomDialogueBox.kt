package com.uwconnect.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uwconnect.android.presentation.ui.*

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit = onCancel,
    confirmButtonText: String = "Yes",
    cancelButtonText: String = "No",
    confirmButtonColor: Color = Red,
    cancelButtonColor: Color = Gray
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkBlue
            ) {
                Column(
                    modifier = Modifier.padding(all = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(title, color = White, fontFamily = PoppinsFamily, fontWeight = FontWeight.SemiBold)
                    Text(message, color = White)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(backgroundColor = cancelButtonColor),
                            shape = RoundedCornerShape(6.dp)
                        ) { Text(cancelButtonText, color = White) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(backgroundColor = confirmButtonColor),
                            shape = RoundedCornerShape(6.dp)
                        ) { Text(confirmButtonText, color = White) }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialogue(
    showDialog: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    singleButton: Boolean = false,
    singleButtonText: String = "OK",
    singleButtonAction: () -> Unit = onDismissRequest,
    confirmButtonText: String = "Yes",
    cancelButtonText: String = "No",
    confirmButtonColor: Color = Red,
    cancelButtonColor: Color = Gray
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkBlue
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .background(DarkBlue),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = title,
                        color = White,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = message,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                        color = White
                    )
                    Row(
                        horizontalArrangement = if (singleButton) Arrangement.Center else Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (!singleButton) {
                            Button(
                                onClick = onDismissRequest,
                                colors = ButtonDefaults.buttonColors(backgroundColor = cancelButtonColor),
                                shape = RoundedCornerShape(6.dp)
                            ) { Text(cancelButtonText, color = White) }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Button(
                            onClick = {
                                singleButtonAction()
                                onDismissRequest()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = if (singleButton) cancelButtonColor else confirmButtonColor),
                            shape = RoundedCornerShape(6.dp)
                        ) { Text(if (singleButton) singleButtonText else confirmButtonText, color = White) }
                    }
                }
            }
        }
    }
}
