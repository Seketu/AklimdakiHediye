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
            onDateChange: (Int, Int, Int) -> Unit,
  // Başlangıç Yılı (Varsayılan olarak mevcut yıl
) {
    // KULLANIM:
    // Dışarıdan gelen modifier ne boyutta olursa olsun, içerik ona uyar.
    Box(contentAlignment = Alignment.Center, modifier = Modifier) {
        ResponsiveDatePicker(
            modifier = modifier
                .background(Color(0xFFF5F5F5), RoundedCornerShape(16)), // Arkaplan ve köşe yumuşatma
            onDateSelected = { d, m, y ->
                onDateChange(d, m, y)
            },
            initialDay = initialDay,
            initialMonth = initialMonth,
            initialYear = initialYear,
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
    onDateSelected: (Int, Int, Int) -> Unit
) {
    val years = (startYear..endYear).toList()
    val months = (1..12).toList()

    var selectedYear by remember { mutableIntStateOf(initialYear.coerceIn(startYear, endYear)) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth.coerceIn(1, 12)) }

    val daysInMonth by remember(selectedMonth, selectedYear) {
        derivedStateOf { YearMonth.of(selectedYear, selectedMonth).lengthOfMonth() }
    }

    var selectedDay by remember { mutableIntStateOf(1) }
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
        // Ekranda aynı anda kaç satır görünsün? (Genelde 3 veya 5 seçilir)
        val visibleItemsCount = 3

        // Matematik: Toplam Yükseklik / Görünmesi İstenen Sayı = Bir Satırın Yüksekliği
        val itemHeight = maxHeight / visibleItemsCount

        // Font boyutu da satır yüksekliğine göre oranlanır (Yüksekliğin %40'ı kadar font olsun)
        val fontSize = with(LocalDensity.current) { (itemHeight * 0.45f).toSp() }

        // Ortadaki Seçim Çizgisi (Highlight)
        // Yüksekliği tam olarak hesaplanan itemHeight kadardır.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(Color(0x1A000000), RoundedCornerShape(8))
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
    itemHeight: Dp,          // Hesaplanan yükseklik dışarıdan gelir
    fontSize: TextUnit,      // Hesaplanan font boyutu dışarıdan gelir
    visibleItemsCount: Int,
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
        modifier = modifier.fillMaxHeight(), // Yüksekliği tamamen doldurur
        // Padding hesabı: (Görünen Sayı / 2) * Satır Yüksekliği -> Tam ortaya denk getirir
        contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2)),
        pageSize = PageSize.Fixed(itemHeight),
    ) { page ->

        // Görsel efektler (Büyüme/Küçülme)
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
                .height(itemHeight) // Hesaplanan yükseklik
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
                fontSize = fontSize, // Hesaplanan font
                fontWeight = if (page == pagerState.currentPage) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (page == pagerState.currentPage) Color.Black else Color.Gray
            )
        }
    }
}