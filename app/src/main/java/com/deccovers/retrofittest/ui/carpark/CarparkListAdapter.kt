package com.deccovers.retrofittest.ui.carpark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deccovers.retrofittest.data.carpark.model.MyCarparkEntry
import com.deccovers.retrofittest.databinding.CarparkRowLayoutBinding

class CarparkListAdapter: ListAdapter<MyCarparkEntry, CarparkListAdapter.ViewHolder>(CarparkDiffCallback) {

    companion object CarparkDiffCallback: DiffUtil.ItemCallback<MyCarparkEntry>(){
        override fun areItemsTheSame(oldItem: MyCarparkEntry, newItem: MyCarparkEntry) = oldItem.carparkNumber == newItem.carparkNumber
        override fun areContentsTheSame(oldItem: MyCarparkEntry, newItem: MyCarparkEntry) = oldItem == newItem
    }

    class ViewHolder(private val binding: CarparkRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(carparkEntry: MyCarparkEntry) {
            binding.myCarparkEntry = carparkEntry
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CarparkRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if (current != null) {
            holder.bind(current)
        }
    }
}