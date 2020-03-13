package com.kobbi.project.coronamask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.database.entity.SafetyHospital
import com.kobbi.project.coronamask.databinding.ItemHospitalDetailBinding

class HospitalAdapter(items: List<SafetyHospital>) : RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {
    private val mItems = mutableListOf<SafetyHospital>()

    init {
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ItemHospitalDetailBinding>(
            LayoutInflater.from(parent.context), R.layout.item_hospital_detail, parent, false
        ).let { binding ->
            ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < mItems.size)
            holder.bind(mItems[position])
    }

    fun setItems(items:List<SafetyHospital>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHospitalDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hospital: SafetyHospital) {
            binding.hospital = hospital
        }
    }
}