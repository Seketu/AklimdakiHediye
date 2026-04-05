package com.reylortechnology.aklimdakihediye.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

val poppinsFontFamily = FontFamily(
    // Thin (100)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Thin),
    // Extra Light (200)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.ExtraLight),
    // Light (300)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Light),
    // Regular (400)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Normal),
    // Medium (500)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Medium),
    // SemiBold (600)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.SemiBold),
    // Bold (700)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Bold),
    // ExtraBold (800)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.ExtraBold),
    // Black (900)
    Font(googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = FontWeight.Black)
)

val OleoScript = FontFamily(
    Font(R.font.oleo_script_regular, FontWeight.Normal)
)
val OrelegaOneRegular = FontFamily(
    Font(R.font.justanotherhand_regular, FontWeight.Normal)
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

val Typography.poppinsTitle: TextStyle
    get() = this.titleLarge.copy(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.SemiBold
    )

val Typography.poppinsBody: TextStyle
    get() = this.bodyLarge.copy(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal
    )

val Typography.poppinsHeadline: TextStyle
    get() = this.headlineMedium.copy(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Bold
    )

val AppTypography = Typography(
    // Display styles - Alegreya Sans SC korunuyor
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),

    // Headlines - Poppins kullanılıyor
    headlineLarge = baseline.headlineLarge.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Medium),

    // Titles - Poppins kullanılıyor
    titleLarge = baseline.titleLarge.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.SemiBold),
    titleMedium = baseline.titleMedium.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Medium),
    titleSmall = baseline.titleSmall.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Medium),

    // Body - Roboto korunuyor ama Poppins seçeneği de var
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),

    // Labels - Poppins kullanılıyor
    labelLarge = baseline.labelLarge.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Medium),
    labelMedium = baseline.labelMedium.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal),
    labelSmall = baseline.labelSmall.copy(fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal),
)

