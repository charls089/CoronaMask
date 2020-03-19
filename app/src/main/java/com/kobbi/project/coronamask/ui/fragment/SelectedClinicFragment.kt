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
import com.kobbi.project.coronamask.databinding.FragmentClinicBinding

class SelectedClinicFragment : Fragment() {

    companion object {
        fun newInstance() = SelectedClinicFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentClinicBinding>(
            inflater, R.layout.fragment_clinic, container, false
        ).apply {
            clinicVm = activity?.run {
                (application as? CoronaMaskApplication)?.let { app ->
                    app.clinicViewModel.apply {
                        clinic.observe(this@run, Observer {
                            //TODO 애니메이션 넣기
                        })
                        app.searchViewModel.address.observe(this@run, Observer {
                            setClinicFromAddress(it)
                        })
                    }
                }
            }
            lifecycleOwner = this@SelectedClinicFragment
        }
        return binding.root
    }
}
