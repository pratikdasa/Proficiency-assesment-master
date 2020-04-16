package com.proficiencyassesment.viewmodelfactoryprovider

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proficiencyassesment.view.MainActivity
import com.proficiencyassesment.viewmodel.MainViewModel

class ViewModelFactory(mainActivity: MainActivity) : ViewModelProvider.NewInstanceFactory() {
    private var mainActivity: MainActivity = mainActivity

    @NonNull
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mainActivity) as T
    }
}