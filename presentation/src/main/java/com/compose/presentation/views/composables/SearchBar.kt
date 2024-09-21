package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.SearchUsersIntent
import com.compose.presentation.ui.theme.light_gray
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.red
import com.compose.presentation.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onBackButtonClicked: (SearchUsersIntent) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(colorScheme.primary_dark_blue, shape = CircleShape)
            .padding(horizontal = 20.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = colorScheme.light_gray,
            contentDescription = stringResource(id = R.string.backToHome),
            modifier = Modifier.clickable { onBackButtonClicked(SearchUsersIntent.BackToHome) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.white
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    style = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.light_gray)
                )
            },
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = colorScheme.white,
                containerColor = Color.Transparent,
                disabledTextColor = Color.Transparent,
                errorCursorColor = colorScheme.red,
                focusedIndicatorColor = Color.Transparent, // Removes underline when focused
                unfocusedIndicatorColor = Color.Transparent, // Removes underline when not focused
                disabledIndicatorColor = Color.Transparent, // Removes underline when disabled
                errorIndicatorColor = Color.Transparent // Removes underline when error
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1
        )
    }
}
