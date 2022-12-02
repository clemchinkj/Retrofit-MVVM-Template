package com.deccovers.retrofittest.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deccovers.retrofittest.data.weather.model.MyAreaMetadataEntry
import com.deccovers.retrofittest.databinding.AreaMetadataRowLayoutBinding

class AreaMetadataListAdapter: ListAdapter<MyAreaMetadataEntry, AreaMetadataListAdapter.ViewHolder>(AreaMetadataDiffUtilCallback) {
    companion object AreaMetadataDiffUtilCallback: DiffUtil.ItemCallback<MyAreaMetadataEntry>(){
        override fun areItemsTheSame(oldItem: MyAreaMetadataEntry, newItem: MyAreaMetadataEntry) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: MyAreaMetadataEntry, newItem: MyAreaMetadataEntry) = oldItem == newItem
    }

    class ViewHolder(private val binding: AreaMetadataRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(myAreaMetadataEntry: MyAreaMetadataEntry) {
            binding.myAreaMetadataEntry = myAreaMetadataEntry
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AreaMetadataRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if (current != null) {
            holder.bind(current)
        }
    }
}