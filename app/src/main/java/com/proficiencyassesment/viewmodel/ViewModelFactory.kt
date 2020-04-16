package com.proficiencyassesment.viewmodel

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proficiencyassesment.view.MainActivity
import com.proficiencyassesment.viewmodel.MainViewModel

class ViewModelFactory(mainActivity: MainActivity, apiKey: String) : ViewModelProvider.Factory{
    private var mainActivity: MainActivity = mainActivity
    private var apiKey: String =apiKey

    @NonNull
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mainActivity, apiKey) as T
    }
}