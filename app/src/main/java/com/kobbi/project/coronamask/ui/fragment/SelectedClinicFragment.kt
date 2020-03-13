package com.kobbi.project.coronamask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.FragmentClinicBinding
import com.kobbi.project.coronamask.ui.viewmodel.ClinicViewModel

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
            activity?.run {
                clinicVm = ViewModelProvider.AndroidViewModelFactory(application).create(ClinicViewModel::class.java)
            }
            lifecycleOwner = this@SelectedClinicFragment
        }
        return binding.root
    }
}
