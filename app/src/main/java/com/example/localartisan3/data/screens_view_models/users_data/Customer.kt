package com.example.localartisan3.data.screens_view_models.users_data

import android.net.Uri
import com.example.local_artisan.screens.screens_all.customer.LoadedProductFromDB
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ProductCategory


data class Customer (
    var email : String = "",
    var fname :String ="",
    var lname: String= "",
    var pnum: String ="",
    var userID: String = "",)


data class ArtisanUIState(
    var email : String = "",
    var fname :String ="",
    var lname: String= "",
    var address: String = "",
    var addressLongitude: String = "",
    var addressLatitude: String = "",

    var pnum: String = "",
    var userID: String = "",
    var artisanID: String = "",

    val phoneNumValid: Boolean = false,
    val phoneNumValidateMessage: String = "",
    var categories: ArrayList<ProductCategory> = ArrayList(),
    var products_within_categories: ArrayList<LoadedProductFromDB> = ArrayList(),
    var categoriesToFirestore: String = "",
    var categoriesFromFirestore: ArrayList <String> = ArrayList(),
    var artisanProfilePicure: Uri? = null,
    )

