package com.compose.presentation.views.composeScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.HomeIntent
import com.compose.presentation.viewStates.HomeViewState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.compose.presentation.intents.HomeIntent.*
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.RecentChatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    setIntent: (HomeIntent) -> Unit,
    dateFormatter:(Long)->String,
    viewState: State<HomeViewState>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = colorResource(id = R.color.primary_dark_blue),
                    scrolledContainerColor = colorResource(id = R.color.white),
                    navigationIconContentColor = colorResource(id = R.color.primary_dark_blue),
                    titleContentColor = colorResource(id = R.color.white),
                    actionIconContentColor = colorResource(id = R.color.white)
                ),
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                    )
                },
                actions = {
                    IconButton(onClick = {
                        setIntent(LoggedOut)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ExitToApp,
                            contentDescription = stringResource(
                                id = R.string.logout
                            )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = colorResource(id = R.color.primary_dark_blue),
                contentColor = colorResource(
                    id = R.color.white
                ),
                onClick = { setIntent(StartNewChat) })
            {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_chat),
                    contentDescription = stringResource(id = R.string.newChat)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
        ) {
            when (viewState.value) {
                is HomeViewState.Loading -> {
                    Spacer(modifier = Modifier.weight(1f))
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.primary_dark_blue),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                is HomeViewState.Failure -> {
                    Spacer(modifier = Modifier.weight(1f))
                    ErrorMsgCard(errorMsg = (viewState.value as HomeViewState.Failure).error)
                    Spacer(modifier = Modifier.weight(1f))
                }

                is HomeViewState.Success -> {
                    val recentChats = (viewState.value as HomeViewState.Success).recentChats
                    if (recentChats.isEmpty()) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(id = R.string.noRecentChats),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                            letterSpacing = 2.sp,
                            color = colorResource(id = R.color.primary_dark_blue)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = colorResource(id = R.color.gray)
                            ),
                            shape = RoundedCornerShape(25.dp), modifier = Modifier

                        ) {
                            LazyColumn {
                                items(recentChats) { recentChat ->
                                    RecentChatCard(
                                        recentChat,
                                        dateFormatter={dateFormatter(recentChat.timestamp)},
                                        onChatSelected = { user ->
                                            setIntent(SelectRecentChat(user))
                                        })
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}