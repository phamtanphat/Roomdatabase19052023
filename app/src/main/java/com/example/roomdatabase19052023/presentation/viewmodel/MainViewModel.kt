package com.example.roomdatabase19052023.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roomdatabase19052023.common.AppResource
import com.example.roomdatabase19052023.data.database.ProductEntity
import com.example.roomdatabase19052023.data.repository.ProductRepository
import com.example.roomdatabase19052023.extension.launchOnMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository
): ViewModel() {
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + coroutineJob)

    private val liveDataListProducts = MutableLiveData<AppResource<List<ProductEntity>>>()

    fun listProductsLiveData(): LiveData<AppResource<List<ProductEntity>>> = liveDataListProducts

    fun getListProducts() {
        liveDataListProducts.value = AppResource.Loading()
        coroutineScope.launch {
            try {
                val deferred = async { productRepository.getListProducts() }
                val listProducts = deferred.await()
                launchOnMain { liveDataListProducts.value = AppResource.Success(listProducts) }
            } catch (e: Exception) {
                launchOnMain { liveDataListProducts.value = AppResource.Error(e.message ?: "") }
            }
        }
    }
}