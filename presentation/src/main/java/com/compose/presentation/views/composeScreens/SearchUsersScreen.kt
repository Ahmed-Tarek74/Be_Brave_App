package com.compose.presentation.views.composeScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.SearchUsersIntent
import com.compose.presentation.viewStates.SearchUsersViewState
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.SearchBar
import com.compose.presentation.views.composables.UserItemCard

@Composable
fun SearchUsersScreen(
    viewState: State<SearchUsersViewState>,
    setIntent: (SearchUsersIntent) -> Unit
) {
    Column(
        Modifier.background(colorResource(id = R.color.white))
    ) {

        SearchBar(searchQuery = viewState.value.searchQuery, setIntent) { newQuery ->
            setIntent(SearchUsersIntent.UpdateSearchQuery(newQuery))
        }

        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        when {
            viewState.value.isLoading -> {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.primary_dark_blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewState.value.emptyListErrorMsg != null -> {
                Text(
                    text = (stringResource(
                        R.string.no_result_found_for,
                        stringResource(id = R.string.noResultFound),
                        viewState.value.searchQuery
                    )),
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            }

            viewState.value.errorMsg != null -> {
                ErrorMsgCard(errorMsg = viewState.value.errorMsg!!)
            }

            else -> {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    LazyColumn {
                        items(viewState.value.usersList) { user ->
                            UserItemCard(user, onUserSelected = {
                                setIntent(SearchUsersIntent.SelectUser(it))
                            })
                        }
                    }
                }
            }
        }
    }

}