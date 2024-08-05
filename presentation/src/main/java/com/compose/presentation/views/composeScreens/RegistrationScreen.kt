package com.compose.presentation.views.composeScreens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.presentation.R
import com.compose.presentation.intents.RegistrationIntent
import com.compose.presentation.intents.RegistrationIntent.*
import com.compose.presentation.viewStates.RegistrationViewState
import com.compose.presentation.views.composables.ErrorMsgCard
import com.compose.presentation.views.composables.LoadingDialog

@Composable
fun RegistrationScreen(
    viewState: State<RegistrationViewState>,
    setIntent: (RegistrationIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { setIntent(NavigateToLogin) },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.backToLogin)
            )
        }

        Text(
            text = stringResource(id = R.string.registration),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontWeight = FontWeight.Bold),
            letterSpacing = 2.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))
            OutlinedTextField(
                value = viewState.value.email,
                onValueChange = { setIntent(EmailChanged(it)) },
                maxLines = 1,
                label = { Text(stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(15.dp))
            OutlinedTextField(
                value = viewState.value.username,
                onValueChange = { setIntent(UsernameChanged(it)) },
                maxLines = 1,
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(15.dp))
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(15.dp))
            OutlinedTextField(
                value = viewState.value.confirmationPassword,
                onValueChange = { setIntent(ConfirmationPasswordChanged(it)) },
                maxLines = 1,
                label = { Text("Confirm Password") },
                visualTransformation = if (viewState.value.isConfirmationPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (viewState.value.isConfirmationPasswordVisible)
                        painterResource(id = R.drawable.baseline_visibility_24)
                    else
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    IconButton(onClick = { setIntent(ConfirmationPasswordVisibilityChanged(viewState.value.isConfirmationPasswordVisible)) }) {
                        val description =
                            if (viewState.value.isConfirmationPasswordVisible) "Show password" else "Hide password"
                        Icon(painter = image, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(20.dp))

            viewState.value.errorMessage?.let {
                ErrorMsgCard(errorMsg = it)
            }

            if (viewState.value.isLoading) {
                LoadingDialog(loadingMsg = stringResource(id = R.string.signingIn)) {}
            }

            if (viewState.value.isSuccess) {
                Toast.makeText(LocalContext.current, "Register Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            Button(
                onClick = { setIntent(Register) },
                enabled = viewState.value.isRegisterEnabled,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    colorResource(id = R.color.primary_dark_blue), Color.White,
                    colorResource(id = R.color.gray1), Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text("REGISTER", modifier = Modifier.padding(14.dp))
            }
        }
    }
}



