package com.example.localartisan3.data.screens_view_models.users_data

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
    var categories: ArrayList<String> = ArrayList(),
    var categoriesToFirestore: String = "",
    var categoriesFromFirestore: ArrayList <String> = ArrayList()

    ) {

}
