package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun AutoResizeText(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 24.sp,
    minFontSize: TextUnit = 10.sp,
    stepRatio: Float = 0.9f,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = TextStyle.Default
) {
    var fontSize by remember(text) { mutableStateOf(maxFontSize) }
    var ready by remember { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier.drawWithContent { if (ready) drawContent() },
        color = color,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        textAlign = textAlign,
        maxLines = 1,
        softWrap = false,
        style = style,
        onTextLayout = { result: TextLayoutResult ->
            val overflow = result.didOverflowWidth || result.lineCount > 1
            if (overflow && fontSize > minFontSize) {
                fontSize = (fontSize.value * stepRatio).sp
            } else {
                ready = true
            }
        }
    )
}
