package com.kobbi.project.coronamask.ui.adapter

import android.content.Context
import android.location.Address
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kobbi.project.coronamask.ClickListener
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.database.entity.ClinicBase
import com.kobbi.project.coronamask.model.Clinic
import com.kobbi.project.coronamask.model.Hospital
import com.kobbi.project.coronamask.model.RemainState
import com.kobbi.project.coronamask.model.Store
import com.kobbi.project.coronamask.ui.viewmodel.*
import com.kobbi.project.coronamask.util.DLog
import com.kobbi.project.coronamask.util.Utils
import java.util.*

object BindingAdapter {
    @BindingAdapter("setDetails", "setVm")
    @JvmStatic
    fun setDetails(
        recyclerView: RecyclerView,
        items: List<Store>?,
        detailVm: StoreDetailViewModel?
    ) {
        if (!items.isNullOrEmpty())
            recyclerView.adapter?.run {
                if (this is StoreAdapter) {
                    this.setItems(items)
                }
            } ?: kotlin.run {
                recyclerView.adapter = StoreAdapter(items).apply {
                    setHasStableIds(true)
                    clickListener = object : ClickListener {
                        override fun onItemClick(position: Int, view: View) {
                            detailVm?.selectItem(position)
                        }
                    }
                }
            }
    }

    @BindingAdapter("setClinic")
    @JvmStatic
    fun setClinic(recyclerView: RecyclerView, items: List<Clinic>?) {
        DLog.i(tag = "BindingAdapter", message = "setClinic() --> items : $items")
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
    fun setHospital(recyclerView: RecyclerView, items: List<Hospital>?) {
        DLog.i(tag = "BindingAdapter", message = "setHospital() --> items : $items")
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

    @BindingAdapter("setTapContents", "setVm")
    @JvmStatic
    fun setTapContents(tabLayout: TabLayout, items: List<String>?, mainVm: MainViewModel?) {
        items?.forEach {
            tabLayout.newTab().run {
                text = it
                tabLayout.addTab(this)
            }
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //Nothing.
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //Nothing.
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    mainVm?.selectPosition(position)
                }
            }
        })
    }

    @BindingAdapter("setPagerCount", "setFsm", "setVm")
    @JvmStatic
    fun setViewPager(
        viewPager: ViewPager,
        items: List<String>?,
        fragmentManager: FragmentManager?,
        mainVm: MainViewModel?
    ) {
        if (!items.isNullOrEmpty())
            viewPager.adapter?.run {
                if (this is ViewPagerAdapter) {
                    setItems(items)
                }
            } ?: kotlin.run {
                if (fragmentManager != null)
                    viewPager.adapter = ViewPagerAdapter(fragmentManager, items)
                viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                        //Nothing.
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        //Nothing.
                    }

                    override fun onPageSelected(position: Int) {
                        mainVm?.selectPosition(position)
                    }
                })
            }
    }


    @BindingAdapter("setViewPosition")
    @JvmStatic
    fun setViewPosition(view: View, position: Int?) {
        if (position != null)
            when (view) {
                is ViewPager -> {
                    view.currentItem = position
                }
                is TabLayout -> {
                    view.run {
                        getTabAt(position)?.let { tab ->
                            selectTab(tab)
                        }

                    }
                }
            }
    }

    @BindingAdapter("setUpdateTime")
    @JvmStatic
    fun setUpdateTime(textView: TextView, date: Date?) {
        date?.run {
            var time = (System.currentTimeMillis() - time) / (60 * 1000)
            val format = time.run {
                if (time > 60) {
                    time /= 60
                    "%d시간 전 업데이트"
                } else {
                    "%d분 전 업데이트"
                }
            }
            textView.text =
                String.format(format, time)
        }
    }

    @BindingAdapter("setUpdate")
    @JvmStatic
    fun setUpdate(textView: TextView, data: ClinicBase?) {
        data?.run {
            data.updateTime?.time?.let {
                val value = Utils.getCurrentTime(Utils.VALUE_DATE_FORMAT2, it)
                textView.text = String.format("업데이트 : %s", value)
            }
        }
    }

    @BindingAdapter("setStockAt")
    @JvmStatic
    fun setStockAt(textView: TextView, data: Date?) {
        data?.run {
            textView.text = String.format(
                "입고 시간 : %s",
                Utils.getCurrentTime(Utils.VALUE_DATETIME_FORMAT2, data.time)
            )
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

    @BindingAdapter("setDistance")
    @JvmStatic
    fun setDistance(textView: TextView, distance: Int?) {
        distance?.run {
            textView.text = String.format("%dm", distance)
        }
    }

    @BindingAdapter("setDistance")
    @JvmStatic
    fun setDistance(textView: TextView, distance: Float?) {
        distance?.run {
            textView.text = String.format("%.2fkm", distance)
        }
    }

    @BindingAdapter("setListener")
    @JvmStatic
    fun setListener(spinner: Spinner, viewModel: ViewModel?) {
        viewModel?.run {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent?.getItemAtPosition(position)
                    if (item is String)
                        when (viewModel) {
                            is StoreDetailViewModel -> {
                                viewModel.sortStoreList(item)
                            }
                            is ClinicViewModel -> {
                                viewModel.sortClinicList(item)
                            }
                            is HospitalViewModel -> {
                                viewModel.sortHospitalList(item)
                            }
                        }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    @BindingAdapter("setVm", "setSearchResult")
    @JvmStatic
    fun setSearchResult(
        recyclerView: RecyclerView,
        searchVm: SearchViewModel?,
        items: List<Address>?
    ) {
        if (items != null)
            recyclerView.adapter?.run {
                if (this is SearchAdapter)
                    this.setItems(items)
            } ?: kotlin.run {
                recyclerView.adapter = SearchAdapter(items).apply {
                    setHasStableIds(true)
                    clickListener = object : ClickListener {
                        override fun onItemClick(position: Int, view: View) {
                            hideSoftInput(view)
                            searchVm?.setSearchAddress(position)
                        }
                    }
                }
            }
    }

    @BindingAdapter("setVm", "searchAddress")
    @JvmStatic
    fun searchAddress(editText: EditText, searchVm: SearchViewModel?, address: String?) {
        address?.let {
            val isMatch = "^[가-힣0-9]*$".toRegex().matches(address)
            if (isMatch)
                searchVm?.setAddressListFromSearch()
        }
    }

    @BindingAdapter("setSearchAddress")
    @JvmStatic
    fun setSearchAddress(textView: TextView, address: Address?) {
        address?.run {
            textView.text = address.getAddressLine(0)
        }
    }

    @BindingAdapter("setEnableCollect")
    @JvmStatic
    fun setEnableCollect(textView: TextView, enable: Boolean?) {
        textView.run {
            if (enable == true) {
                setText(R.string.text_enable_collect)
                setBackgroundResource(R.drawable.border_plenty)
            } else {
                setText(R.string.text_disable_collect)
                setBackgroundResource(R.drawable.border_empty)
            }
        }
    }

    @BindingAdapter("setHospitalization")
    @JvmStatic
    fun setHospitalization(textView: TextView, enable: Boolean?) {
        textView.run {
            if (enable == true) {
                setText(R.string.text_enable_hospitalization)
                setBackgroundResource(R.drawable.border_plenty)
            } else {
                setText(R.string.text_disable_hospitalization)
                setBackgroundResource(R.drawable.border_empty)
            }
        }
    }

    private fun hideSoftInput(view: View) {
        val manager =
            view.context?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        manager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}