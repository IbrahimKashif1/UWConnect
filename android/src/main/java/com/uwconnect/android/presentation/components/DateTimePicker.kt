package com.uwconnect.android.presentation.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uwconnect.android.presentation.ui.EveningBlue
import com.uwconnect.android.presentation.ui.NightBlue
import com.uwconnect.android.presentation.ui.White
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateTimePicker(
    label: String,
    year: MutableState<Int>,
    month: MutableState<Int>,
    day: MutableState<Int>,
    hour: MutableState<Int>,
    minute: MutableState<Int>,
    context: Context,
    dateString: MutableState<String>,
    timeString: MutableState<String>,
) {
    fun formatTime(hour: Int, minute: Int): String {
        val format = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.time
        return format.format(date)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonthOfYear, selectedDayOfMonth ->
                        year.value = selectedYear
                        month.value = selectedMonthOfYear
                        day.value = selectedDayOfMonth
                        dateString.value = "$selectedDayOfMonth/${selectedMonthOfYear + 1}/$selectedYear"
                    }, year.value, month.value, day.value
                )
                datePickerDialog.show()
            },
            modifier = Modifier
                .height(70.dp)
                .weight(1f)
                .padding(2.dp)
                .border(2.dp, EveningBlue, RoundedCornerShape(6.dp)),
            colors = ButtonDefaults.buttonColors(NightBlue),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(if (dateString.value.isEmpty()) "$label Date" else dateString.value, color = White)
        }

        Button(
            onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        hour.value = selectedHour
                        minute.value = selectedMinute
                        timeString.value = formatTime(selectedHour, selectedMinute)
                    }, hour.value, minute.value, false
                )
                timePickerDialog.show()
            },
            modifier = Modifier
                .height(70.dp)
                .weight(1f)
                .padding(2.dp)
                .border(1.dp, EveningBlue, RoundedCornerShape(6.dp)),
            colors = ButtonDefaults.buttonColors(NightBlue),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(if (timeString.value.isEmpty()) {
                    "$label Time"
                } else {
                    timeString.value
                }
            , color = White)
        }
    }
}
