package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.CreateArtisanProductModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateArtisanProductRecordViewModel : ViewModel()
{
    private val TAG = CreateArtisanProductRecordViewModel::class.simpleName

    val artisanProductRecordUIstate = mutableStateOf(ArtisanProductRecordCreateUIstate())

    val isLoading = mutableStateOf(false)

    fun onEvent(event: CreateArtisanProductUIEvent){
        when(event){
            is CreateArtisanProductUIEvent.ProductNameChanged -> {
                artisanProductRecordUIstate.value.productName = event.productName
            }
            is CreateArtisanProductUIEvent.ProductDescriptionChanged -> {
                artisanProductRecordUIstate.value.productDescription = event.productDescription
            }
            is CreateArtisanProductUIEvent.ProductPriceChanged -> {
                artisanProductRecordUIstate.value.productPrice = event.productPrice
            }

            is CreateArtisanProductUIEvent.ProductImageChanged -> {
                artisanProductRecordUIstate.value.productImage = event.productImage
            }
            is CreateArtisanProductUIEvent.ProductCreateRecordButtonClicked -> {
                createNewProductRecordInFirebase()

            }
            is CreateArtisanProductUIEvent.NextProductRecordItemButtonClicked -> {
                clearEntryFields()
            }
            is CreateArtisanProductUIEvent.BackToDashboardButtonClicked -> {
                //TODO Navigate back to dashboard
                LocalArtisansRouter.navigateTo(Screen.ArtisanHomeScreen)
            }
            is CreateArtisanProductUIEvent.ProductDiscountChanged -> {
                artisanProductRecordUIstate.value.productDiscountPrice = event.productDiscount
            }
            is CreateArtisanProductUIEvent.QtyOnHandChanged -> {
                artisanProductRecordUIstate.value.qtyOnHand = event.qtyOnHand
            }
            is CreateArtisanProductUIEvent.DataFetchedFromFirestore -> {

            }


        }
    }
    fun takeDataFromFirestoreOfArtisan(){
        isLoading.value = true
        //TODO take data from firestore and place it in the values of artisanProductRecordUIstate
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)
        docRef.get().addOnFailureListener{
            //TODO Handle failure
            Log.d(TAG, "CrProdArt DocumentSnapshot data Collection Failed")
            isLoading.value = false
        }.addOnSuccessListener { document ->
            if (document != null){
                artisanProductRecordUIstate.value.artisanName = document.data?.get("fname").toString()
                artisanProductRecordUIstate.value.artisanSurname = document.data?.get("lname").toString()
                artisanProductRecordUIstate.value.artisanUID = uid
               var artisanCategories = document.data?.get("categories") as ArrayList<String>
                artisanProductRecordUIstate.value.artisanProductCategories = artisanCategories

                Log.d(TAG, "CrProdArt DocumentSnapshot data: ${document.data}")
                Log.d(TAG, "${artisanProductRecordUIstate.value.artisanName}  ${artisanProductRecordUIstate.value.artisanSurname}" +
                        "  ${artisanProductRecordUIstate.value.artisanUID}  ${artisanProductRecordUIstate.value.artisanProductCategories}")
                isLoading.value = false
            }
        }
    }

    fun createNewProductRecordInFirebase(){
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan_product_item_within_category")

        val newProductRecord = hashMapOf(
            "product_name" to artisanProductRecordUIstate.value.productName,
            "product_description" to artisanProductRecordUIstate.value.productDescription,
            "product_price" to artisanProductRecordUIstate.value.productPrice,
            "product_discount_price" to artisanProductRecordUIstate.value.productDiscountPrice,
            "product_category" to "pottery",
                    //artisanProductRecordUIstate.productCategory,
            "qty_on_hand" to artisanProductRecordUIstate.value.qtyOnHand,
            "artisan_uid" to uid,
            "artisan_name" to artisanProductRecordUIstate.value.artisanName,
            "artisan_surname" to artisanProductRecordUIstate.value.artisanSurname,

        )

        docRef.add(newProductRecord)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }

    fun filterWordsStartingWithCapitalLetter(input: String): List<String> {
        return input.split(", ").filter { it.isNotEmpty() && it[0].isUpperCase() }
    }

    fun putUrlofPictureFormFirestoreStorageToProductRecord(){


    }


    fun clearEntryFields(){
        artisanProductRecordUIstate.value.productName = ""
        artisanProductRecordUIstate.value.productDescription = ""
        artisanProductRecordUIstate.value.productPrice = 0.0
        artisanProductRecordUIstate.value.productDiscountPrice = 0.0
        artisanProductRecordUIstate.value.productCategory = ""
        artisanProductRecordUIstate.value.qtyOnHand = 0
        artisanProductRecordUIstate.value.productImage = null
    }

}


data class ArtisanProductRecordCreateUIstate(
    var productName: String = "",
    var productDescription: String = "",
    var productPrice: Double = 0.0,
    var productDiscountPrice: Double = 0.0,
    var artisanProductCategories: ArrayList<String> = ArrayList(),
    var productCategory: String = "",

    var qtyOnHand: Int = 0,
    var productImage: Uri? = null,


    var artisanUID: String = "",
    var artisanName: String = "",
    var artisanSurname: String = "",
)
