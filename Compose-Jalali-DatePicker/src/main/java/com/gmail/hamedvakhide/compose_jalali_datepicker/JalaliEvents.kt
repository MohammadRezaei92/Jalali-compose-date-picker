package com.gmail.hamedvakhide.compose_jalali_datepicker

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.PersianCalendarTheme
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.backgroundColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.selectedIconColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.textColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.textColorHighlight
import com.gmail.hamedvakhide.compose_jalali_datepicker.util.FormatHelper
import com.gmail.hamedvakhide.compose_jalali_datepicker.util.LeftToRightLayout
import com.gmail.hamedvakhide.compose_jalali_datepicker.util.PickerType
import com.gmail.hamedvakhide.compose_jalali_datepicker.util.RightToLeftLayout
import ir.huri.jcal.JalaliCalendar
import kotlin.math.ceil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JalaliEventView(
    onSelectDay: (JalaliCalendar) -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.backgroundColor,
    selectedIconColor: Color = MaterialTheme.colorScheme.selectedIconColor,
    todayCircleColor: Color = MaterialTheme.colorScheme.selectedIconColor,
    textColorHighlight: Color = MaterialTheme.colorScheme.textColorHighlight,
    nextPreviousBtnColor: Color = MaterialTheme.colorScheme.textColor,
    dividerColor: Color = MaterialTheme.colorScheme.onBackground,
    yearMonthTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    weekDaysTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    dayNumberTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
) {
    var iconSize: Dp by remember {
        mutableStateOf(43.dp)
    }

    var weekDaysLabelPadding: Dp by remember {
        mutableStateOf(18.dp)
    }
    var yearSelectorHeight: Dp by remember {
        mutableStateOf(280.dp)
    }

    var jalali by remember {
        mutableStateOf(JalaliCalendar())
    }

    val today = JalaliCalendar()
    var selectedDate: JalaliCalendar? by remember {
        mutableStateOf(jalali)
    }

    var pickerType: PickerType by remember {
        mutableStateOf(PickerType.Day)
    }

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Log.d("DAGDAG", "CalendarView: Landscape")
            iconSize = 32.dp
            weekDaysLabelPadding = 9.dp
            yearSelectorHeight = 230.dp
        }

        else -> {
            Log.d("DAGDAG", "CalendarView: Portrait")
        }
    }


    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var firstJomeh: Int
        firstJomeh = 7 - JalaliCalendar(jalali.year, jalali.month, 1).dayOfWeek
        if (JalaliCalendar(jalali.year, jalali.month, 1).dayOfWeek == 7)
            firstJomeh = 7

        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pickerType == PickerType.Day) {
                IconButton(
                    onClick = {
                        if (jalali.month != 12) {
                            jalali = JalaliCalendar(jalali.year, jalali.month + 1, 1)
                        } /*else {
                            JalaliCalendar(jalali.year + 1, 1, 1)
                        }*/
                    },
                    modifier = Modifier.size(iconSize),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = nextPreviousBtnColor
                    )
                }
            }

            TextButton(onClick = {
                pickerType = if (pickerType != PickerType.Month)
                    PickerType.Month
                else
                    PickerType.Day
            }) {
                Text(
                    text = jalali.monthString,
                    style = yearMonthTextStyle
                )
            }

            if (pickerType == PickerType.Day) {
                IconButton(
                    onClick = {
                        if (jalali.month != 1) {
                            jalali = JalaliCalendar(jalali.year, jalali.month - 1, 1)
                        } /*else {
                            JalaliCalendar(jalali.year - 1, 12, 1)
                        }*/
                    },
                    modifier = Modifier.size(iconSize)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "",
                        tint = nextPreviousBtnColor
                    )
                }
            }

        }

        when (pickerType) {
            PickerType.Day -> {
                val pageCount = ceil(jalali.monthLength.toDouble().div(7.0)).toInt()
                val pagerState = rememberPagerState(pageCount = { pageCount })

                LaunchedEffect(key1 = jalali) {
                    pagerState.animateScrollToPage(0)
                }

                RightToLeftLayout {
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        var day = firstJomeh.plus(7.times(page))
                        LeftToRightLayout {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = weekDaysLabelPadding),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (dayIndex in 1..7) {
                                    val selectedDay = day
                                    OutlinedIconButton(
                                        onClick = {
                                            selectedDate =
                                                JalaliCalendar(
                                                    jalali.year,
                                                    jalali.month,
                                                    selectedDay
                                                )
                                            onSelectDay(selectedDate!!)
                                        },
                                        Modifier
                                            .size(40.dp, 80.dp),
                                        border = BorderStroke(
                                            width = 1.5.dp,
                                            color = if (day == today.day && jalali.year == today.year && jalali.month == today.month)
                                                todayCircleColor
                                            else
                                                Color.Transparent
                                        ),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = if (selectedDate != null && day == selectedDate!!.day && jalali.year == selectedDate!!.year && jalali.month == selectedDate!!.month)
                                                selectedIconColor
                                            else
                                                Color.Transparent,
                                        )
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            val dayName = when (dayIndex) {
                                                1 -> "جمعه"
                                                2 -> "5شنبه"
                                                3 -> "4شنبه"
                                                4 -> "3شنبه"
                                                5 -> "2شنبه"
                                                6 -> "1شنبه"
                                                7 -> "شنبه"
                                                else -> ""
                                            }
                                            RightToLeftLayout {
                                                Text(
                                                    text = FormatHelper.toPersianNumber(dayName),
                                                    style = weekDaysTextStyle
                                                )
                                            }
                                            Divider(
                                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                                color = dividerColor
                                            )
                                            Text(
                                                modifier = Modifier.alpha(if (day > 0 && day <= jalali.monthLength) 1F else 0F),
                                                text = FormatHelper.toPersianNumber(day.toString()),
                                                style = dayNumberTextStyle,
                                                color = if (day == today.day && jalali.year == today.year && jalali.month == today.month) {
                                                    textColorHighlight
                                                } else {
                                                    dayNumberTextStyle.color
                                                }
                                            )
                                        }
                                    }
                                    if (day > 0 && day <= jalali.monthLength)
                                        day--
                                }
                            }
                        }
                    }
                }
            }

            PickerType.Month -> {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        style = yearMonthTextStyle,
                        text = "انتخاب ماه"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 4, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "تیر",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                4,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 3, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "خرداد",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                3,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 2, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "اردیبهشت",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                2,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 1, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "فروردین",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                1,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 8, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "آبان",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                8,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 7, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "مهر",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                7,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 6, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "شهریور",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                6,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 5, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "مرداد",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                5,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 12, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "اسفند",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                12,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 11, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "بهمن",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                11,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 10, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "دی",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                10,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                    TextButton(onClick = {
                        jalali = JalaliCalendar(jalali.year, 9, 1)
                        pickerType = PickerType.Day
                    }) {
                        Text(
                            text = "آذر",
                            style = yearMonthTextStyle,
                            color = monthTextColorFun(
                                9,
                                jalali,
                                textColorHighlight,
                                yearMonthTextStyle.color
                            )
                        )
                    }
                }
            }

            PickerType.Year -> {}
        }
    }
}

@Preview
@Composable
fun jalaliEventViewPrev() {
    PersianCalendarTheme {
        JalaliEventView(
            onSelectDay = {}
        )
    }
}