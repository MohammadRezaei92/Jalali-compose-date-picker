package com.gmail.hamedvakhide.jalalidatepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliDatePickerBottomSheet
import com.gmail.hamedvakhide.compose_jalali_datepicker.JalaliEventView
import com.gmail.hamedvakhide.compose_jalali_datepicker.util.JalaliCalendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val openDialog = remember { mutableStateOf(false) }

            var selectedDate by remember {
                mutableStateOf<Pair<JalaliCalendar,JalaliCalendar?>?>(null)
            }

            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { openDialog.value = true }) {
                        Text(text = "open dialog")
                    }
                    val text = if (selectedDate != null && selectedDate?.second == null)
                        "تاریخ: ${selectedDate!!.first.dayOfWeekDayMonthString}"
                    else if (selectedDate != null)
                        "تاریخ: از ${selectedDate!!.first.dayOfWeekDayMonthString}" +
                                " تا ${selectedDate!!.second!!.dayOfWeekDayMonthString}"
                    else ""

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = text,
                        fontSize = 22.sp
                    )

                    JalaliEventView(onSelectDay = {
                        "Event for: $it"
                    })

                    JalaliDatePickerBottomSheet(
                        initialDate = Pair(null, null),
                        openBottomSheet = openDialog,
                        rangePickerMode = true,
                        onSelectDay = { start, end ->
                            "Event for: $start"
                        },
                        onConfirm = { start, end ->
                            selectedDate = Pair(start, end)
                        },
                        onDismiss = {}
                    )
                }
            }
        }
    }
}

