package com.example.oilchangenotice.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oilchangenotice.OilFilterUtils
import com.example.oilchangenotice.ui.theme.OilChangeNoticeTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OilFilterScreen(
    context: Context = LocalContext.current,
) {
    // Variable for saved oil filter base date (loaded from preferences or default 18/11/2026)
    var baseDate by remember { mutableStateOf(OilFilterUtils.loadBaseDate(context)) }

    // Today's date when opening the app
    val today = remember { LocalDate.now() }

    // Date currently selected for testing / checking (defaults to today)
    var selectedTestDate by remember { mutableStateOf(today) }

    // Calculate if today is oil filter day
    val isTodayOilFilterDay = remember(baseDate, today) {
        OilFilterUtils.isOilFilterDay(today, baseDate)
    }

    // Days elapsed between base date and today
    val daysFromBaseToToday = remember(baseDate, today) {
        OilFilterUtils.getDaysBetween(today, baseDate)
    }

    // State for showing DatePicker dialog to edit base date
    var showDatePickerDialog by remember { mutableStateOf(value = false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Oil Filter Notice",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Every 2 Days Schedule",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Today's Date Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "TODAY'S DATE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = OilFilterUtils.formatDateDisplay(today),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = OilFilterUtils.formatDateShort(today),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // PRIMARY HERO STATUS CARD: Is today Oil Filter Day or not?
            StatusHeroCard(
                isOilFilterDay = isTodayOilFilterDay,
                today = today,
                baseDate = baseDate
            )

            // ALGORITHM & SAVED VARIABLE DETAILS CARD
            AlgorithmDetailsCard(
                baseDate = baseDate,
                daysBetween = daysFromBaseToToday,
                isTodayOilFilterDay = isTodayOilFilterDay,
                onResetToToday = {
                    baseDate = today
                    OilFilterUtils.saveBaseDate(context, today)
                },
                onResetToDefault = {
                    val defaultDate = OilFilterUtils.DEFAULT_BASE_DATE
                    baseDate = defaultDate
                    OilFilterUtils.saveBaseDate(context, defaultDate)
                },
                onOpenDatePicker = {
                    showDatePickerDialog = true
                }
            )

            // INTERACTIVE DATE TESTER CARD
            DateTesterCard(
                selectedDate = selectedTestDate,
                baseDate = baseDate,
                onDateChanged = { newDate ->
                    selectedTestDate = newDate
                },
                onResetToToday = {
                    selectedTestDate = today
                }
            )
        }

        // Date Picker Dialog to set custom Base Oil Filter Date
        if (showDatePickerDialog) {
            val initialMillis = baseDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialMillis
            )

            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedLocalDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                baseDate = selectedLocalDate
                                OilFilterUtils.saveBaseDate(context, selectedLocalDate)
                            }
                            showDatePickerDialog = false
                        }
                    ) {
                        Text("Save Base Date")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

/**
 * Hero Card displaying today's oil filter status clearly upon app open.
 */
@Composable
fun StatusHeroCard(
    isOilFilterDay: Boolean,
    today: LocalDate,
    baseDate: LocalDate
) {
    val containerColor = if (isOilFilterDay) {
        Color(0xFFE8F5E9) // Light Green
    } else {
        Color(0xFFFFF3E0) // Light Warm Orange/Yellow
    }

    val contentColor = if (isOilFilterDay) {
        Color(0xFF1B5E20) // Dark Green
    } else {
        Color(0xFFE65100) // Dark Orange
    }

    val iconVector = if (isOilFilterDay) Icons.Default.CheckCircle else Icons.Default.Info

    val nextOilFilterDate = remember(today, baseDate) {
        OilFilterUtils.getNextOilFilterDate(today, baseDate)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isOilFilterDay) "YES! TODAY IS OIL FILTER DAY" else "NO, TODAY IS NOT OIL FILTER DAY",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isOilFilterDay) {
                    "Your oil filter schedule is active today (every 2 days interval). Please check or change your oil filter."
                } else {
                    "Next Oil Filter Day will be on ${OilFilterUtils.formatDateShort(nextOilFilterDate)} (${OilFilterUtils.formatDateDisplay(nextOilFilterDate)})."
                },
                fontSize = 14.sp,
                color = contentColor.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = contentColor,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = if (isOilFilterDay) "STATUS: OIL FILTER DUE TODAY 🛢️" else "STATUS: NEXT IN 1 DAY ⏳",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Card explaining the stored base date variable, algorithm, and controls to update the saved date.
 */
@Composable
fun AlgorithmDetailsCard(
    baseDate: LocalDate,
    daysBetween: Long,
    isTodayOilFilterDay: Boolean,
    onResetToToday: () -> Unit,
    onResetToDefault: () -> Unit,
    onOpenDatePicker: () -> Unit
) {
    val mathFormulaText = "(Date - ${OilFilterUtils.formatDateShort(baseDate)}) % 2 == 0"
    val calculationStepText = "($daysBetween days) % 2 = ${kotlin.math.abs(daysBetween) % 2L}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Saved Base Oil Filter Date Variable",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onOpenDatePicker) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Base Date",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Base Reference Date Variable",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${OilFilterUtils.formatDateShort(baseDate)} (${OilFilterUtils.formatDateDisplay(baseDate)})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Text(
                text = "Algorithm Math",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Formula: $mathFormulaText",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Today Calculation: $calculationStepText -> ${if (isTodayOilFilterDay) "0 (Match!)" else "1 (Not Match)"}",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onResetToToday,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Set to Today", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onResetToDefault,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Reset (18/11/2026)", fontSize = 11.sp)
                }
            }
        }
    }
}

/**
 * Card allowing user to test any specific date.
 */
@Composable
fun DateTesterCard(
    selectedDate: LocalDate,
    baseDate: LocalDate,
    onDateChanged: (LocalDate) -> Unit,
    onResetToToday: () -> Unit
) {
    val isTestDateOilFilterDay = remember(selectedDate, baseDate) {
        OilFilterUtils.isOilFilterDay(selectedDate, baseDate)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Test Any Date",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                if (selectedDate != LocalDate.now()) {
                    TextButton(onClick = onResetToToday) {
                        Text("Today", fontSize = 12.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onDateChanged(selectedDate.minusDays(1)) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("← -1 Day")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = OilFilterUtils.formatDateShort(selectedDate),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = OilFilterUtils.formatDateDisplay(selectedDate),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                OutlinedButton(
                    onClick = { onDateChanged(selectedDate.plusDays(1)) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+1 Day →")
                }
            }

            Surface(
                color = if (isTestDateOilFilterDay) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isTestDateOilFilterDay) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isTestDateOilFilterDay) Color(0xFF1B5E20) else Color(0xFFE65100),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTestDateOilFilterDay) {
                            "${OilFilterUtils.formatDateShort(selectedDate)} IS an Oil Filter Day!"
                        } else {
                            "${OilFilterUtils.formatDateShort(selectedDate)} is NOT an Oil Filter Day."
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTestDateOilFilterDay) Color(0xFF1B5E20) else Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OilFilterScreenPreview() {
    OilChangeNoticeTheme {
        StatusHeroCard(
            isOilFilterDay = true,
            today = LocalDate.of(2026, 11, 18),
            baseDate = LocalDate.of(2026, 11, 18)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OilFilterScreenNotDayPreview() {
    OilChangeNoticeTheme {
        StatusHeroCard(
            isOilFilterDay = false,
            today = LocalDate.of(2026, 11, 19),
            baseDate = LocalDate.of(2026, 11, 18)
        )
    }
}
