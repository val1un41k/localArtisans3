package com.example.localartisan3.screens.screens_all.artisan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ArtisanCategoryDropdown
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.DisplayOnlyTextField
import com.example.local_artisan.components.HeadingTextComponentWithLogOut
import com.example.local_artisan.components.IntegerDropdown
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.localartisan3.R

import com.example.localartisan3.components.selectPhotoFromGallaryforProduct
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.AllArtisansScreensViewModel
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.CreateArtisanProductModel.CreateArtisanProductUIEvent


@Composable
fun ArtisansCreateProductWithinCategory(createArtisanProductRecordViewModel:
                                        AllArtisansScreensViewModel = viewModel()
) {

 //createArtisanProductRecordViewModel.takeDataFromFirestoreOfArtisan()

    createArtisanProductRecordViewModel.passDataToCreateProductRecord()


        var oneclickCreateProfile = false

        Surface(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    HeadingTextComponentWithLogOut(value = "Artisan's Product Category Profile")
               }
                item {
                    //TODO Create TextComponent ArtisanNameFromLogin
                    var artisanName =
                        createArtisanProductRecordViewModel.artisanProductRecordUIstate.value.artisanName
                    var artisanSecndName =
                        createArtisanProductRecordViewModel.artisanProductRecordUIstate.value.artisanSurname
                    DisplayOnlyTextField(
                        "${artisanName}  ${artisanSecndName}",
                        "${artisanName}  ${artisanSecndName}"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    var categories =
                        createArtisanProductRecordViewModel.
                        artisanProductRecordUIstate.
                        value.artisanProductCategories
                                  var selectedCategory =ArtisanCategoryDropdown(categories)


                }
                item {
                    MyTextFieldComponent(
                        labelValue = "Product Item Name",
                        painterResource(R.drawable.profile),
                        onTextChanged = {
                            createArtisanProductRecordViewModel.onEvent(
                               CreateArtisanProductUIEvent.ProductNameChanged(it)
                            )
                        },
                    )
                    MyTextFieldComponent(
                        labelValue = "Product Item Description",
                        painterResource(R.drawable.profile),
                        onTextChanged = {
                            createArtisanProductRecordViewModel.onEvent(
                                CreateArtisanProductUIEvent.ProductDescriptionChanged(it)
                            )
                        },
                    )
                }

                item {
                    Row() {
                        Column() {
                            MyTextFieldComponent(
                                labelValue = "Product Item Price (EUR)",
                                painterResource(R.drawable.profile),
                                onTextChanged = {
                                    createArtisanProductRecordViewModel.onEvent(
                                        CreateArtisanProductUIEvent.ProductPriceChanged(it.toDouble())
                                    )
                                },
                            )
                        }

                    }
                    Column() {
                        MyTextFieldComponent(
                            labelValue = "Discounted Price (EUR)",
                            painterResource(R.drawable.profile),
                            onTextChanged = {
                                createArtisanProductRecordViewModel.onEvent(
                                    CreateArtisanProductUIEvent.ProductDiscountChanged(it.toDouble())
                                )
                            },
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Product Quantity on hand")
                    createArtisanProductRecordViewModel
                        .artisanProductRecordUIstate.value.qtyOnHand = IntegerDropdown()

                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))

                   //TODO ProductItemPicture Upload
                    selectPhotoFromGallaryforProduct(
                        createArtisanProductRecordViewModel.artisanProductRecordUIstate.value
                            .artisanUID,
                        createArtisanProductRecordViewModel.artisanProductRecordUIstate.value.productCategory
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonComponent(value = "Create Product Item Record", onButtonClicked = {
                        createArtisanProductRecordViewModel.onEvent(
                            CreateArtisanProductUIEvent.ProductCreateRecordButtonClicked
                        )
                        oneclickCreateProfile = true
                    }, isEnabled = !oneclickCreateProfile)
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    ButtonComponent(value = "Next Product Item", onButtonClicked = {
                        createArtisanProductRecordViewModel.onEvent(
                            CreateArtisanProductUIEvent.NextProductRecordItemButtonClicked
                        )
                        oneclickCreateProfile = false
                    })
                }
                item {
                   Spacer(modifier = Modifier.height(10.dp))
                    ButtonComponent(value = "Back to Dashboard", onButtonClicked = {
                        createArtisanProductRecordViewModel.onEvent(
                            CreateArtisanProductUIEvent.BackToDashboardButtonClicked
                        )
                    })
                }

           }
        }
    }

