package com.example.dondeva.presentation.authentication.sign_up

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dondeva.R
import com.example.dondeva.data.impl.AccountServiceImpl
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.ui.theme.AppTheme
import kotlinx.coroutines.launch
import com.example.dondeva.presentation.authentication.AuthenticationButton

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    onSignInRequired: () -> Unit,
    modifier: Modifier = Modifier,
    accountService: AccountService,
) {
    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(accountService),
    )
    val errorMessages = viewModel.errorEvents

    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val confirmPassword = viewModel.confirmPassword.collectAsState()
    val isSigningUp = viewModel.isSigningUp.collectAsState()

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmationPassword by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        errorMessages.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = context.resources.getString(message))
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.imePadding())
        },
    ) { innerPadding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Logo App Image",
            )
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
                        shape = MaterialTheme.shapes.large,
                    )
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                value = email.value,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = {
                    Text(text = stringResource(R.string.email))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
                        shape = MaterialTheme.shapes.large,
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                value = password.value,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = {
                    Text(text = stringResource(R.string.password))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Email",
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outline),
                        shape = MaterialTheme.shapes.large,
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                value = confirmPassword.value,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                placeholder = {
                    Text(text = stringResource(R.string.confirm_password))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Email",
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showConfirmationPassword = !showConfirmationPassword }) {
                        Icon(
                            if (showConfirmationPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showConfirmationPassword) "Hide password" else "Show password",
                        )
                    }
                },
                visualTransformation = if (showConfirmationPassword) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (isSigningUp.value) Unit else viewModel.onSignUpClick(openAndPopUp)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = if (isSigningUp.value) "${stringResource(R.string.signing_up)}..."
                    else stringResource(R.string.sign_up),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(0.dp, 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.or))
            Spacer(modifier = Modifier.height(8.dp))

            AuthenticationButton(R.string.sign_up_with_google) {
                viewModel.handleGoogleSignUpWithBottomSheet(context, openAndPopUp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onSignInRequired) {
                Text(text = stringResource(R.string.account_already_created))
            }
        }
    }
}


@Preview
@Composable
fun SignUpScreenPreview() = AppTheme {
    SignUpScreen(
        openAndPopUp = { a, b -> {} },
        onSignInRequired = {},
        accountService = AccountServiceImpl(),
    )
}