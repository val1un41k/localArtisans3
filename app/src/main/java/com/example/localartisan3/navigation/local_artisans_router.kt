package com.example.localartisan3.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen (val route: String, val name: String = route){
    //General Screens
    object HomeScreen: Screen ("HomeScreen", name = "Home Screen")
    object ResetPasswordScreen: Screen ("ResetPasswordScreen", name = "Reset Password")

    //Artisan Screens
    object ArtisanCreateProductCategoryProfile: Screen ("ArtisanCreateProductCategoryProfile",
        name = "Create Product Category Profile")

    object ArtisansCreateProductWithinCategory: Screen ("ArtisansCreateProductWithinCategory",
        name = "Create Product Within Category")

    object ArtisanLoginScreen: Screen ("ArtisanLoginScreen",
        name = "Artisan Login Screen")

    object ArtisanHomeScreen: Screen ("ArtisanHomeScreen", name = "Artisan Home Screen")

    object UpdateArtisansPersonalDetails: Screen ("UpdateArtisansPersonalDetails",
        name = "Update Artisans Personal Details")

    object ArtisanViewCustomerRequest: Screen ("artisanViewCustomerRequest",
        name = "View Customer Request")

    object ArtisanViewFeedbackAndRating: Screen ("artisanViewFeedbackAndRating",
        name = "View Feedback And Rating")

    //Customer Screens

    object CustomerHomeDashboardScreen: Screen ("CustomerHomeDashboardScreen",
        name = "Customer Home Dashboard Screen")

    object CustomerLoginScreen: Screen ("CustomerLoginScreen",
        name = "Customer Login Screen")

    object CustomerSignUpScreen: Screen ("CustomerSignUpScreen",
        name = "Customer Sign Up Screen")

    object CustomerCreateAndFundDigitalWallet: Screen ("CustomerCreateAndFundDigitalWallet",
        name = "Create And Fund Digital Wallet")

    object CusotmerListOfNotifications: Screen ("CusotmerListOfNotifications",
        name = "List Of Notifications")

    object CustomerListOfRecievedInvoices: Screen ("CustomerListOfRecievedInvoices",
        name = "List Of Recieved Invoices")

}

object LocalArtisansRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.HomeScreen)

    fun navigateTo(destination: Screen){
        currentScreen.value = destination
    }
}

var customerScreens = listOf(
    Screen.CustomerHomeDashboardScreen,
    Screen.CustomerCreateAndFundDigitalWallet,
    Screen.CusotmerListOfNotifications,
    Screen.CustomerListOfRecievedInvoices
)

var artisanScreens = listOf(
    Screen.ArtisanCreateProductCategoryProfile,
    Screen.ArtisansCreateProductWithinCategory,
    Screen.ArtisanHomeScreen,
    Screen.UpdateArtisansPersonalDetails,
    Screen.ArtisanViewCustomerRequest,
    Screen.ArtisanViewFeedbackAndRating
)
//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
//        composable(route = Screen.HomeScreen.route) {
//            HomeScreen(navController)
//        }
//        composable(route = Screen.ResetPasswordScreen.route) {
//            ResetPasswordScreen(navController)
//        }
//
//        // navigate artisans screens
//        composable(route = Screen.ArtisanCreateProductCategoryProfile.route) {
//            ArtisanCreateProductCategoryProfile(navController)
//        }
//        composable(route = Screen.ArtisansProductCategoryProfile.route) {
//            ArtisansProductCategoryProfile(navController)
//        }
//        composable(route = Screen.ArtisanHomeScreen.route) {
//            ArtisanHomeScreen(navController)
//        }
//        composable(route = Screen.UpdateArtisansPersonalDetails.route) {
//            UpdateArtisansPersonalDetails(navController)
//        }
//
//        // navigate customer screens
//        composable(route = Screen.CustomerHomeDashboardScreen.route) {
//            CustomerHomeDashboardScreen(navController)
//        }
//
//        composable(route = Screen.CustomerLoginScreen.route) {
//            CustomerLoginScreen(navController)
//        }
//
//        composable(route = Screen.CustomerSignUpScreen.route) {
//            CustomerSignUpScreen(navController)
//        }
//    }
//
//}
//


