package com.example.ecomode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomode.databinding.ItemMainReceiptBinding

class MainAdapter: RecyclerView.Adapter<MainAdapter.SpendingViewHolder>() {
    var spendingData = mutableListOf<String>()
    inner class SpendingViewHolder(private val binding: ItemMainReceiptBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String){
            binding.dateText.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        return SpendingViewHolder(
            ItemMainReceiptBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        holder.bind(spendingData[position])
    }

    override fun getItemCount(): Int  = spendingData.size
}