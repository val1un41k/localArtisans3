package com.example.local_artisan.screens.screens_all.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.HeadingTextComponentWithoutLogout


import com.example.localartisan3.data.screens_view_models.home.HomeViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
               ) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    homeViewModel.checkForActiveSession()
   // homeViewModel.readresationCurrentUser()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn {
                item{
                    HeadingTextComponentWithoutLogout(value = "Application Home Screen")
                    Spacer(modifier = Modifier.height(80.dp))
                }
                item{
                    ButtonComponent(value = "Customer User",
                        onButtonClicked = {
                            //navigate to customer login screen

                            homeViewModel.changeCustomerRoleToCusotmer()
                            LocalArtisansRouter.navigateTo(Screen.CustomerLoginScreen)
                        })
                    Spacer (modifier = Modifier.height(120.dp))
                }
              //  TODO Add Component Craft Maket User
                item{
                    ButtonComponent(value = "Craft Market User",
                        onButtonClicked = {
                            //navigate to craft market login screen
                            homeViewModel.changeCustomerRoleToArtisan()
                            LocalArtisansRouter.navigateTo(Screen.ArtisanLoginScreen)
                        })
                }

            }
        }
    }


@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}

