package com.kobbi.project.coronamask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.ClickListener
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ItemClinicDetailBinding
import com.kobbi.project.coronamask.model.Clinic
import com.kobbi.project.coronamask.util.Utils

class ClinicAdapter(items: List<Clinic>) : RecyclerView.Adapter<ClinicAdapter.ViewHolder>() {
    var clickListener: ClickListener? = null
    private val mItems = mutableListOf<Clinic>()

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

    override fun getItemId(position: Int): Long {
        return mItems[position].run {
            (Utils.replaceSign(longitude.toString()) + Utils.replaceSign((latitude.toString()))).toLong()
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < mItems.size)
            holder.bind(mItems[position])
    }

    fun setItems(items: List<Clinic>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemClinicDetailBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(clinic: Clinic) {
            binding.clinic = clinic
        }

        override fun onClick(v: View?) {
            v?.let { clickListener?.onItemClick(layoutPosition, v) }
        }
    }
}