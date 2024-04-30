package com.example.local_artisan.screens.screens_all.customer

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.ClickableLoginTextComponent
import com.example.local_artisan.components.DividerTextComponent
import com.example.local_artisan.components.HeadingTextComponentWithoutLogout
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.local_artisan.components.PasswordTextFieldComponent
import com.example.local_artisan.components.UnderLinedTextComponent
import com.example.localartisan3.R

import com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels.CustomerMainViewModel
import com.example.localartisan3.data.screens_view_models.login.LoginUIEvent
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun CustomerLoginScreen(
    loginViewModel: CustomerMainViewModel = viewModel(),
                        )
 {


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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    HeadingTextComponentWithoutLogout(value = "Customer Login Screen")
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    MyTextFieldComponent(
                        labelValue = stringResource(R.string.email),
                        painterResource(R.drawable.message),
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError
                    )

                    Text(
                        text = loginViewModel.emailValidationMessage.value,
                        color = Color.Red
                    )
                }
                item {
                    PasswordTextFieldComponent(
                        labelValue = stringResource(R.string.password),
                        painterResource(R.drawable.lock),
                        onTextSelected = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError
                    )

                    Text(loginViewModel.passwordValidationMessage.value, color = Color.Red)
                }

                //add text that will be checking the
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    UnderLinedTextComponent(onTextSelected = {
                        LocalArtisansRouter.navigateTo(Screen.ResetPasswordScreen)
                    })
                }
                //underlined text that will navigate to the reset password screen
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = loginViewModel.LoginErrorMessage.value)
                    Spacer(modifier = Modifier.height(20.dp))

                    ButtonComponent(
                        value = stringResource(R.string.login),

                        onButtonClicked = {
                            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        }
                    )


                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                        LocalArtisansRouter.navigateTo(Screen.CustomerSignUpScreen)
                    })
                }
            }
        }

        if(loginViewModel.loginInProgress.value) {
        CircularProgressIndicator()
     }
    }


}

@Preview
@Composable
fun CustomerLoginScreenPreview() {
    CustomerLoginScreen()
}