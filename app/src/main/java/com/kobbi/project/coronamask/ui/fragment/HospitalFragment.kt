package com.kobbi.project.coronamask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.FragmentHospitalBinding
import com.kobbi.project.coronamask.ui.viewmodel.HospitalViewModel

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
            activity?.run {
                hospitalVm = ViewModelProvider.AndroidViewModelFactory(application).create(HospitalViewModel::class.java)
            }
            lifecycleOwner = this@HospitalFragment
        }
        return binding.root
    }
}
