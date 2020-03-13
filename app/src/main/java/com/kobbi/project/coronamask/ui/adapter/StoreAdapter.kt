package com.kobbi.project.coronamask.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.model.Store
import com.kobbi.project.coronamask.databinding.ItemStoreDetailBinding

class StoreAdapter(items: List<Store>) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    private val mItems = mutableListOf<Store>()

    init {
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ItemStoreDetailBinding>(
            LayoutInflater.from(parent.context), R.layout.item_store_detail, parent, false
        ).let { binding ->
            ViewHolder(binding)
        }
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].code
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("####","onBindViewHolder() --> position : $position, mItems.size : ${mItems.size}")

        if (position < mItems.size)
            holder.bind(mItems[position])
    }

    fun setItems(items:List<Store>) {
        Log.e("####","DetailAdapter.setItems() --> items : $items")
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemStoreDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store) {
            binding.store = store
        }
    }
}