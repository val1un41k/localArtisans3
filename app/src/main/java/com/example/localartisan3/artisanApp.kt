package com.example.localartisan3

import android.app.Application
import com.google.firebase.FirebaseApp

class artisanApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

    }
}