package com.compose.presentation.views.composeScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.LoginIntent
import com.compose.presentation.intents.LoginIntent.*
import com.compose.presentation.ui.theme.ChatAppTheme
import com.compose.presentation.ui.theme.dark_gray
import com.compose.presentation.ui.theme.gray
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white
import com.compose.presentation.viewStates.LoginViewState
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.LoadingDialog


@Composable
fun LoginScreen(
    viewState: State<LoginViewState>,
    setIntent: (LoginIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = viewState.value.email,
            onValueChange = { setIntent(EmailChanged(it)) },
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = viewState.value.password,
            onValueChange = { setIntent(PasswordChanged(it)) },
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            visualTransformation = viewState.value.passwordVisualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { setIntent(PasswordVisibilityChanged(viewState.value.isPasswordVisible)) })
                {
                    val image = painterResource(id = viewState.value.passwordTrailingIcon)
                    val description =
                        stringResource(id = viewState.value.passwordIconDescription)
                    Icon(painter = image, contentDescription = description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 30.dp)
        )
        viewState.value.errorMessage?.let { errorMsg ->
            ErrorMsgCard(errorMsg = errorMsg)
        }
        if (viewState.value.isLoading) {
            LoadingDialog(loadingMsg = stringResource(id = R.string.signingIn)) {}
        }
        Button(
            onClick = {
                setIntent(Login)
            },
            enabled = viewState.value.isLoginEnabled,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                colorScheme.primary_dark_blue, colorScheme.white,
                colorScheme.gray, colorScheme.white
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 5.dp)
        ) {
            Text(
                stringResource(R.string.log_in),
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.headlineMedium.copy(color = colorScheme.white)
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Row(modifier = Modifier.align(Alignment.End)) {
            Text(
                text = stringResource(id = R.string.newAccountMsg),
                style = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.dark_gray)
            )
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            Text(
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.headlineMedium.copy(
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable { setIntent(NavigateToRegister) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    ChatAppTheme {
        LoginScreen(viewState = remember {
            mutableStateOf(LoginViewState())
        }) {}
    }
}
