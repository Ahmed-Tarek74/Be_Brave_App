package com.compose.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel :ViewModel() {
    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }
    // Abstract method to handle exceptions
    private  fun handleException(exception: Throwable) {
        // Log the exception for debugging purposes
        Log.e("BaseViewModel", "An error occurred", exception)
    }
}