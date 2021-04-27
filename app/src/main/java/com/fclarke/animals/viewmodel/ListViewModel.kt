package com.fclarke.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fclarke.animals.di.modules.AppModule
import com.fclarke.animals.di.modules.CONTEXT_APP
import com.fclarke.animals.di.components.DaggerViewModelComponent
import com.fclarke.animals.di.modules.TypeOfContext
import com.fclarke.animals.model.Animal
import com.fclarke.animals.model.AnimalApiService
import com.fclarke.animals.model.ApiKey
import com.fclarke.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() } //populate only as needed
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable() //cleared onDestroy

    @Inject
    lateinit var apiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)//2 kinds of context defined
    lateinit var prefs: SharedPreferencesHelper

    private var noMoreRetry = false
    private var injected = false

    fun inject() {
        if (!injected) {  //not during test
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refresh() {
        inject()
        loading.value = true
        noMoreRetry = false
        val key = prefs.getApiKey()
        if (key.isNullOrEmpty()) {
            getKeyAndAnimals()
        } else {
            getAnimals(key)
        }
    }

    fun fullRefresh() {
        inject()
        loading.value = true
        getKeyAndAnimals()
    }

    private fun getKeyAndAnimals() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()) //for UI
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if (key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }

                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (noMoreRetry) { //only one retry allowed
                            e.printStackTrace()
                            loading.value = false
                            animals.value = null
                            loadError.value = true
                        } else {
                            noMoreRetry = true
                            getKeyAndAnimals()
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}