package com.example.localartisan3.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.screens.screens_all.artisan.ArtisanLoginScreen
import com.example.local_artisan.screens.screens_all.artisan.ArtisanViewCustomerRequest
import com.example.local_artisan.screens.screens_all.artisan.ArtisanViewFeedbackAndRating
import com.example.local_artisan.screens.screens_all.artisan.UpdateArtisansPersonalDetails
import com.example.local_artisan.screens.screens_all.customer.CustomerHomeDashboardScreen
import com.example.local_artisan.screens.screens_all.customer.CustomerLoginScreen
import com.example.local_artisan.screens.screens_all.customer.CustomerSignUpScreen
import com.example.local_artisan.screens.screens_all.general.HomeScreen
import com.example.local_artisan.screens.screens_all.general.ResetPasswordScreen
import com.example.localartisan3.data.screens_view_models.home.HomeViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.example.localartisan3.screens.screens_all.artisan.ArtisanCreateProductCategoryProfile
import com.example.localartisan3.screens.screens_all.artisan.ArtisanHomeScreen

import com.example.localartisan3.screens.screens_all.artisan.ArtisansCreateProductWithinCategory
import com.example.localartisan3.screens_all.customer.CusotmerListOfNotifications
import com.example.localartisan3.screens_all.customer.CustomerCreateAndFundDigitalWallet
import com.example.localartisan3.screens_all.customer.CustomerListOfRecievedInvoices


@Composable
fun LocalArtisanApplication (homeViewModel: HomeViewModel = viewModel(),
                             ) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        if (homeViewModel.isUserLoggedIn.value == false) {
            LocalArtisansRouter.navigateTo(Screen.HomeScreen)
        }
        else{
            //check what type of user is logged in
            //navigate related to type.
        }

        Crossfade(targetState = LocalArtisansRouter.currentScreen){
            when(it.value) {
                is Screen.HomeScreen -> {
                    HomeScreen()
                }
                is Screen.ResetPasswordScreen -> {
                    ResetPasswordScreen()
                }
                /////////////////////////////
                //////
                //ArtisanScreens

                is Screen.ArtisanCreateProductCategoryProfile -> {
                    ArtisanCreateProductCategoryProfile()
                }

                is Screen.ArtisansCreateProductWithinCategory -> {
                    ArtisansCreateProductWithinCategory()
                }

                is Screen.ArtisanLoginScreen -> {
                    ArtisanLoginScreen()
                }

                is Screen.ArtisanHomeScreen -> {
                    ArtisanHomeScreen()
                }

                is Screen.UpdateArtisansPersonalDetails -> {
                    UpdateArtisansPersonalDetails()
                }

                is Screen.ArtisanViewCustomerRequest -> {
                    ArtisanViewCustomerRequest()
                }

                is Screen.ArtisanViewFeedbackAndRating -> {
                    ArtisanViewFeedbackAndRating()
                }
                /////////////////////////////
                /////////////////////////////
                /////////////////////////////
                //CustomerScreens

                is Screen.CustomerHomeDashboardScreen -> {
                    CustomerHomeDashboardScreen()
                }

                is Screen.CustomerLoginScreen -> {
                    CustomerLoginScreen()
                }

                is Screen.CustomerSignUpScreen -> {
                    CustomerSignUpScreen()
                }

                is Screen.CusotmerListOfNotifications ->{
                    CusotmerListOfNotifications()
                }

                is Screen.CustomerCreateAndFundDigitalWallet -> {
                    CustomerCreateAndFundDigitalWallet()
                }

                is Screen.CustomerListOfRecievedInvoices -> {
                    CustomerListOfRecievedInvoices()
                }

            }
        }

//        if (homeViewModel.isUserLoggedIn.value == true) {
//            LocalArtisansRouter.navigareTo(Screen.HomeScreen)
//
//        } else {
//            homeViewModel.isUserLoggedIn.observeForever {
//                if (it) {
//                    homeViewModel.readresationCurrentUser()
//                }
//            }
//        }

    }

}