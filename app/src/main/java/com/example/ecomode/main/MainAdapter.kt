package com.example.ecomode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomode.databinding.ItemMainReceiptBinding

class MainAdapter : RecyclerView.Adapter<ReceiptViewHolder>() {
    var spendingData = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        return ReceiptViewHolder(
            ItemMainReceiptBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        holder.bind(spendingData[position])
    }

    override fun getItemCount(): Int = spendingData.size
}

class ReceiptViewHolder(private val binding: ItemMainReceiptBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        // TODO Modify data
    }
}