package com.fclarke.animals

import android.app.Application
import com.fclarke.animals.di.modules.PrefsModule
import com.fclarke.animals.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}