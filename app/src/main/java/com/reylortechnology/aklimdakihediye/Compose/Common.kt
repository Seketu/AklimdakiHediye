package com.reylortechnology.aklimdakihediye.Compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.util.lerp
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerScreen(
    modifier: Modifier = Modifier,
    initialDay: Int = 1,     // Başlangıç Günü
    initialMonth: Int = 1,   // Başlangıç Ayı
    initialYear: Int = LocalDate.now().year,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    highlightColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    selectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
    onDateChange: (Int, Int, Int) -> Unit,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier) {
        ResponsiveDatePicker(
            modifier = modifier
                .background(containerColor, RoundedCornerShape(16)),
            onDateSelected = { d, m, y ->
                onDateChange(d, m, y)
            },
            initialDay = initialDay,
            initialMonth = initialMonth,
            initialYear = initialYear,
            highlightColor = highlightColor,
            selectedTextColor = selectedTextColor,
            unselectedTextColor = unselectedTextColor
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResponsiveDatePicker(
    modifier: Modifier = Modifier,
    startYear: Int = 1950,
    endYear: Int = 2026,
    initialDay: Int = 1,
    initialMonth: Int = 1,
    initialYear: Int = endYear,
    highlightColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    selectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
    onDateSelected: (Int, Int, Int) -> Unit
) {
    val years = (startYear..endYear).toList()
    val months = (1..12).toList()

    var selectedYear by remember { mutableIntStateOf(initialYear.coerceIn(startYear, endYear)) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth.coerceIn(1, 12)) }

    val daysInMonth by remember(selectedMonth, selectedYear) {
        derivedStateOf { YearMonth.of(selectedYear, selectedMonth).lengthOfMonth() }
    }

    var selectedDay by remember { mutableIntStateOf(initialDay.coerceIn(1, daysInMonth)) }
    LaunchedEffect(daysInMonth) {
        if (selectedDay > daysInMonth) selectedDay = daysInMonth
    }

    LaunchedEffect(selectedDay, selectedMonth, selectedYear) {
        onDateSelected(selectedDay, selectedMonth, selectedYear)
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val visibleItemsCount = 3
        val itemHeight = maxHeight / visibleItemsCount
        val fontSize = with(LocalDensity.current) { (itemHeight * 0.45f).toSp() }

        // Ortadaki Seçim Çizgisi (Highlight)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(highlightColor, RoundedCornerShape(8))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // GÜN
            WheelPicker(
                modifier = Modifier.weight(1f),
                items = (1..daysInMonth).toList(),
                initialItem = selectedDay,
                itemHeight = itemHeight,
                fontSize = fontSize,
                visibleItemsCount = visibleItemsCount,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                onItemSelected = { selectedDay = it }
            )

            // AY
            WheelPicker(
                modifier = Modifier.weight(1f),
                items = months,
                initialItem = selectedMonth,
                itemHeight = itemHeight,
                fontSize = fontSize,
                visibleItemsCount = visibleItemsCount,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                onItemSelected = { selectedMonth = it }
            )

            // YIL
            WheelPicker(
                modifier = Modifier.weight(1f),
                items = years,
                initialItem = selectedYear,
                itemHeight = itemHeight,
                fontSize = fontSize,
                visibleItemsCount = visibleItemsCount,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                onItemSelected = { selectedYear = it }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialItem: T,
    itemHeight: Dp,
    fontSize: TextUnit,
    visibleItemsCount: Int,
    selectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
    onItemSelected: (T) -> Unit
) {
    val startIndex = items.indexOf(initialItem).coerceAtLeast(0)

    val pagerState = rememberPagerState(initialPage = startIndex) {
        items.size
    }

    LaunchedEffect(pagerState.currentPage) {
        onItemSelected(items[pagerState.currentPage])
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2)),
        pageSize = PageSize.Fixed(itemHeight),
    ) { page ->

        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        val scale = lerp(
            start = 0.8f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        val alpha = lerp(
            start = 0.3f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        Box(
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = items[page].toString(),
                fontSize = fontSize,
                fontWeight = if (page == pagerState.currentPage) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (page == pagerState.currentPage) selectedTextColor else unselectedTextColor
            )
        }
    }
}
