package com.compose.chatapp.presentation.views.composeScreens

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.chatapp.R
import com.compose.chatapp.presentation.intents.LoginIntent
import com.compose.chatapp.presentation.intents.LoginIntent.*
import com.compose.chatapp.presentation.viewStates.LoginViewState
import com.compose.chatapp.presentation.views.composables.ErrorMsgCard
import com.compose.chatapp.presentation.views.composables.LoadingDialog
import com.compose.domain.entities.User


@Composable
fun LoginScreen(
    viewState: State<LoginViewState>,
    setIntent: (LoginIntent) -> Unit
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
            label = { Text(stringResource(id = R.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = viewState.value.password,
            onValueChange = { setIntent(PasswordChanged(it)) },
            maxLines = 1,
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = if (viewState.value.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (viewState.value.isPasswordVisible)
                    painterResource(id = R.drawable.baseline_visibility_24)
                else
                    painterResource(id = R.drawable.baseline_visibility_off_24)
                IconButton(onClick = { setIntent(PasswordVisibilityChanged(viewState.value.isPasswordVisible)) }) {
                    val description =
                        if (viewState.value.isPasswordVisible) "Show password" else "Hide password"
                    Icon(painter = image, contentDescription = description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 30.dp)
        )

        viewState.value.errorMessage?.let {
            ErrorMsgCard(errorMsg = it)
        }

        if (viewState.value.isLoading) {
            LoadingDialog(loadingMsg = stringResource(id = R.string.signingIn)) {}
        }
        Button(
            onClick = {
                setIntent(
                    Login(
                        User(
                            email = viewState.value.email,
                            password = viewState.value.password
                        )
                    )
                )
            },
            enabled = viewState.value.isLoginEnabled,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                colorResource(id = R.color.primary_dark_blue), Color.White,
                colorResource(id = R.color.gray1), Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 5.dp)
        ) {
            Text(
                "LOG IN", modifier = Modifier.padding(12.dp), style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Row(modifier = Modifier.align(Alignment.End)) {
            Text(
                text = stringResource(id = R.string.newAccountMsg),
                color = Color.Gray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            Text(
                "Register",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                ),
                color = colorResource(
                    id = R.color.primary_dark_blue
                ),
                modifier = Modifier.clickable { setIntent(NavigateToRegister) },
                fontSize = 18.sp
            )
        }
    }
}
