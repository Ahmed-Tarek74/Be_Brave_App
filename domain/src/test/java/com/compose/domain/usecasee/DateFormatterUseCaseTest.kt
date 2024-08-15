package com.compose.domain.usecasee

import com.compose.domain.usecases.DateFormatterUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateFormatterUseCaseTest {

    private val dateFormatterUseCase = DateFormatterUseCase()
    @Test
    fun `invoke should format timestamp correctly`() {
        val timestamp = 1625086800000L // Example timestamp
        val expectedFormattedDate = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
        val result = dateFormatterUseCase(timestamp)
        assertEquals(expectedFormattedDate, result)
    }
}