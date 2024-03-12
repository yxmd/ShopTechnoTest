package com.yxl.shoptechnotest.data.api

import com.yxl.shoptechnotest.data.model.Product
import com.yxl.shoptechnotest.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
    ): Response<Product?>

    @GET("products/search")
    suspend fun searchProduct(
        @Query("q") q: String
    ): Response<ProductResponse?>

    companion object{
        const val BASE_URL = "https://dummyjson.com/"
    }
}
