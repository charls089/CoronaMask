package com.kobbi.project.coronamask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.database.entity.SelectedClinic
import com.kobbi.project.coronamask.databinding.ItemClinicDetailBinding

class ClinicAdapter(items: List<SelectedClinic>) : RecyclerView.Adapter<ClinicAdapter.ViewHolder>() {
    private val mItems = mutableListOf<SelectedClinic>()

    init {
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ItemClinicDetailBinding>(
            LayoutInflater.from(parent.context), R.layout.item_clinic_detail, parent, false
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

    fun setItems(items:List<SelectedClinic>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemClinicDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clinic: SelectedClinic) {
            binding.clinic = clinic
        }
    }
}