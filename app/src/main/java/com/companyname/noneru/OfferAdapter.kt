package com.companyname.noneru

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.companyname.noneru.data.Offer

class OfferAdapter(
    private val activity: AppCompatActivity,
    private val viewModel: OfferDisplayCaseViewModel
):
    PagedListAdapter<Offer, OfferAdapter.OfferViewHolder>(OfferItemCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val attachToRoot = false
        val view = inflater.inflate(R.layout.offer_layout, parent, attachToRoot)
        return OfferViewHolder(activity, view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val article = getItem(position)
        article?.let {
            holder.bind(viewModel, it)
        }
    }

    class OfferViewHolder(private val activity: AppCompatActivity,
                          view: View): RecyclerView.ViewHolder(view) {
        private val offerView = OfferView(activity, view)
        fun bind(viewModel: OfferDisplayCaseViewModel, offer: Offer) {
            offerView.onCreate(offer)

            offerView.setOnClickListener {
                viewModel.onUserNeedDetailedOffer(offer)
            }
        }
    }
}