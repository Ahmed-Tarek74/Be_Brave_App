package com.compose.chatapp.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.compose.chatapp.R
import com.compose.chatapp.presentation.intents.SearchUsersIntent

@Composable
fun SearchBar(searchQuery: String,onBackButtonClicked:(SearchUsersIntent)->Unit ,onSearchQueryChanged: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(colorResource(id = R.color.primary_dark_blue), shape = CircleShape)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = colorResource(id = R.color.gray),
            contentDescription = stringResource(id = R.string.backToHome)
            , modifier = Modifier.clickable {onBackButtonClicked(SearchUsersIntent.BackToHome)}
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            singleLine= true,
            cursorBrush = SolidColor(Color.White),
            textStyle = TextStyle(color = Color.White),
            decorationBox = {
                innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(text = stringResource(id = R.string.search),color = colorResource(id = R.color.gray))
                }
                innerTextField()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
