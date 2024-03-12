package com.yxl.shoptechnotest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.yxl.shoptechnotest.data.Repository
import com.yxl.shoptechnotest.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val productList: LiveData<PagingData<Product>> =
        repository.getProducts().map {
            val productMap = mutableSetOf<Int>()
            it.filter { person ->
                if (productMap.contains(person.id)) {
                    false
                } else {
                    productMap.add(person.id)
                }
            }
        }
            .cachedIn(viewModelScope)

    private val _searchList = MutableLiveData<List<Product>?>()

    val searchList: LiveData<List<Product>?>
        get() = _searchList

    fun searchProduct(q: String) = viewModelScope.launch {
        try {
            val list = repository.searchProduct(q)
            _searchList.value = list?.products
        } catch (e: Exception){
            _searchList.value = null
        }

    }
}