package com.example.localartisan3.screens.screens_all.artisan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.HeadingTextComponentWithLogOut

import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.AllArtisansScreensViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen


@Composable
fun ArtisanHomeScreen(
    allArtisansScreensViewModel: AllArtisansScreensViewModel = viewModel()
) {

    allArtisansScreensViewModel.onArtisanLogin()

    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            HeadingTextComponentWithLogOut(value = "Artisan's Home Screen")
            Spacer(modifier = Modifier.height(40.dp))


            ButtonComponent(value = "Update Personal and Business Details", onButtonClicked = {
                LocalArtisansRouter.navigateTo(Screen.UpdateArtisansPersonalDetails)
            })
            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(value = "Create Product Category", onButtonClicked = {
                LocalArtisansRouter.navigateTo(Screen.ArtisanCreateProductCategoryProfile)
            })
            Spacer(modifier = Modifier.height(40.dp))


            ButtonComponent(value = "Create Product Within Category", onButtonClicked = {
                allArtisansScreensViewModel.passDataToCreateProductRecord()
                LocalArtisansRouter.navigateTo(Screen.ArtisansCreateProductWithinCategory)
            })
            Spacer(modifier = Modifier.height(40.dp))


            ButtonComponent(value = "View Customer Request " +
                    "\n Send Notifications and Invoices", onButtonClicked = {
                  LocalArtisansRouter.navigateTo(Screen.ArtisanViewCustomerRequest)
            })
            Spacer(modifier = Modifier.height(40.dp))


            ButtonComponent(value = "View Customer`s Feedback and Rating", onButtonClicked = {

                LocalArtisansRouter.navigateTo(Screen.ArtisanViewFeedbackAndRating)
            })
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}