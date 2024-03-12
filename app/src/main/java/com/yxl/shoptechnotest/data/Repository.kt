package com.yxl.shoptechnotest.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.yxl.shoptechnotest.data.api.Api
import com.yxl.shoptechnotest.data.model.Product
import com.yxl.shoptechnotest.data.model.ProductResponse
import javax.inject.Inject

class Repository @Inject constructor(private val api: Api) {

    fun getProducts(): LiveData<PagingData<Product>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ProductPagingSource(api) }
    ).liveData

    suspend fun getProductById(id: Int): Product? {
        val response = api.getProductById(id)
        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }
    }

    suspend fun searchProduct(q: String): ProductResponse? {
        val response = api.searchProduct(q)
        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }
    }
}