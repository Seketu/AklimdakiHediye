package com.reylortechnology.aklimdakihediye.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.reylortechnology.aklimdakihediye.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)
val kanitFontFamily = FontFamily(
    // İnce (Light - 300)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.Light),
    // Normal (Regular - 400)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.Normal),
    // Orta (Medium - 500)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.Medium),
    // Yarı Kalın (SemiBold - 600)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.SemiBold),
    // Kalın (Bold - 700)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.Bold),
    // Ekstra Kalın (ExtraBold - 800)
    Font(googleFont = GoogleFont("Kanit"), fontProvider = provider, weight = FontWeight.ExtraBold)
)

val JustAnotherHandFont = FontFamily(
    Font(R.font.justanotherhand_regular, FontWeight.Normal)
)
val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Alegreya Sans SC"),
        fontProvider = provider,
    )
)

val baseline = Typography()

val Typography.kanitOzelBaslik: TextStyle
    get() = this.headlineLarge.copy(
        fontFamily = kanitFontFamily,
        fontWeight = FontWeight.Bold
    )

val AppTypography = Typography(

    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

