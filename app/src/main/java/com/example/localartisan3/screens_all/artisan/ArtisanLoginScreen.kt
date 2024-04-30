package com.example.local_artisan.screens.screens_all.artisan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
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
import com.example.local_artisan.components.DividerTextComponent
import com.example.local_artisan.components.HeadingTextComponentWithoutLogout
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.local_artisan.components.PasswordTextFieldComponent
import com.example.local_artisan.components.UnderLinedTextComponent
import com.example.localartisan3.R

import com.example.localartisan3.data.screens_view_models.login.LoginUIEvent
import com.example.localartisan3.data.screens_view_models.login.LoginViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen


@Composable
fun ArtisanLoginScreen(loginViewModel: LoginViewModel = viewModel()
                       ) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        androidx.compose.material.Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                HeadingTextComponentWithoutLogout(value = "Artisan Login Screen")

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = LoginViewModel().LoginErrorMessage.value,
                    color = Color.Red)
                Spacer(modifier = Modifier.height(10.dp))
                MyTextFieldComponent(
                    labelValue = stringResource(R.string.email),
                    painterResource(R.drawable.message),
                    onTextChanged = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError
                )
                Text(loginViewModel.emailValidationMessage.value,
                    color = Color.Red)

                Spacer(modifier = Modifier.height(40.dp))

                PasswordTextFieldComponent(
                    labelValue = stringResource(R.string.password),
                    painterResource(R.drawable.lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError
                )
                Text(loginViewModel.passwordValidationMessage.value,
                    color = Color.Red)

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = stringResource(R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.ArtisanLoginButtonClicked)
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )

                Text(text = loginViewModel.LoginErrorMessage.value,
                    color = Color.Red)


                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                UnderLinedTextComponent( onTextSelected ={
                    LocalArtisansRouter.navigateTo(Screen.ResetPasswordScreen)
                })
            }
        }

        if(loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }

    //when pressing back button back to home screen
    //TODO fix bug related to back button
//    SystemBackButtonHandler( onBackPressed = {
//        LocalArtisansRouter.navigateTo(Screen.HomeScreen)
//    }
//    )

}

@Preview
@Composable
fun ArtisanLoginScreenPreview() {
    ArtisanLoginScreen()
}
