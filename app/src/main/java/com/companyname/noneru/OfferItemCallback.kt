package com.companyname.noneru

import androidx.recyclerview.widget.DiffUtil
import com.companyname.noneru.data.Offer

class OfferItemCallback: DiffUtil.ItemCallback<Offer>() {
    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem == newItem
    }
}