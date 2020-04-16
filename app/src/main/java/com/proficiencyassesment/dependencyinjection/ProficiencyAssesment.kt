package com.proficiencyassesment.dependencyinjection

import android.app.Application
import com.proficiencyassesment.di.ApiClient
import com.proficiencyassesment.di.AppComponent
import com.proficiencyassesment.di.DaggerAppComponent

class ProficiencyAssesment : Application() {
    var mAppComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        mAppComponent= DaggerAppComponent.builder()
            .apiClient(ApiClient())
            .build()
    }

    fun getAppComponent(): AppComponent? {
        return mAppComponent
    }

}