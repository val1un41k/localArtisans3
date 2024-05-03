package com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.local_artisan.screens.screens_all.artisan.UpdatingArtisanDetailsUIState
import com.example.localartisan3.components.productDownloadUri
import com.example.localartisan3.data.rules.Validator
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.CreateArtisanProductModel.CreateArtisanProductUIEvent
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.UpdateAartisansDetailsViewModel
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.UpdateArtisanDetailsUIEvent
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

    val db = FirebaseFirestore.getInstance()

       fun onArtisanLogin(){
        //TODO takes from Firestore the Artisan details and send to artisanUIState
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
                var profilePic = document.data?.get("profile_picture") as? String
                if (profilePic != null) {
                    artisanUIState.value.artisanProfilePicure = Uri.parse(profilePic)
                }
                //take array of categories from Firestore as a collection inside artisan document
            }
            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
           copyValuesToUpdateArtisanState()
        }.addOnCompleteListener {
           takeCategoriesFromArtisan()
            }.addOnFailureListener {
              Log.d(TAG, "Failed to get categories from Firestore")
            }
        }

    fun takeCategoriesFromArtisan(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)
            .collection("categories")

        docRef.get().addOnSuccessListener {
            querySnapshot ->
            if (querySnapshot != null){
                for (document in querySnapshot){
                   val category = document.toObject(ProductCategory::class.java)

                    artisanUIState.value.categories.add(category)
                }
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener{
            Log.d(TAG, "Error getting documents: ", it)
        }
    }


    fun addCategoryforArtisan(category: ProductCategory){
        artisanUIState.value.categories.add(category)
        artisanUIState.value.categories.toSet()
        Log.d("Updated categories", artisanUIState.value.categories.toString())
    }

    fun UpdateCategoriesStateInFirestore(context: Context) {
        val categories = artisanUIState.value.categories
        val set = categories.toSet()
        artisanUIState.value.categories = ArrayList(set)

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)

        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val categoriesCollectionRef = docRef.collection("categories")
                if (!categories.isEmpty()) {
                    for (category in categories) {
                        val categoryDocRef = categoriesCollectionRef.document(category.categoryID)
                        categoryDocRef.get().addOnSuccessListener { categoryDocument ->
                            if (!categoryDocument.exists()) {
                                // Document with this ID does not exist, so we can add it
                                categoryDocRef.set(category)
                                Log.d(TAG, "DocumentSnapshot data: ${categoryDocument.data}")
                                UpdateCategoryMessage.value = "Category added successfully"
                                Toast.makeText(context, "Category added successfully", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                // Document with this ID already exists, so we will place message related to it
                                Log.d(TAG, "DocumentSnapshot data: ${categoryDocument.data}")
                                UpdateCategoryMessage.value = "Category Already exists " +
                                        "\n please try anotherCategory"
                            }
                        }
                    }
                } else {
                    // Create new collection of categories
                    for (category in categories) {
                        val categoryDocRef = categoriesCollectionRef.document(category.categoryID)
                        categoryDocRef.set(category)
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    var homeScreenCreateCategoryMessage = mutableStateOf("")
    fun checkAvailableCategories(){

        if (artisanUIState.value.categories.isEmpty()){
            homeScreenCreateCategoryMessage.value = "No Categories available"
        }else{
            passDataToCreateProductRecord()
            LocalArtisansRouter.navigateTo(Screen.ArtisansCreateProductWithinCategory)
        }
    }



//////////////////////////////////////////////////////////////////////////
//////////////////////////////////
/////////////////////TODO Functions Related to ArtisanCreateProductWithinCategory
//////////////////////////////////
//////////////////////////////////////////////////////////////////////////

    var availableCategories = mutableStateOf(ArrayList<ProductCategory>())


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
                    productCategory = ProductCategory(),
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

            else -> {}
        }
    }

    fun populateAvailableCategories(){
        //take all data from Firestore collection of
        //categories and place it in availableCategories
        val db = db
        val docRef = db.collection("product_category")
        docRef.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null){
                for (document in querySnapshot){
                    val category = document.toObject(ProductCategory::class.java)
                    availableCategories.value.add(category)
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    Log.d(TAG, "Available Categories: ${availableCategories.value.toString()}")
                }
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener{
            Log.d(TAG, "Error getting documents: ", it)
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
                //TODO take array of Categories from Firestore as a collection inside artisan document


                Log.d(TAG, "CrProdArt DocumentSnapshot data: ${document.data}")
                Log.d(TAG, "${artisanProductRecordUIstate.value.artisanName}  ${artisanProductRecordUIstate.value.artisanSurname}" +
                        "  ${artisanProductRecordUIstate.value.artisanUID}  ${artisanProductRecordUIstate.value.artisanProductCategories}")
                isLoading.value = false
            }
        }
            .addOnSuccessListener {
                val docRef = db.collection("artisan").document(uid).collection("categories")
                docRef.get().addOnSuccessListener{
                    querySnapshot ->
                    if (querySnapshot != null){
                        for (document in querySnapshot){
                            val category = document.toObject(ProductCategory::class.java)
                            artisanProductRecordUIstate.value.artisanProductCategories.add(category)
                            Log.d(TAG, "CrProdArt DocumentSnapshot data: ${document.data}")
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }.addOnFailureListener{
                    Log.d(TAG, "Error getting documents: ", it)
                }

            }
    }

    fun createNewProductRecordInFirebase(){
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        var categoryID = artisanProductRecordUIstate.value.productCategory.categoryID

        val docRef = db.collection("artisan")
            .document(uid)
            .collection("categories")
            .document(categoryID)
            .collection("product_within_category")


        val newProductRecord = hashMapOf(
            "artisanID" to uid,
            "product_name" to artisanProductRecordUIstate.value.productName,
            "product_description" to artisanProductRecordUIstate.value.productDescription,
            "product_price" to artisanProductRecordUIstate.value.productPrice,
            "product_discount_price" to artisanProductRecordUIstate.value.productDiscountPrice,
            "product_category_ID" to artisanProductRecordUIstate.value.productCategory.categoryID,
            "product_category_name" to artisanProductRecordUIstate.value.productCategory.ProdCatName,
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
        artisanProductRecordUIstate.value.productCategory = ProductCategory()
        artisanProductRecordUIstate.value.qtyOnHand = 0
        artisanProductRecordUIstate.value.productImage = null
    }
    ////
    //////////////
    ///////////////////////////////////////
    //////////////Update Artisan Details
    ///////////////////////////////////////
    //////////////
    ////

    var updateArtisanUIState = mutableStateOf(UpdatingArtisanDetailsUIState())

    var allValidationsPassed = mutableStateOf(false)

    var artisanID = FirebaseAuth.getInstance().currentUser?.uid.toString()


    fun copyValuesToUpdateArtisanState(){

        updateArtisanUIState.value = updateArtisanUIState.value.copy(
            fname = artisanUIState.value.fname,
            lname = artisanUIState.value.lname,
            pnum = artisanUIState.value.pnum,
            address = artisanUIState.value.address,
            addressLongitude = artisanUIState.value.addressLongitude,
            addressLatitude = artisanUIState.value.addressLatitude,
            email = artisanUIState.value.email,
            artisanID = artisanUIState.value.artisanID
        )

    }




    fun onEvent (event: UpdateArtisanDetailsUIEvent){
        when(event){
            is UpdateArtisanDetailsUIEvent.ArtisansNameChanged -> {
                updateArtisanUIState.value = updateArtisanUIState.value.copy(fname = event.ArtisansName)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansSurnameChanged -> {
                updateArtisanUIState.value = updateArtisanUIState.value.copy(lname = event.ArtisansSurname)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtsisansPhoneNumberChanged -> {
                val phoneNumValidateMessage = validatePhoneNumber(event.ArtsisansPhoneNumber)
                updateArtisanUIState.value = updateArtisanUIState.value.copy(pnum = event.ArtsisansPhoneNumber,
                    phoneNumValidateMessage = phoneNumValidateMessage)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansStreetAddressChanged -> {
                updateArtisanUIState.value = updateArtisanUIState.value.copy(address = event.ArtisansAddress)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansAddressLongitudeChanged -> {
                updateArtisanUIState.value = updateArtisanUIState.value.copy(addressLongitude = event.ArtisansAddressLongitude)
                printState()
            }
            is UpdateArtisanDetailsUIEvent.ArtisansAddressLatitudeChanged -> {
                updateArtisanUIState.value = updateArtisanUIState.value.copy(addressLatitude = event.ArtisansAddressLatitude)
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
                LocalArtisansRouter.navigateTo(Screen.ArtisanHomeScreen)
            }

            else -> {}
        }
        validateUpdateArtisanDetailsUIDataWithRules()
    }

    private fun validateUpdateArtisanDetailsUIDataWithRules() {
        val phoneNumResult = Validator.validatePhoneNumber(phoneNumber = updateArtisanUIState.value.pnum)

        updateArtisanUIState.value = updateArtisanUIState.value.copy(phoneNumValid = phoneNumResult.status)

        allValidationsPassed.value = phoneNumResult.status
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, updateArtisanUIState.value.toString())
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
                    updateArtisanUIState.value.fname = document.data?.get("fname").toString()
                    updateArtisanUIState.value.lname = document.data?.get("lname").toString()
                    updateArtisanUIState.value.pnum = document.data?.get("pnum").toString()
                    updateArtisanUIState.value.address = document.data?.get("address").toString()
                    updateArtisanUIState.value.addressLongitude = document.data?.get("addressLongitude").toString()
                    updateArtisanUIState.value.addressLatitude = document.data?.get("addressLatitude").toString()
                    updateArtisanUIState.value.email = document.data?.get("email").toString()
                    updateArtisanUIState.value.artisanID = uid

                }
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")

            }
    }

    fun updateArtisanDetailsInFirestore() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val docRef = db.collection("artisan").document(uid)

        docRef.update(
            "fname", updateArtisanUIState.value.fname,
            "lname", updateArtisanUIState.value.lname,
            "pnum", updateArtisanUIState.value.pnum,
            "address", updateArtisanUIState.value.address,
            "addressLongitude", updateArtisanUIState.value.addressLongitude,
            "addressLatitude", updateArtisanUIState.value.addressLatitude,
            "firstTime", false,

            ).addOnSuccessListener {
            Log.d(TAG, "Arisan Updated Details successfully updated!")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error to Update Artisan document", e)
        }
    }


}
data class ArtisanProductRecordCreateUIstate(
    var productName: String = "",
    var productDescription: String = "",
    var productPrice: Double = 0.0,
    var productDiscountPrice: Double = 0.0,
    var artisanProductCategories: ArrayList<ProductCategory> = ArrayList(),
    var productCategory: ProductCategory = ProductCategory(),

    var qtyOnHand: Int = 0,
    var productImage: Uri? = null,

    var productDownloadUri: Uri? = null,
    var artisanUID: String = "",
    var artisanName: String = "",
    var artisanSurname: String = "",
)

data class ProductCategory(
    var categoryID: String = "",
    var ProdCatName: String = ""
)
