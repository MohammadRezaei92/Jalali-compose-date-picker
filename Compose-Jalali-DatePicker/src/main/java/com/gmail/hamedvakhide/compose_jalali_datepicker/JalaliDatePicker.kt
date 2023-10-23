package com.gmail.hamedvakhide.compose_jalali_datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.backgroundColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.selectedIconColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.textColor
import com.gmail.hamedvakhide.compose_jalali_datepicker.ui.theme.textColorHighlight
import ir.huri.jcal.JalaliCalendar

/**
 * Opens a Jalali DatePicker dialog with the given content.
 *
 * Example usage:
 *
 * @param openDialog Dialog will be visible as long as openDialog value is true.
 * @param initialDate Specify a date to be shown when dialog opens.
 * @param onSelectDay Called when a day is selected.
 * @param onConfirm Called when confirm button is clicked.
 * @param backgroundColor Background color of the dialog.
 * @param selectedIconColor Color of selected day (month, year) circular icon.
 * @param textColorHighlight Color of current day (month, year) text.
 * @param nextPreviousBtnColor Color of next and previous month button.
 *
 */

@Composable
fun JalaliDatePickerDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    initialDate: Pair<JalaliCalendar?, JalaliCalendar?> = Pair(null, null),
    onSelectDay: (startRange: JalaliCalendar, endRange: JalaliCalendar?) -> String?,
    onConfirm: (startRange: JalaliCalendar, endRange: JalaliCalendar?) -> Unit,
    rangePickerMode: Boolean = false,
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
    eventTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    eventIconRes: Int? = null
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
//            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        // same action as in onDismissRequest
                        openDialog.value = false
                    }
            ) {
                // content
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation,

                    ) {
                    JalaliRangePickerView(
                        modifier = modifier,
                        initialDate = initialDate,
                        onSelectDay = onSelectDay,
                        onConfirm = { start, end ->
                            onConfirm(start, end)
                            openDialog.value = false
                        },
                        onDismiss = {
                            openDialog.value = false
                        },
                        rangePickerMode = rangePickerMode,
                        backgroundColor = backgroundColor,
                        selectedIconColor = selectedIconColor,
                        textColorHighlight = textColorHighlight,
                        nextPreviousBtnColor = nextPreviousBtnColor,
                        todayCircleColor = todayCircleColor,
                        headerDividerColor = headerDividerColor,
                        headerTextStyle = headerTextStyle,
                        yearMonthTextStyle = yearMonthTextStyle,
                        dayNumberTextStyle = dayNumberTextStyle,
                        buttonsTextStyle = buttonsTextStyle,
                        weekDaysTextStyle = weekDaysTextStyle,
                        eventTextStyle = eventTextStyle,
                        eventIconRes = eventIconRes
                    )
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JalaliDatePickerBottomSheet(
    modifier: Modifier = Modifier,
    openBottomSheet: MutableState<Boolean>,
    initialDate: Pair<JalaliCalendar?, JalaliCalendar?> = Pair(null, null),
    onSelectDay: (startRange: JalaliCalendar, endRange: JalaliCalendar?) -> String?,
    onConfirm: (startRange: JalaliCalendar, endRange: JalaliCalendar?) -> Unit,
    onDismiss: () -> Unit,
    rangePickerMode: Boolean = false,
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
    eventTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    eventIconRes: Int? = null
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (openBottomSheet.value)
        ModalBottomSheet(
            onDismissRequest = {
                openBottomSheet.value = false
                onDismiss()
            },
            sheetState = bottomSheetState,
            dragHandle = null
        ) {
            JalaliRangePickerView(
                modifier = modifier,
                initialDate = initialDate,
                onSelectDay = onSelectDay,
                onConfirm = { start, end ->
                    onConfirm(start, end)
                    openBottomSheet.value = false
                },
                onDismiss = {
                    openBottomSheet.value = false
                    onDismiss()
                },
                rangePickerMode = rangePickerMode,
                backgroundColor = backgroundColor,
                selectedIconColor = selectedIconColor,
                textColorHighlight = textColorHighlight,
                nextPreviousBtnColor = nextPreviousBtnColor,
                todayCircleColor = todayCircleColor,
                headerDividerColor = headerDividerColor,
                headerTextStyle = headerTextStyle,
                yearMonthTextStyle = yearMonthTextStyle,
                dayNumberTextStyle = dayNumberTextStyle,
                buttonsTextStyle = buttonsTextStyle,
                weekDaysTextStyle = weekDaysTextStyle,
                eventTextStyle = eventTextStyle,
                eventIconRes = eventIconRes
            )
        }
}