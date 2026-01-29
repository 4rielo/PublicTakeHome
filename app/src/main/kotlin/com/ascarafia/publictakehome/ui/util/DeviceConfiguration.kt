package com.ascarafia.publictakehome.ui.util

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;
    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {

            println("******** WindowSizeClass: $windowSizeClass")

            val atLeastMediumWidth = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
            val atLeastLargeWidth = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

            val atLeastMediumHeight = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
            val atLeastLargeHeight = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)

            val isSmallWidth = !atLeastMediumWidth
            val isMediumWidth = atLeastMediumWidth && !atLeastLargeWidth

            val isSmallHeight = !atLeastMediumHeight
            val isMediumHeight = atLeastMediumHeight && !atLeastLargeHeight

            return when {
                isSmallWidth && atLeastMediumHeight -> MOBILE_PORTRAIT
                atLeastMediumWidth && isSmallHeight -> MOBILE_LANDSCAPE
                isMediumWidth && atLeastMediumHeight -> TABLET_PORTRAIT
                atLeastLargeWidth && isMediumHeight -> TABLET_LANDSCAPE
                else -> DESKTOP
            }
        }
    }
}