package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.local_artisan.screens.screens_all.artisan.UpdatingArtisanDetailsUIState
import com.example.localartisan3.data.rules.Validator
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateAartisansDetailsViewModel : ViewModel()  {

    private val TAG = UpdateAartisansDetailsViewModel::class.simpleName

    var artisanUIState = mutableStateOf(UpdatingArtisanDetailsUIState())

    var allValidationsPassed = mutableStateOf(false)

    var artisanID = FirebaseAuth.getInstance().currentUser?.uid.toString()


    fun onEvent (event: UpdateArtisanDetailsUIEvent){
        when(event){
            is UpdateArtisanDetailsUIEvent.ArtisansNameChanged -> {
                artisanUIState.value = artisanUIState.value.copy(fname = event.ArtisansName)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansSurnameChanged -> {
                artisanUIState.value = artisanUIState.value.copy(lname = event.ArtisansSurname)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtsisansPhoneNumberChanged -> {
                val phoneNumValidateMessage = validatePhoneNumber(event.ArtsisansPhoneNumber)
                artisanUIState.value = artisanUIState.value.copy(pnum = event.ArtsisansPhoneNumber,
                    phoneNumValidateMessage = phoneNumValidateMessage)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansStreetAddressChanged -> {
                artisanUIState.value = artisanUIState.value.copy(address = event.ArtisansAddress)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansAddressLongitudeChanged -> {
                artisanUIState.value = artisanUIState.value.copy(addressLongitude = event.ArtisansAddressLongitude)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansAddressLatitudeChanged -> {
                artisanUIState.value = artisanUIState.value.copy(addressLatitude = event.ArtisansAddressLatitude)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansUpdateDetailsButtonClicked -> {

                //TODO Update Artisan details in Firestore

                updateArtisanDetailsInFirestore()

                printState()

                LocalArtisansRouter.navigateTo(Screen.ArtisanHomeScreen)
            }
            is UpdateArtisanDetailsUIEvent.ArtisansUpdateDetailsCencelButtonClicked -> {
              // TODO Reset pot update   allValidationsPassed.value = false#
                takingDataFromFirestoreAndPlaceToValuesOfArtisanUIState()
                
                printState()
            }

            else -> {}
        }
        validateUpdateArtisanDetailsUIDataWithRules()
    }

    private fun validateUpdateArtisanDetailsUIDataWithRules() {
        val phoneNumResult = Validator.validatePhoneNumber(phoneNumber = artisanUIState.value.pnum)

        artisanUIState.value = artisanUIState.value.copy(phoneNumValid = phoneNumResult.status)

        allValidationsPassed.value = phoneNumResult.status
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, artisanUIState.value.toString())
    }

    fun getArtisanEmailfromFirestore() {
        //TODO get Artisan email from Firestore
        val uid = artisanUIState.value.artisanID
        var artisanEmail = ""

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("Artisans").document().get().addOnSuccessListener { document ->
                if (document != null && document.exists() && document.exists() && document.contains(
                        "email"
                    )
                ) {
                    artisanEmail = document.data?.get("email").toString()
                    Log.d(TAG, "Artisan Email = $artisanEmail")

                    artisanUIState.value = artisanUIState.value.copy(email = artisanEmail)

                } else {
                    Log.d(TAG, "Document does not exist")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        }
    }


    fun validatePhoneNumber(phoneNumber: String): String {
        var phoneNumValidateMessage = ""
        if (!Validator.validatePhoneNumber(phoneNumber).status){
            phoneNumValidateMessage = "Phone number must be 10 digits long and start with 085, 083, 086, 087 or 089"
        }
        return phoneNumValidateMessage
    }

    fun takingDataFromFirestoreAndPlaceToValuesOfArtisanUIState() {

        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)
        docRef.get().addOnFailureListener{
            Log.d(TAG, "Failed to get from document")
        }
            .addOnSuccessListener { document ->
            if (document != null) {
                artisanUIState.value.fname = document.data?.get("fname").toString()
                artisanUIState.value.lname = document.data?.get("lname").toString()
                artisanUIState.value.pnum = document.data?.get("pnum").toString()
                artisanUIState.value.address = document.data?.get("address").toString()
                artisanUIState.value.addressLongitude = document.data?.get("addressLongitude").toString()
                artisanUIState.value.addressLatitude = document.data?.get("addressLatitude").toString()
                artisanUIState.value.email = document.data?.get("email").toString()
                artisanUIState.value.artisanID = uid

            }
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")

        }
    }

    fun updateArtisanDetailsInFirestore() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)

        docRef.update(
            "fname", artisanUIState.value.fname,
            "lname", artisanUIState.value.lname,
            "pnum", artisanUIState.value.pnum,
            "address", artisanUIState.value.address,
            "addressLongitude", artisanUIState.value.addressLongitude,
            "addressLatitude", artisanUIState.value.addressLatitude,
            "firstTime", false,

        ).addOnSuccessListener {
            Log.d(TAG, "Arisan Updated Details successfully updated!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error to Update Artisan document", e)
        }
    }

}