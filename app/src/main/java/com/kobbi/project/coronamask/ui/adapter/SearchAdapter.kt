package com.kobbi.project.coronamask.ui.adapter

import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kobbi.project.coronamask.ClickListener
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.databinding.ItemSearchResultBinding
import com.kobbi.project.coronamask.util.Utils

class SearchAdapter(items: List<Address>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    var clickListener: ClickListener? = null
    private val mItems = mutableListOf<Address>()

    init {
        mItems.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ItemSearchResultBinding>(
            LayoutInflater.from(parent.context), R.layout.item_search_result, parent, false
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
        if (mItems.size > position)
            holder.bind(mItems[position])
    }

    fun setItems(items: List<Address>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(address: Address) {
            binding.address = address
        }

        override fun onClick(v: View?) {
            v?.let {
                clickListener?.onItemClick(layoutPosition, v)
            }
        }
    }
}