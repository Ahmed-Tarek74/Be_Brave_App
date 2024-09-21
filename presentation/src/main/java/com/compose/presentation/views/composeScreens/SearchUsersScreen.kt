package com.compose.presentation.views.composeScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.SearchUsersIntent
import com.compose.presentation.ui.theme.light_blue
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white
import com.compose.presentation.viewStates.SearchUsersViewState
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.SearchBar
import com.compose.presentation.views.composables.UserItemCard

@Composable
fun SearchUsersScreen(
    viewState: State<SearchUsersViewState>,
    setIntent: (SearchUsersIntent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.background(colorScheme.white)
    ) {
        SearchBar(searchQuery = viewState.value.searchQuery, setIntent) { newQuery ->
            setIntent(SearchUsersIntent.UpdateSearchQuery(newQuery))
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        when {
            viewState.value.isLoading -> {
                CircularProgressIndicator(
                    color = colorScheme.primary_dark_blue,
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
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            viewState.value.errorMsg != null -> {
                ErrorMsgCard(errorMsg = viewState.value.errorMsg!!)
            }

            else -> {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = BorderStroke(width = 2.dp, color = colorScheme.light_blue),
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