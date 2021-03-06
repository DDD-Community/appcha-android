package com.editor.appcha.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    colors: AppColors = AppTheme.colors,
    typography: AppTypography = AppTheme.typography,
    content: @Composable() () -> Unit
) {
    val rememberedColors = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    CompositionLocalProvider(
        LocalAppColors provides rememberedColors,
        LocalTypography provides typography
    ) {
        MaterialTheme(
            shapes = AppShapes,
            content = content,
        )
    }
}

@Composable
fun SplashTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.apply {
        isStatusBarVisible = false
        setStatusBarColor(
            color = SplashBarColor,
            darkIcons = true)
    }

    MaterialTheme(
        content = content
    )
}

object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
