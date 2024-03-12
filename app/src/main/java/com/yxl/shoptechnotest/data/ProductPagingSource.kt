package com.yxl.shoptechnotest.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yxl.shoptechnotest.data.api.Api
import com.yxl.shoptechnotest.data.model.Product
import java.io.IOException

class ProductPagingSource(
    private val api: Api
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val position = params.key ?: 0
            Log.d("key", params.key.toString())
            val limit = params.loadSize
            val response = api.getProducts(position * limit, limit)
            Log.d("products", response.body()?.products.toString())

            LoadResult.Page(
                data = response.body()!!.products,
                prevKey = if (position == 0) null else (position - 1),
                nextKey = if (response.body()!!.products.isEmpty()) null else (position + 1)
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: retrofit2.HttpException) {
            LoadResult.Error(e)
        }
    }
}
