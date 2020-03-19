package com.kobbi.project.coronamask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kobbi.project.coronamask.CoronaMaskApplication
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.FragmentDetailBinding

class StoreDetailFragment : Fragment() {
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
            detailVm = activity?.run {
                (application as? CoronaMaskApplication)?.let { app ->
                    app.storeDetailViewModel.apply {
                        stores.observe(this@run, Observer {
                            //TODO 애니메이션 넣기(in/out)
                        })
                        app.searchViewModel.address.observe(this@run, Observer {
                            setStoreInfoFromAddress(it)
                        })
                    }
                }
            }
            lifecycleOwner = this@StoreDetailFragment
        }
        return binding.root
    }
}
