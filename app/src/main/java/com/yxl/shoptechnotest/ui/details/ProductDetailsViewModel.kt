package com.yxl.shoptechnotest.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yxl.shoptechnotest.data.Repository
import com.yxl.shoptechnotest.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?>
        get() = _product

    fun getProductDetails(productId: Int) = viewModelScope.launch {
        try {
            val productFromApi = repository.getProductById(productId)
            _product.value = productFromApi
        }catch (e: Exception){
            _product.value = null
        }

    }
}