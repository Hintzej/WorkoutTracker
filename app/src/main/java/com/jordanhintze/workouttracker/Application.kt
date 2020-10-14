package com.jordanhintze.workouttracker

import android.app.Application
import android.util.Log

private const val TAG = "WorkoutApplication"

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Repository Initialized")
        Repositories.initialize(this)
    }
}