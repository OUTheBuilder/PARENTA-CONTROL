package com.parentguard

import android.app.Application
import com.google.firebase.FirebaseApp

class ParentGuardApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Reads config from app/google-services.json — do NOT hardcode keys in source.
        FirebaseApp.initializeApp(this)
    }
}
