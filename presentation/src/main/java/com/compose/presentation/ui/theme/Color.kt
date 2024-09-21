package com.compose.presentation.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val lightGray = Color(0xFFE9EAEC)
val Gray = Color(0xFFAEB6BF)
val darkGray = Color(0xff65737e)
val Pink = Color(0xffefd0d4)
val lightRed = Color(0xfff26b73)
val Red = Color(0xffe02020)
val darkRed = Color(0xff8c253f)
val lightBlue = Color(0xFF9FC3E9)
val primaryDarkBlue = Color(0xFF253649)
val Blue = Color(0xFF0000FF)
val Green = Color(0xFF008000)
val lightGreen = Color(0xFFAFE1AF)
val Yellow = Color(0xFFFFFF00)
val lightYellow = Color(0xFFFFFFC5)

// colors to be used in compose like this MaterialTheme.colors.-----
//Grayscale Colors
val ColorScheme.black: Color
    get() = Black
val ColorScheme.white: Color
    get() = White
val ColorScheme.gray: Color
    get() = Gray
val ColorScheme.light_gray: Color
    get() = lightGray
val ColorScheme.dark_gray: Color
    get() = darkGray
// Blue Shades
val ColorScheme.light_blue: Color
    get() = lightBlue
val ColorScheme.blue: Color
    get() = Blue
val ColorScheme.primary_dark_blue: Color
    get() = primaryDarkBlue
// Red Shades
val ColorScheme.red: Color
    get() = Red
val ColorScheme.light_red: Color
    get() = lightRed
val ColorScheme.dark_red: Color
    get() = darkRed
val ColorScheme.pink: Color
    get() = Pink

// Yellow Shades
val ColorScheme.yellow: Color
    get() = Yellow
val ColorScheme.light_yellow: Color
    get() = lightYellow
// green Shades
val ColorScheme.green: Color
    get() = Green
val ColorScheme.light_green: Color
    get() = lightGreen