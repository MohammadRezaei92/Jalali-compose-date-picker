package com.gmail.hamedvakhide.jalalidatepicker

import android.os.Bundle
import android.util.Log
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
import ir.huri.jcal.JalaliCalendar
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val openDialog = remember { mutableStateOf(false) }

            var selectedDate by remember {
                mutableStateOf<JalaliCalendar?>(null)
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
                    val gregorian = selectedDate?.toGregorian()
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Selected Date: ${selectedDate?.year}/${selectedDate?.month}/${selectedDate?.day}\n " +
                            "${gregorian?.get(Calendar.YEAR)}/${gregorian?.get(Calendar.MONTH)}/${
                                gregorian?.get(
                                    Calendar.DAY_OF_MONTH
                                )
                            }",
                        fontSize = 22.sp
                    )

                    JalaliEventView(onSelectDay = {
                        "Event for: $it"
                    })

                    JalaliDatePickerBottomSheet(
                        initialDate = JalaliCalendar().yesterday,
                        openBottomSheet = openDialog,
                        onSelectDay = {
                            "Event for: $it"
                        },
                        onConfirm = {
                            Log.d("Date", "onConfirm: ${it.day} ${it.monthString} ${it.year}")
                            selectedDate = it
                        },
                        onDismiss = {}
                    )
                }
            }
        }
    }
}

