package com.example.localartisan3.data.screens_view_models.cusotmerScreensViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.local_artisan.screens.screens_all.customer.ArtisanToPutOnMarkers
import com.example.local_artisan.screens.screens_all.customer.CategoriesWithProducts

import com.example.local_artisan.screens.screens_all.customer.CustomerSelectedProductItem
import com.example.local_artisan.screens.screens_all.customer.LoadedProductFromDB
import com.example.local_artisan.screens.screens_all.customer.selectingPoruductCateogyDialogWindow

import com.example.localartisan3.data.rules.Validator
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ArtisanProductRecordCreateUIstate
import com.example.localartisan3.data.screens_view_models.artisanScreens.UpdateArtisanDetailsModel.ArtisansHomeScreen.ProductCategory
import com.example.localartisan3.data.screens_view_models.login.LoginUIEvent
import com.example.localartisan3.data.screens_view_models.login.LoginUIState

import com.example.localartisan3.data.screens_view_models.users_data.ArtisanUIState

import com.example.localartisan3.data.screens_view_models.users_data.Customer
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerMainViewModel : ViewModel() {

    private val TAG = CustomerMainViewModel::class.simpleName

    var db = FirebaseFirestore.getInstance()

    var loginUIState = mutableStateOf(LoginUIState())

    val customerUiState = mutableStateOf(Customer())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)

    var LoginOutMessage = mutableStateOf("")

    var LoginErrorMessage = mutableStateOf("")

    var emailValidationMessage = mutableStateOf("")

    var passwordValidationMessage = mutableStateOf("")

    var artisanList = mutableStateOf(ArrayList<ArtisanToPutOnMarkers>())

    val selectedArtisan = mutableStateOf(ArtisanUIState())

    val selectedProduct = mutableStateOf(ArtisanProductRecordCreateUIstate())


    fun onEvent(event: LoginUIEvent) {

        when (event) {
            is LoginUIEvent.EmailChanged -> {
                emailValidationMessage.value = validateEmail(event.email)
                loginUIState.value = loginUIState.value.copy(
                    email = event.email,
                    emailValidationError = emailValidationMessage.value
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                passwordValidationMessage.value = validatePassword(event.password)
                loginUIState.value = loginUIState.value.copy(
                    password = event.password,
                    passwordValidationError = passwordValidationMessage.value
                )
            }

            is LoginUIEvent.ConfirmPasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
                GlobalScope.launch {
                    toDelay(time = 3000)
                    withContext(Dispatchers.Main) {

                    }
                }

            }

            is LoginUIEvent.ArtisanLoginButtonClicked -> {
                "   artisanLogin()"
            }

            is LoginUIEvent.UserRoleChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    userRole = "Customer"
                )
            }

            is LoginUIEvent.CustomerExists -> {
                loginUIState.value = loginUIState.value.copy(
                    customerExists = event.customerExists
                )
            }

            is LoginUIEvent.ResetPasswrodButtonClicked -> {
                sendPasswordResetEmail(loginUIState.value.email)
            }

            else -> {}
        }
        validateLoginUIDataWithRules()

    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )
        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,

            passwordError = passwordResult.status
        )
        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    fun login() {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful) {
                    loginInProgress.value = false

                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                LoginErrorMessage.value = "Invalied Email or Password"
                loginInProgress.value = false
            }.addOnCompleteListener { checkCustomerLoginInFirestore() }

    }

    fun validatePassword(password: String): String {
        val passwordValidationMessage = StringBuilder()

        if (password.length < 8) {
            passwordValidationMessage.append("Password must be at least 8 characters long\n")
        }

        if (!password.contains(Regex(".*[0-9].*"))) {
            passwordValidationMessage.append("Password must contain at least one number\n")
        }

        if (!password.contains(Regex(".*[a-z].*"))) {
            passwordValidationMessage.append("Password must contain at least one lowercase letter\n")
        }

        if (!password.contains(Regex(".*[A-Z].*"))) {
            passwordValidationMessage.append("Password must contain at least one uppercase letter\n")
        }

        if (!password.contains(Regex(".*[!@#\$%^&*].*"))) {
            passwordValidationMessage.append("Password must contain at least one special character\n")
        }

        return passwordValidationMessage.toString()

    }


    fun validateEmail(email: String): String {
        val emailValidationMessage = StringBuilder()


        if (!email.contains(Regex(".*[@].*"))) {
            emailValidationMessage.append("Email must contain @\n")
        }

        if (!email.contains(".com")
            || !email.contains(".ie")
            || !email.contains(".org")
            || !email.contains(".net")
            || !email.contains(".gov")
            || !email.contains(".edu")
        ) {
            emailValidationMessage.append("Email must contain proper ending")
        }

        if (!email.contains("gmail")
            || !email.contains("yahoo")
            || !email.contains("hotmail")
            || !email.contains("outlook")
            || !email.contains("icloud")
            || !email.contains("live")
        ) {
            emailValidationMessage.append(
                "Email must contain proper domain \n" +
                        "like google yahoo hotmail outlook icloud live.\n"
            )
        }
        return emailValidationMessage.toString()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun checkCustomerLoginInFirestore() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "UID = $uid")
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()

            db.collection("customer").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        //change value in customerExists to True
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = true
                        )
                        //populate the Customer with data taken from Firebase

                        customerUiState.value.email = document.getString("email").toString()
                        customerUiState.value.fname = document.getString("firstName").toString()
                        customerUiState.value.lname = document.getString("lastName").toString()
                        customerUiState.value.pnum = document.getString("phoneNumber").toString()

                        Log.d(TAG, "Customer Data: ${customerUiState.value}")
                        //populate list of Artisans

                        GlobalScope.launch {  populateArtisans(db)}


                        Log.d(TAG, "Artisan List: ${artisanList.value}")

                        //navigate to Customer Home Screen
                        //        LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen)

                        LoginErrorMessage.value = "Customer Exists"

                        LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen)


                    } else {
                        Log.d(TAG, "No such Customer Found")
                        //change value in customerExists to False
                        loginUIState.value = loginUIState.value.copy(
                            customerExists = false
                        )
                        LoginErrorMessage.value = "Customer does not exist"
                    }

                }.addOnCompleteListener {
                    if (loginUIState.value.customerExists) {

                    }
                }
        }

    }


    fun sendPasswordResetEmail(email: String): String {
        val auth = FirebaseAuth.getInstance()
        var message = ""

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    message = "Email sent."
                } else {
                    Log.d(TAG, "Account not exists.")
                    message = "Account not exists."

                }
            }
        return message
    }

    suspend fun populateArtisans(db: FirebaseFirestore) = withContext(Dispatchers.IO) {
        val artisanResult = db.collection("artisan").get().await()
        for (artisanDocument in artisanResult) {
            val artisan = artisanDocument.toObject(ArtisanToPutOnMarkers::class.java)

            // Fetch categories of each artisan
            val categoryResult = db.collection("artisan")
                .document(artisanDocument.id)
                .collection("categories")
                .get()
                .await()
            for (categoryDocument in categoryResult) {
                val category = categoryDocument.toObject(CategoriesWithProducts::class.java)

                // Fetch products within each category
                val productResult = db.collection("artisan")
                    .document(artisanDocument.id)
                    .collection("categories")
                    .document(categoryDocument.id)
                    .collection("product_within_category")
                    .get()
                    .await()
                for (productDocument in productResult) {
                    val product = productDocument.toObject(LoadedProductFromDB::class.java)
                    // Add the product to the products list of the category
                    category.products.add(product)
                }

                // Add the category to the categories list of the artisan
                artisan.categories.add(category)
            }

            artisanList.value.add(artisan)
        }
        artisanList.value.removeIf {
            it.categories.isNullOrEmpty()
        }
    }

//    fun populateArtisans(db: FirebaseFirestore) {
//        db.collection("artisan")
//            .get()
//            .addOnSuccessListener { artisanResult ->
//                for (artisanDocument in artisanResult) {
//                    val artisan = artisanDocument.toObject(ArtisanUIState::class.java)
//
//                    Log.d(TAG, "Artisan: $artisan")
//
//                    // Fetch categories of each artisan
//                    db.collection("artisan")
//                        .document(artisanDocument.id)
//                        .collection("categories")
//                        .get()
//                        .addOnSuccessListener { categoryResult ->
//                            for (categoryDocument in categoryResult) {
//                                val category =
//                                    categoryDocument.toObject(ProductCategory::class.java)
//                                artisan.categories.add(category)
//
//                                Log.d(TAG, "Artisan ${artisanDocument.id}   Category: ${category}")
//
//                                // Fetch products within each category
//                                db.collection("artisan")
//                                    .document(artisanDocument.id)
//                                    .collection("categories")
//                                    .document(categoryDocument.id)
//                                    .collection("product_within_category")
//                                    .get()
//                                    .addOnSuccessListener { productResult ->
//                                        for (productDocument in productResult) {
//                                            val product =
//                                                productDocument.toObject(LoadedProductFromDB::class.java)
//                                            // Add the product to the products list of the artisan
//                                            Log.d(
//                                                TAG, "Artisan ${artisanDocument.id} " +
//                                                        "Category ${categoryDocument.id}" +
//                                                        "  Product: ${product}"
//                                            )
//                                            artisan.products_within_categories.add(product)
//                                        }
//                                    }
//                            }
//                        }
//
//                    artisanList.value.add(artisan)
//                }
//                artisanList.value.forEach { artisan ->
//                    Log.d("Taking Artisans from Database", "Artisan List contains of  : ${artisan} \n")
//                }
//                artisanList.value.removeIf {
//                    it.categories.isNullOrEmpty()
//                }
//
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
//            .addOnCompleteListener { LocalArtisansRouter.navigateTo(Screen.CustomerHomeDashboardScreen) }
//    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    ////
    //////Code related to the Google Maps Artisans Location
    //
    ////
    //////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    ////
    //////Code related to the Creating Request
    //
    ////
    //////////////////////////////////////////////////////////////////////////////////////////

    val limerick = LatLng(52.66, -8.63)

    val homeLocation = LatLng(52.658664940642296, -8.633123277485389)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(limerick, 12f)

    var productsToShow = mutableStateOf(ArrayList<LoadedProductFromDB>())

    var showSelectCategoryDialog by mutableStateOf(false)

    var showSelectProductWithinCategoryDialog by mutableStateOf(false)


    var customerSelectedProductItem = mutableStateOf(CustomerSelectedProductItem())


    fun onEvent(event: CusotmerSelectedProductItemEvent) {
        when (event) {
            is CusotmerSelectedProductItemEvent.SelectedProductArtisanIDChanged -> {
                customerSelectedProductItem.value = customerSelectedProductItem.value.copy(
                    artisan = event.artisan
                )
            }

            is CusotmerSelectedProductItemEvent.SelectedProductArtsanProductCategoryChanged -> {
                customerSelectedProductItem.value = customerSelectedProductItem.value.copy(
                    artisanProductCategory = event.artisanProductCategory
                )

                if (customerSelectedProductItem.value.artisanProductCategory != null) {
                    //TODO load products from DB

                }
            }

            is CusotmerSelectedProductItemEvent.SelectedProductChanged -> {
                customerSelectedProductItem.value = customerSelectedProductItem.value.copy(
                    selectedProduct = event.selectedProduct
                )
            }

            is CusotmerSelectedProductItemEvent.SelectedProductQuantityChanged -> {
                customerSelectedProductItem.value = customerSelectedProductItem.value.copy(
                    quantityRequested = event.quantityRequested
                )
            }

            is CusotmerSelectedProductItemEvent.SelectedProductRequestedTotalChanged -> {
                customerSelectedProductItem.value = customerSelectedProductItem.value.copy(
                    requestedTotal = event.requestedTotal
                )
            }

            else -> {}
        }
    }

    fun loadProductsFromDB() {


        db.collection("artisan").document(customerSelectedProductItem.value
            .artisan.artisanID)
            .collection("product_category")
            .document(customerSelectedProductItem.value.artisanProductCategory.categoryID)
            .collection("product_within_category")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val product = document.toObject(LoadedProductFromDB::class.java)
                    product.productID = document.id
                    productsToShow.value.add(product)
                    productsToShow.value.removeIf { it.qty_on_hand == 0 || it.qty_on_hand == null }
                }
                Log.d(TAG, "Product List: $productsToShow")
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }






//                for (document in result) {
//                    val product = document.toObject(LoadedProductFromDB::class.java)
//                    productsToShow.value.add(product)
//                    productsToShow.value.removeIf { it.qty_on_hand == 0 || it.qty_on_hand == null }
//                }
//                Log.d(TAG, "Product List: $productsToShow")
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
    }
}




suspend fun toDelay(time: Long) {
    delay(time)
}