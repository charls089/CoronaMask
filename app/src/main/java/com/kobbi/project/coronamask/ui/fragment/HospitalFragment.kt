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
import com.kobbi.project.coronamask.databinding.FragmentHospitalBinding

class HospitalFragment : Fragment() {

    companion object {
        fun newInstance() = HospitalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHospitalBinding>(
            inflater, R.layout.fragment_hospital, container, false
        ).apply {
            hospitalVm = activity?.run {
                (application as? CoronaMaskApplication)?.let { app ->
                    app.hospitalViewModel.apply {
                        hospital.observe(this@run, Observer {
                            //TODO 애니메이션 넣기
                        })
                        app.searchViewModel.address.observe(this@run, Observer {
                            setHospitalFromAddress(it)
                        })
                    }
                }
            }
            lifecycleOwner = this@HospitalFragment
        }
        return binding.root
    }
}
