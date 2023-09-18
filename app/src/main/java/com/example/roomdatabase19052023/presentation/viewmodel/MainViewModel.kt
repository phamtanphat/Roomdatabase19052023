package com.example.roomdatabase19052023.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabase19052023.common.AppResource
import com.example.roomdatabase19052023.data.database.ProductEntity
import com.example.roomdatabase19052023.data.repository.ProductRepository
import com.example.roomdatabase19052023.extension.launchIO
import com.example.roomdatabase19052023.extension.launchOnMain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val liveDataListProducts = MutableLiveData<AppResource<List<ProductEntity>>>()
    private val liveDataInsert = MutableLiveData<AppResource<Any?>>()
    private val liveDataLoading = MutableLiveData<Boolean>()

    fun listProductsLiveData(): LiveData<AppResource<List<ProductEntity>>> = liveDataListProducts
    fun insertLiveData(): LiveData<AppResource<Any?>> = liveDataInsert
    fun loadingLiveData(): LiveData<Boolean> = liveDataLoading

    fun getListProducts() {
        liveDataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deferred = async { productRepository.getListProducts() }
                val listProducts = deferred.await()
                launchOnMain { liveDataListProducts.value = AppResource.Success(listProducts) }
            } catch (e: Exception) {
                launchOnMain { liveDataListProducts.value = AppResource.Error(e.message ?: "") }
            } finally {
                launchOnMain { liveDataLoading.value = false }
            }
        }
    }

    fun insertProduct(productEntity: ProductEntity) {
        liveDataLoading.value = false
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            launch {
                try {
                    productRepository.insertProduct(productEntity)
                    launchOnMain { liveDataInsert.value = AppResource.Success(null) }
                } catch (e: Exception) {
                    launchOnMain { liveDataInsert.value = AppResource.Error(e.message ?: "") }
                } finally {
                    launchOnMain { liveDataLoading.value = true }
                }
            }
        }
    }
}