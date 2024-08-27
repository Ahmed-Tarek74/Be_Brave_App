package com.compose.domain.base

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

open class BaseUseCaseTest {
    protected var testDispatcher :TestDispatcher = StandardTestDispatcher()
}