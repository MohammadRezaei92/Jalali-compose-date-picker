package com.gmail.hamedvakhide.compose_jalali_datepicker

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JalaliEventView(
    onSelectDay: (JalaliCalendar) -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.backgroundColor,
    selectedIconColor: Color = MaterialTheme.colorScheme.selectedIconColor,
    todayCircleColor: Color = MaterialTheme.colorScheme.selectedIconColor,
    textColorHighlight: Color = MaterialTheme.colorScheme.textColorHighlight,
    nextPreviousBtnColor: Color = MaterialTheme.colorScheme.textColor,
    headerDividerColor: Color = MaterialTheme.colorScheme.onBackground,
    headerTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    yearMonthTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    weekDaysTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    dayNumberTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    buttonsTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
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
//        mutableStateOf(JalaliCalendar())
    }
    val today = JalaliCalendar()
    var selectedDate: JalaliCalendar? by remember {
        mutableStateOf(jalali)
    }

    var pickerType: PickerType by remember {
        mutableStateOf(PickerType.Day)
    }

//        val coroutineScope = rememberCoroutineScope()
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
                        jalali = if (jalali.month != 12) {
                            JalaliCalendar(jalali.year, jalali.month + 1, 1)
                        } else {
                            JalaliCalendar(jalali.year + 1, 1, 1)
                        }
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
                        jalali = if (jalali.month != 1) {
                            JalaliCalendar(jalali.year, jalali.month - 1, 1)
                        } else {
                            JalaliCalendar(jalali.year - 1, 12, 1)
                        }
                    },
                    modifier = Modifier.size(iconSize)
//                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.dialogNavigationButton)
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
                var jomeh = firstJomeh
                var day = jomeh
                RightToLeftLayout {
                    HorizontalPager(
                        state = rememberPagerState(pageCount = { 5 })
                    ) { page ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = weekDaysLabelPadding),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    text = FormatHelper.toPersianNumber("شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = FormatHelper.toPersianNumber("1شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = FormatHelper.toPersianNumber("2شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = FormatHelper.toPersianNumber("3شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = FormatHelper.toPersianNumber("4شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = FormatHelper.toPersianNumber("5شنبه"),
                                    style = weekDaysTextStyle
                                )
                                Text(
                                    text = "جمعه",
                                    style = weekDaysTextStyle
                                )
                            }

                            LeftToRightLayout {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    val week = if (page > 0)
                                        7
                                    else
                                        firstJomeh

                                    day = jomeh.plus(if (page > 0) week else 0)
                                    for (i in 7 downTo 1) {
                                        if (day > 0 && day <= jalali.monthLength) {
                                            val selectDay = day

                                            OutlinedIconButton(
                                                onClick = {
                                                    selectedDate =
                                                        JalaliCalendar(
                                                            jalali.year,
                                                            jalali.month,
                                                            selectDay
                                                        )
                                                    onSelectDay(selectedDate!!)
                                                },
                                                Modifier.size(iconSize),
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

                                                Text(
                                                    text = FormatHelper.toPersianNumber(day.toString()),
                                                    style = dayNumberTextStyle,
                                                    color = if (day == today.day && jalali.year == today.year && jalali.month == today.month) {
                                                        textColorHighlight
                                                    } else {
                                                        dayNumberTextStyle.color
                                                    }
                                                )
                                            }

                                        } else {
                                            FilledIconButton(
                                                onClick = {},
                                                Modifier
                                                    .size(iconSize)
                                                    .alpha(0f),
                                                colors = IconButtonDefaults.filledIconButtonColors(
                                                    containerColor = Color.Transparent
                                                )
                                            ) {
                                                Text(
                                                    text = day.toString(),
                                                    style = dayNumberTextStyle
                                                )
                                            }
                                        }
                                        day--
                                    }
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

            PickerType.Year -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = yearMonthTextStyle,
                        color = MaterialTheme.colorScheme.textColor,
                        text = "انتخاب سال"
                    )
                }
                val scrollState =
                    rememberLazyListState(initialFirstVisibleItemIndex = jalali.year - 2)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(yearSelectorHeight)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = scrollState
                ) {
                    items(3000) { index ->
                        Divider()
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                jalali = JalaliCalendar(index, jalali.month, 1)
                                pickerType = PickerType.Day
                            }
                            .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = FormatHelper.toPersianNumber(index.toString()),
                                style = yearMonthTextStyle,
                                fontSize = 30.sp,
                                color = yearTextColorFun(
                                    jalali.year,
                                    index,
                                    textColorHighlight,
                                    yearMonthTextStyle.color
                                )
                            )
                        }
                    }
                }
            }
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