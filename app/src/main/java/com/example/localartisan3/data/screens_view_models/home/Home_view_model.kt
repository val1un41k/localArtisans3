package com.example.localartisan3.data.screens_view_models.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.localartisan3.data.NavigationItem
import com.example.localartisan3.data.screens_view_models.login.LoginUIState
import com.example.localartisan3.navigation.LocalArtisansRouter
import com.example.localartisan3.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    val navigationItemList = listOf<NavigationItem>(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            description = "Home Screen",
            itemId = "homeScreen"
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            description = "Settings Screen",
            itemId = "settingsScreen"
        ),
        NavigationItem(
            title = "Favorite",
            icon = Icons.Default.Favorite,
            description = "Favorite Screen",
            itemId = "favoriteScreen"
        )
    )

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun logout() {

        val firebathAuth = FirebaseAuth.getInstance()

        firebathAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null){
                Log.d(TAG, "Inside sign outsucess")
                //go to home screen
                LocalArtisansRouter.navigateTo(Screen.HomeScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebathAuth.addAuthStateListener(authStateListener)
    }

    fun checkForActiveSession(){
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "Invalid session")
            isUserLoggedIn.value = false
        }
    }

    val emailId: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {

        FirebaseAuth.getInstance().currentUser?.also{
            it.email?.let { email ->
                emailId.value = email
            }
        }
    }



    fun changeCustomerRoleToCusotmer(){
        loginUIState.value = loginUIState.
        value.copy(userRole = "Customer")
    }
    fun changeCustomerRoleToArtisan(){
        loginUIState.value = loginUIState.
        value.copy(userRole = "Artisan")
    }
}