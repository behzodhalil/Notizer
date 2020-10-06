@file:Suppress("UNCHECKED_CAST")

package com.hfad.notizer.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotizerViewModelFactory(private val application: Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotizerViewModel::class.java)){
            return NotizerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}