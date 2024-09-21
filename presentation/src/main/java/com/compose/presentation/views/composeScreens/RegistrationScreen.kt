package com.compose.presentation.views.composeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.intents.RegistrationIntent
import com.compose.presentation.intents.RegistrationIntent.*
import com.compose.presentation.ui.theme.ChatAppTheme
import com.compose.presentation.ui.theme.gray
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white
import com.compose.presentation.viewStates.RegistrationViewState
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.LoadingDialog

@Composable
fun RegistrationScreen(
    viewState: State<RegistrationViewState>,
    setIntent: (RegistrationIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.white)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { setIntent(NavigateToLogin) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.backToLogin)
                )
            }
            Text(
                text = stringResource(id = R.string.registration),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            OutlinedTextField(
                value = viewState.value.email,
                onValueChange = { setIntent(EmailChanged(it)) },
                maxLines = 1,
                label = {
                    Text(
                        stringResource(id = R.string.email),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = viewState.value.username,
                onValueChange = { setIntent(UsernameChanged(it)) },
                maxLines = 1,
                label = {
                    Text(
                        stringResource(R.string.username),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = viewState.value.password,
                onValueChange = { setIntent(PasswordChanged(it)) },
                maxLines = 1,
                label = {
                    Text(
                        stringResource(id = R.string.password),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                visualTransformation = viewState.value.passwordVisualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { setIntent(PasswordVisibilityChanged(viewState.value.isPasswordVisible)) }) {
                        val image = painterResource(viewState.value.passwordTrailingIcon)
                        val description =
                            stringResource(id = viewState.value.passwordIconDescription)
                        Icon(painter = image, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = viewState.value.confirmationPassword,
                onValueChange = { setIntent(ConfirmationPasswordChanged(it)) },
                maxLines = 1,
                label = {
                    Text(
                        stringResource(R.string.confirm_password),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                visualTransformation = viewState.value.confirmationPasswordVisualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        setIntent(
                            ConfirmationPasswordVisibilityChanged(
                                viewState.value.isConfirmationPasswordVisible
                            )
                        )
                    }) {
                        val image =
                            painterResource(id = viewState.value.confirmationPasswordTrailingIcon)
                        val description =
                            stringResource(id = viewState.value.confirmationPasswordIconDescription)
                        Icon(painter = image, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(24.dp))

            viewState.value.errorMessage?.let { errorMsg ->
                ErrorMsgCard(errorMsg = errorMsg)
            }
            if (viewState.value.isLoading) {
                LoadingDialog(loadingMsg = stringResource(id = R.string.signingIn))
            }
            Button(
                onClick = { setIntent(Register) },
                enabled = viewState.value.isRegisterEnabled,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    colorScheme.primary_dark_blue, colorScheme.white,
                    colorScheme.gray, colorScheme.white
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.headlineMedium.copy(color = colorScheme.white),
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegistrationScreenPreview() {
    ChatAppTheme {
        RegistrationScreen(viewState = remember {
            mutableStateOf(RegistrationViewState())
        }) {}
    }
}


