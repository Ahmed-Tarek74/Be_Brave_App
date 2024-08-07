package com.compose.presentation.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape

data class MessageUiModel (
    val message:String,
    val date: String,
    val backgroundColor: Int,
    val arrangement: Arrangement.Horizontal,
    val shape: RoundedCornerShape
)