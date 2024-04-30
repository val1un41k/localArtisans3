package com.example.local_artisan.screens.screens_all.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.ClickableLoginTextComponent
import com.example.local_artisan.components.DividerTextComponent
import com.example.local_artisan.components.HeadingTextComponentWithoutLogout
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.local_artisan.components.PasswordTextFieldComponent
import com.example.localartisan3.R

import com.example.localartisan3.data.sign_up.SignUpUIEvent
import com.example.localartisan3.data.sign_up.SignUpViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen

@Composable
fun CustomerSignUpScreen(
    signupViewModel: SignUpViewModel = viewModel(),
    ) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {


                item{
                    HeadingTextComponentWithoutLogout(value = "User Registration Screen")
                    Spacer(modifier = Modifier.height(20.dp))

                }

            item {

    MyTextFieldComponent(
        labelValue = stringResource(id = R.string.first_name),
        painterResource(id = R.drawable.profile),
        onTextChanged = {
            signupViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it))
        },
        errorStatus = signupViewModel.registrationUIState.value.firstNameError
    )
        }

                item{


                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.last_name),
                        painterResource = painterResource(id = R.drawable.profile),
                        onTextChanged = {
                            signupViewModel.onEvent(SignUpUIEvent.LastNameChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.lastNameError
                    )

                }

                item{

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.email),
                        painterResource = painterResource(id = R.drawable.message),
                        onTextChanged = {
                            signupViewModel.onEvent(SignUpUIEvent.EmailChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.emailError
                    )

                    Text(signupViewModel.registrationUIState.value.emailValidationError, color = Color.Red)

                }


                item{

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.phone_number),
                        painterResource = painterResource(id = androidx.core.R.drawable.ic_call_answer),
                        onTextChanged = {
                            signupViewModel.onEvent(SignUpUIEvent.PhoneNumberChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.emailError
                    )

                    Text(signupViewModel.registrationUIState.value.phoneNymberValidationError, color = Color.Red)
                }

                item{
                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        painterResource = painterResource(id = R.drawable.ic_lock),
                        onTextSelected = {
                            signupViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.passwordError
                    )

                    Text(signupViewModel.registrationUIState.value.passwordValidationError, color = Color.Red)

                    PasswordTextFieldComponent(
                        labelValue = stringResource(id = R.string.confirm_passowrd),
                        painterResource = painterResource(id = R.drawable.ic_lock),
                        onTextSelected = {
                            signupViewModel.onEvent(SignUpUIEvent.ConfirmPasswordChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.confirmPasswordError
                    )

                    Text(signupViewModel.registrationUIState.value.confirmPasswordValidationError, color = Color.Red)

                }


                item{
                    Spacer(modifier = Modifier.height(40.dp))

                    ButtonComponent(
                        value = stringResource(id = R.string.register),
                        onButtonClicked = {
                            signupViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
                            LocalArtisansRouter.navigateTo(Screen.CustomerLoginScreen)
                        },
                        isEnabled = signupViewModel.allValidationsPassed.value
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                }

                item{

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                        LocalArtisansRouter.navigateTo(Screen.CustomerLoginScreen)
                    })
                }
            }

        }

        if(signupViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }

}

@Preview
@Composable
fun SignUpScreenPreview() {
    CustomerSignUpScreen()
}