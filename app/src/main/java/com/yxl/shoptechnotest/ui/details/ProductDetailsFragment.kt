package com.yxl.shoptechnotest.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.size.Scale
import com.yxl.shoptechnotest.R
import com.yxl.shoptechnotest.databinding.FragmentProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel by viewModels<ProductDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getProductDetails()
        binding.bRetry.setOnClickListener {
            getProductDetails()
        }
        viewModel.product.observe(viewLifecycleOwner){product ->
            with(binding){
                if (product != null) {
                    binding.bRetry.visibility = View.GONE
                    tvTitle.text = product.title
                    ivProductImage.load(product.thumbnail){
                        crossfade(true)
                        placeholder(R.drawable.ic_image)
                        scale(Scale.FIT)
                    }
                    tvDescription.text = product.description
                    ratingBar.rating = product.rating
                    tvPrice.text = "Price: ${product.price}"
                    tvDiscountPercentage.text = "Discount ${product.discountPercentage}%"
                    tvStock.text = "Now available: ${product.stock}"
                    tvBrand.text = "Brand: ${product.brand}"
                    tvCategory.text = "Category: ${product.category}"
                }
                else{
                    binding.bRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getProductDetails() {
        arguments?.getInt(PRODUCT)?.let { viewModel.getProductDetails(it) }
    }

    companion object{

        private const val PRODUCT = "product"

        fun newInstance(productId: Int): ProductDetailsFragment{
            val args = bundleOf(PRODUCT to productId)
            return ProductDetailsFragment().apply {
                arguments = args
            }
        }
    }
}