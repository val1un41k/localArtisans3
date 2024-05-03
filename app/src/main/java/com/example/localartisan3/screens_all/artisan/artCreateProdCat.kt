package com.example.localartisan3.screens.screens_all.artisan

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ArtisanCategoryDropdown
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.DisplayOnlyTextField
import com.example.local_artisan.components.HeadingTextComponentWithLogOut

import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.AllArtisansScreensViewModel
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ProductCategory
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen


@Composable
fun ArtisanCreateProductCategoryProfile(allArtisansScreensViewModel:
                                        AllArtisansScreensViewModel = viewModel(),
){
    var context = LocalContext.current
    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            HeadingTextComponentWithLogOut(value = "Artisan`s Product Category Profile")

            DisplayOnlyTextField("Artisan`s Name & Surname",
                allArtisansScreensViewModel.artisanUIState.value.fname +
                        " " + allArtisansScreensViewModel.artisanUIState.value.lname)

                var categoriesToDisplay = ""
                allArtisansScreensViewModel.artisanUIState
                    .value.categories.forEach { category ->
                    categoriesToDisplay += category.ProdCatName + " \n"}

            DisplayOnlyTextField("Artisan`s Already Existing Product Categories",
                categoriesToDisplay)

            Spacer(modifier = Modifier.height(10.dp))

              val selectedCategory = ArtisanCategoryDropdown(allArtisansScreensViewModel.
              availableCategories.value){

           }

            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(value = "Create Category Record", onButtonClicked = {
                allArtisansScreensViewModel.addCategoryforArtisan(selectedCategory)
               allArtisansScreensViewModel.UpdateCategoriesStateInFirestore(context)

            })
            Text(allArtisansScreensViewModel.UpdateCategoryMessage.value)

            Spacer(modifier = Modifier.height(35.dp))

            ButtonComponent(value = "Back to Dashboard", onButtonClicked = {
                LocalArtisansRouter.navigateTo(Screen.ArtisanHomeScreen) })
        }

    }
}