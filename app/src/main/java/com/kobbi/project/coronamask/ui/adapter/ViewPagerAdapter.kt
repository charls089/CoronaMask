package com.kobbi.project.coronamask.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.kobbi.project.coronamask.ui.fragment.HospitalFragment
import com.kobbi.project.coronamask.ui.fragment.SelectedClinicFragment
import com.kobbi.project.coronamask.ui.fragment.StoreDetailFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, items: List<String>) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mItems = mutableListOf<String>()

    init {
        mItems.addAll(items)
    }

    override fun getItem(position: Int): Fragment {
        Log.e("####", "ViewPagerAdapter.getItem() --> position : $position")
        return when (position) {
            0 -> StoreDetailFragment.newInstance()
            1 -> SelectedClinicFragment.newInstance()
            else -> HospitalFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun setItems(items: List<String>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }
}