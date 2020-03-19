package com.kobbi.project.coronamask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.ClickListener
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ItemHospitalDetailBinding
import com.kobbi.project.coronamask.model.Hospital

class HospitalAdapter(items: List<Hospital>) : RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {
    var clickListener: ClickListener? = null
    private val mItems = mutableListOf<Hospital>()

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

    override fun getItemId(position: Int): Long {
        return mItems[position].code.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < mItems.size)
            holder.bind(mItems[position])
    }

    fun setItems(items: List<Hospital>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHospitalDetailBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(hospital: Hospital) {
            binding.hospital = hospital
        }

        override fun onClick(v: View?) {
            v?.let { clickListener?.onItemClick(layoutPosition, v) }
        }
    }
}