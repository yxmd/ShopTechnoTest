package com.yxl.shoptechnotest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yxl.shoptechnotest.R
import com.yxl.shoptechnotest.databinding.FragmentProductsBinding
import com.yxl.shoptechnotest.ui.main.adapter.ProductPagingAdapter
import com.yxl.shoptechnotest.ui.main.adapter.SearchAdapter
import com.yxl.shoptechnotest.ui.details.ProductDetailsFragment
import com.yxl.shoptechnotest.ui.main.adapter.LoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductPagingAdapter.OnItemClickListener {

    private lateinit var binding: FragmentProductsBinding
    private val viewModel by viewModels<ProductsViewModel>()
    private lateinit var productAdapter: ProductPagingAdapter
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerProducts()

        binding.svSearchProduct.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let { viewModel.searchProduct(it) }
                setUpSearchRecycler()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        binding.ivReset.setOnClickListener {
            setUpRecyclerProducts()
            productAdapter.retry()
        }
    }

    private fun setUpRecyclerProducts() {
        productAdapter = ProductPagingAdapter(this)

        productAdapter.addLoadStateListener { loadState ->
            binding.spinner.isVisible = loadState.source.refresh is LoadState.Loading
            binding.rvProducts.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.tvError.isVisible = loadState.source.refresh is LoadState.Error
        }

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { productAdapter.retry() },
                footer = LoadStateAdapter { productAdapter.retry() }
            )
        }

        viewModel.productList.observe(viewLifecycleOwner) {
            productAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

    }

    private fun setUpSearchRecycler() {
        searchAdapter = SearchAdapter()
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
        viewModel.searchList.observe(viewLifecycleOwner) {
            if (it != null) {
                searchAdapter.differ.submitList(it)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Something goes wrong. Try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onItemClick(productId: Int) {

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, ProductDetailsFragment.newInstance(productId))
            .addToBackStack("product_details")
            .commit()
    }

}