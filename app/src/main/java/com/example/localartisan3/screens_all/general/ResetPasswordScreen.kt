package com.example.local_artisan.screens.screens_all.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.DisplayOnlyTextField
import com.example.local_artisan.components.HeadingTextComponentWithoutLogout
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.localartisan3.R

import com.example.localartisan3.data.screens_view_models.login.LoginUIEvent
import com.example.localartisan3.data.screens_view_models.login.LoginViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen


@Composable
fun ResetPasswordScreen(loginViewModel: LoginViewModel = viewModel(),
                        ) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp),
    ) {
        HeadingTextComponentWithoutLogout(value = "Reset Password Screen")
        Column(
            Modifier.fillMaxSize(),
            Arrangement.Center
        ) {
            //TODO Add Component Email and address per registration
            Spacer(modifier = Modifier.height(30.dp))
            DisplayOnlyTextField ( labelValue = "",
                textValue = "Email address as per Registration" )
            MyTextFieldComponent(
                labelValue = stringResource(R.string.email),
                painterResource(R.drawable.message),
                onTextChanged = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.emailError
            )
            Spacer(modifier = Modifier.height(80.dp))

            var resetMessage = ""
            ButtonComponent(
                value = stringResource(R.string.reset_password),
                onButtonClicked = {
                    resetMessage = loginViewModel.onEvent(LoginUIEvent.
                    ResetPasswrodButtonClicked).toString()
                }
            )
            if(resetMessage.equals("Email sent.")){
                Text(text = "Email sent.")
                LocalArtisansRouter.navigateTo(Screen.HomeScreen)
            }else if(resetMessage.equals("Account not exists.")){
                Text(text = "Account Not Exists. Please Register.")
            }
        }
    }
}

@Composable
@Preview
fun ResetPasswordScreenPreview(){
    ResetPasswordScreen()
}
