package com.kobbi.project.coronamask.ui.adapter

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.database.entity.SafetyHospital
import com.kobbi.project.coronamask.database.entity.SelectedClinic
import com.kobbi.project.coronamask.model.RemainState
import com.kobbi.project.coronamask.model.Store
import com.kobbi.project.coronamask.ui.viewmodel.StoreDetailViewModel
import com.kobbi.project.coronamask.util.Utils
import java.util.*

object BindingAdapter {
    @BindingAdapter("setDetails")
    @JvmStatic
    fun setDetails(recyclerView: RecyclerView, items: List<Store>?) {
        Log.e("####", "setDetails() --> items : ${!items.isNullOrEmpty()}")
        if (!items.isNullOrEmpty())
            recyclerView.adapter?.run {
                if (this is StoreAdapter) {
                    this.setItems(items)
                }
            } ?: kotlin.run {
                recyclerView.adapter = StoreAdapter(items).apply {
                    setHasStableIds(true)
                }
            }
    }

    @BindingAdapter("setClinic")
    @JvmStatic
    fun setClinic(recyclerView: RecyclerView, items: List<SelectedClinic>?) {
        Log.e("####", "setClinic() --> items : ${!items.isNullOrEmpty()}")
        if (!items.isNullOrEmpty())
            recyclerView.adapter?.run {
                if (this is ClinicAdapter)
                    this.setItems(items)
            } ?: kotlin.run {
                recyclerView.adapter = ClinicAdapter(items).apply {
                    setHasStableIds(true)
                }
            }
    }

    @BindingAdapter("setHospital")
    @JvmStatic
    fun setHospital(recyclerView: RecyclerView, items: List<SafetyHospital>?) {
        Log.e("####", "setHospital() --> items : ${!items.isNullOrEmpty()}")
        if (!items.isNullOrEmpty())
            recyclerView.adapter?.run {
                if (this is HospitalAdapter)
                    this.setItems(items)
            } ?: kotlin.run {
                recyclerView.adapter = HospitalAdapter(items).apply {
                    setHasStableIds(true)
                }
            }
    }

    @BindingAdapter("setTapContents")
    @JvmStatic
    fun setTapContents(tabLayout: TabLayout, items: List<String>?) {
        Log.e("####", "setTapContents() --> items: $items")
        items?.forEach {
            tabLayout.newTab().run {
                text = it
                tabLayout.addTab(this)
            }
        }
    }

    @BindingAdapter("setPagerCount", "setFsm")
    @JvmStatic
    fun setViewPager(
        viewPager: ViewPager,
        items: List<String>?,
        fragmentManager: FragmentManager?
    ) {
        if (!items.isNullOrEmpty())
            viewPager.adapter?.run {
                if (this is ViewPagerAdapter) {
                    Log.e("####", "this is ViewPagerAdapter")
                    setItems(items)
                }
            } ?: kotlin.run {
                Log.e("####", "this is null")
                if (fragmentManager != null)
                    viewPager.adapter = ViewPagerAdapter(fragmentManager, items)
            }
    }

    @BindingAdapter("setUpdateTime")
    @JvmStatic
    fun setUpdateTime(textView: TextView, date: Date?) {
        date?.run {
            textView.text =
                String.format("%d분 전 업데이트", (System.currentTimeMillis() - time) / (60 * 1000))
        }
    }

    @BindingAdapter("setStockAt")
    @JvmStatic
    fun setStockAt(textView: TextView, data: Date?) {
        data?.run {
            textView.text = String.format("입고 시간 : %s", Utils.getCurrentTime(Utils.VALUE_DATETIME_FORMAT, data.time))
        }
    }

    @BindingAdapter("setRemainAt")
    @JvmStatic
    fun setRemainAt(textView: TextView, data: RemainState?) {
        val remainValue = when (data) {
            RemainState.BREAK -> {
                Pair("판매중지", R.drawable.border_empty)
            }
            RemainState.EMPTY -> {
                Pair("없음", R.drawable.border_empty)
            }
            RemainState.FEW -> {
                Pair("30개 미만", R.drawable.border_few)
            }
            RemainState.SOME -> {
                Pair("100개 미만", R.drawable.border_some)
            }
            RemainState.PLENTY -> {
                Pair("100개 이상", R.drawable.border_plenty)
            }
            else -> {
                Pair("알 수 없음", R.drawable.border_empty)
            }
        }
        remainValue.run {
            with(textView) {
                text = remainValue.first
                setBackgroundResource(remainValue.second)
            }
        }
    }

    @BindingAdapter("setListener")
    @JvmStatic
    fun setListener(spinner: Spinner, detailVm: StoreDetailViewModel?) {
        Log.e("####", "BindingAdapter.setListener() --> detailVm : $detailVm")
        detailVm?.run {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position)
                    if (item is String)
                        detailVm.sortStoreList(item)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }
}