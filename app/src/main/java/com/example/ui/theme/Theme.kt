package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = BrandPrimaryDark,
  onPrimary = Color(0xFF5E0033),
  primaryContainer = BrandPrimary,
  onPrimaryContainer = Color(0xFFFFD9E4),
  secondary = BrandSecondaryDark,
  onSecondary = Color(0xFF4A2800),
  secondaryContainer = Color(0xFF693C00),
  onSecondaryContainer = BrandSecondaryContainer,
  tertiary = BrandTertiaryDark,
  background = DarkBackground,
  surface = DarkSurface,
  surfaceVariant = DarkSurfaceCard,
  outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
  primary = BrandPrimary,
  onPrimary = Color.White,
  primaryContainer = BrandPrimaryContainer,
  onPrimaryContainer = BrandOnPrimaryContainer,
  secondary = BrandSecondary,
  onSecondary = Color.White,
  secondaryContainer = BrandSecondaryContainer,
  onSecondaryContainer = BrandOnSecondaryContainer,
  tertiary = BrandTertiary,
  background = BackgroundLight,
  surface = SurfaceLight,
  surfaceVariant = SurfaceCard,
  outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded luxury colors consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
