package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.localartisan3.components.productDownloadUri
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.CreateArtisanProductModel.CreateArtisanProductUIEvent
import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference

class AllArtisansScreensViewModel : ViewModel(){

    private val TAG = AllArtisansScreensViewModel::class.simpleName
    var artisanUIState = mutableStateOf(ArtisanUIState())
    var UpdateCategoryMessage = mutableStateOf("")

    fun onArtisanLogin(){
        //TODO takes from Firestore the Artisan details and send to artisanUIState
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                artisanUIState.value.fname = document.data?.get("fname").toString()
                artisanUIState.value.lname = document.data?.get("lname").toString()
                artisanUIState.value.pnum = document.data?.get("pnum").toString()
                artisanUIState.value.address = document.data?.get("address").toString()
                artisanUIState.value.addressLongitude = document.data?.get("addressLongitude").toString()
                artisanUIState.value.addressLatitude = document.data?.get("addressLatitude").toString()
                artisanUIState.value.email = document.data?.get("email").toString()
                artisanUIState.value.artisanID = uid
                //take array of categories from Firestore and Store it as an Array of Strings

                var data = document.data?.get("categories") as? ArrayList<String>
                artisanUIState.value.categories = data ?: arrayListOf()
            }
            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
        }

    }

    fun addCategoryforArtisan(category: String){
        artisanUIState.value.categories.add(category)
        Log.d("Updated categories", artisanUIState.value.categories.toString())
    }

    fun UpdateCategoriesStateInFirestore(){

        //replace values of field categories inside artisan document in Firestore
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)
        artisanUIState.value.categories=ArrayList(artisanUIState.value.categories.toSet())
        docRef.update("categories", artisanUIState.value.categories)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")

                UpdateCategoryMessage.value = "Category added successfully"}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)
                UpdateCategoryMessage.value = "Error adding category"
            }

    }

//
// Functions Related to ArtisanCreateProductWithinCategory
//
    val artisanProductRecordUIstate = mutableStateOf(ArtisanProductRecordCreateUIstate())

    val isLoading = mutableStateOf(false)

    fun onEvent(event: CreateArtisanProductUIEvent){
        when(event){
            is CreateArtisanProductUIEvent.ProductNameChanged -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productName = event.productName
                )
            }
            is CreateArtisanProductUIEvent.ProductDescriptionChanged -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productDescription = event.productDescription)
            }
            is CreateArtisanProductUIEvent.ProductPriceChanged -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productPrice =  event.productPrice
                )
            }

            is CreateArtisanProductUIEvent.ProductCategoryChanged -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productCategory = event.productCategory)
            }

            is CreateArtisanProductUIEvent.ProductImageChanged -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productImage = event.productImage
                )
            }
            is CreateArtisanProductUIEvent.ProductCreateRecordButtonClicked -> {
                createNewProductRecordInFirebase()

            }
            is CreateArtisanProductUIEvent.NextProductRecordItemButtonClicked -> {
                artisanProductRecordUIstate.value = artisanProductRecordUIstate.value.copy(
                    productName = "",
                    productDescription = "",
                    productPrice = 0.0,
                    productDiscountPrice = 0.0,
                    productCategory = "",
                    qtyOnHand = 0,
                    productImage = null
                )
                //TODO

                //hot to make all events to have entered
                //
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
            "artisanID" to uid,
            "product_name" to artisanProductRecordUIstate.value.productName,
            "product_description" to artisanProductRecordUIstate.value.productDescription,
            "product_price" to artisanProductRecordUIstate.value.productPrice,
            "product_discount_price" to artisanProductRecordUIstate.value.productDiscountPrice,
            "product_category" to artisanProductRecordUIstate.value.productCategory,
            //artisanProductRecordUIstate.productCategory,
            "qty_on_hand" to artisanProductRecordUIstate.value.qtyOnHand,
            "artisan_uid" to uid,
            "artisan_name" to artisanProductRecordUIstate.value.artisanName,
            "artisan_surname" to artisanProductRecordUIstate.value.artisanSurname,
            "product_picture" to productDownloadUri.toString()
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
    fun passDataToCreateProductRecord(){
// all elements from artisanProductRecordUIstate are passed from
        //artisanUIState
        artisanProductRecordUIstate.value.artisanName = artisanUIState.value.fname
        artisanProductRecordUIstate.value.artisanSurname = artisanUIState.value.lname
        artisanProductRecordUIstate.value.artisanUID = artisanUIState.value.userID
        artisanProductRecordUIstate.value.artisanProductCategories = artisanUIState.value.categories
        Log.d(TAG, "passDataToCreateProductRecord: " +
                "${artisanProductRecordUIstate.value.artisanName}  " +
                "${artisanProductRecordUIstate.value.artisanSurname}" +
                "  ${artisanProductRecordUIstate.value.artisanUID}  " +
                "${artisanProductRecordUIstate.value.artisanProductCategories}")
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

    var productDownloadUri: Uri? = null,
    var artisanUID: String = "",
    var artisanName: String = "",
    var artisanSurname: String = "",
)


