package com.compose.domain.usecases

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateFormatterUseCase {
    operator fun invoke(timestamp: Long):String{
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
    }
}