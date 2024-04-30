package com.example.local_artisan.screens.screens_all.artisan

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.local_artisan.components.ButtonComponent
import com.example.local_artisan.components.DisplayOnlyTextField
import com.example.local_artisan.components.HeadingTextComponentWithLogOut
import com.example.local_artisan.components.MyTextFieldComponent
import com.example.localartisan3.R

import com.example.localartisan3.components.selectPhotoFromGallaryforProfile
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.UpdateAartisansDetailsViewModel
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.UpdateArtisanDetailsUIEvent
import com.example.localartisan3.ui.theme.Primary


@Composable
fun UpdateArtisansPersonalDetails(
    updateAartisansDetailsViewModel: UpdateAartisansDetailsViewModel = viewModel()

) {
    updateAartisansDetailsViewModel
        .takingDataFromFirestoreAndPlaceToValuesOfArtisanUIState()

    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        LazyColumn(
        ) {

            item {

                HeadingTextComponentWithLogOut(
                    value = "Updating Artisans` Personal " +
                            "and Business Details"
                )
            }

            item {
                DisplayOnlyTextField(
                    "Artisan Account Name", updateAartisansDetailsViewModel
                        .artisanUIState.value.email
                )
            }
            item {

                Row() {
                    Column() {
                        MyTextFieldComponent(labelValue = updateAartisansDetailsViewModel.artisanUIState.value.fname,
                            painterResource(R.drawable.profile),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtisansNameChanged(it)
                                )
                            })

                        MyTextFieldComponent(labelValue = updateAartisansDetailsViewModel.artisanUIState.value.lname,
                            painterResource(R.drawable.profile),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtisansSurnameChanged(it)
                                )
                            })
                    }
                    Column() {
                        Spacer(modifier = Modifier.height(10.dp))
                        ButtonComponent(value = "Update", onButtonClicked = { })
                    }

                }
            }

            //TODO Create Component

            item {

                Spacer(modifier = Modifier.height(5.dp))
                Row() {
                    Column() {
                        MyTextFieldComponent(labelValue = updateAartisansDetailsViewModel.artisanUIState.value.pnum,
                            painterResource(androidx.core.R.drawable.ic_call_answer),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtsisansPhoneNumberChanged(it)
                                )
                            })
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Column() {
                        Spacer(modifier = Modifier.height(10.dp))
                        ButtonComponent(value = "Update", onButtonClicked = { })

                    }
                }

                Text(
                    text = updateAartisansDetailsViewModel
                        .artisanUIState.value.phoneNumValidateMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Column() {
                        Text(
                            text = "Artisan Photo",
                            modifier = Modifier
                                .heightIn(),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal
                            ),
                            color = Primary,
                        )
                    }
                    Column() {
                        //TODO Create button that will do make option update from directory.
                        selectPhotoFromGallaryforProfile(
                            updateAartisansDetailsViewModel.artisanUIState.value.artisanID
                        )
                    }
                }

            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Business Venue Address",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    color = Primary,
                    textAlign = TextAlign.Center
                )
                    Column() {
                        Spacer(modifier = Modifier.height(5.dp))
                        MyTextFieldComponent(labelValue = "address",
                            painterResource(R.drawable.message),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtisansStreetAddressChanged(it)
                                )
                            })
                        Spacer(modifier = Modifier.height(5.dp))
                        MyTextFieldComponent(labelValue = "address Longitude",
                            painterResource(R.drawable.message),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtisansAddressLongitudeChanged(
                                        it
                                    )
                                )
                            })
                        Spacer(modifier = Modifier.height(5.dp))
                        MyTextFieldComponent(labelValue = "address Latitude",
                            painterResource(R.drawable.message),
                            onTextChanged = {
                                updateAartisansDetailsViewModel.onEvent(
                                    UpdateArtisanDetailsUIEvent.ArtisansAddressLatitudeChanged(
                                        it
                                    )
                                )
                            })

                    }
                    Column() {
                        Spacer(modifier = Modifier.height(10.dp))
                        ButtonComponent(value = "Update Personal Details", onButtonClicked = {
                            updateAartisansDetailsViewModel.onEvent(UpdateArtisanDetailsUIEvent.ArtisansUpdateDetailsButtonClicked)
                        })
                    }

            }

            item {
                Spacer(modifier = Modifier.height(5.dp))
                //TODO Create Component ArtisanUpdateCancelButton
                ButtonComponent(value = "Cancel", onButtonClicked = {
                    updateAartisansDetailsViewModel.onEvent(UpdateArtisanDetailsUIEvent.ArtisansUpdateDetailsCencelButtonClicked)
                })
            }

        }
    }
}

@Preview
@Composable
fun PreviewUpdateArtisansPersonalDetails(){
    UpdateArtisansPersonalDetails()
}


data class UpdatingArtisanDetailsUIState(
    var email: String = "",
    var fname:String ="",
    var lname: String= "",
    var address: String = "",
    var addressLongitude: String = "",
    var addressLatitude: String = "",

    var pnum: String = "",
    var artisanID: String = "",

    var firstTime: Boolean = true,

    var updateProfileImage: Uri? = null,

    var phoneNumValid: Boolean = false,
    val phoneNumValidateMessage: String = "",
    val categories: ArrayList<String> = ArrayList(),
)