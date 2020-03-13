package com.kobbi.project.coronamask.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.FragmentDetailBinding
import com.kobbi.project.coronamask.ui.viewmodel.StoreDetailViewModel

class StoreDetailFragment : Fragment() {
    private val detailViewModel =
        ViewModelProvider.NewInstanceFactory().create(StoreDetailViewModel::class.java)

    companion object {
        fun newInstance() = StoreDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater, R.layout.fragment_detail, container, false
        ).apply {
            context?.applicationContext?.let {
                detailViewModel.setStoreDetailInfo(it)
            }
            detailVm = detailViewModel
            lifecycleOwner = this@StoreDetailFragment
        }
        return binding.root
    }
}
