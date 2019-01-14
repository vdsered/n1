package com.companyname.noneru

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.companyname.noneru.data.Offer
import kotlinx.serialization.protobuf.ProtoBuf

class OfferDisplayCaseView {
    private lateinit var adapter: OfferAdapter
    private lateinit var offersRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    fun onCreate(viewModel: OfferDisplayCaseViewModel, activity: AppCompatActivity) {
        activity.setContentView(R.layout.activity_main)
        progressBar = activity.findViewById(R.id.progressBar)
        offersRecyclerView = activity.findViewById(R.id.offers)
        offersRecyclerView.isNestedScrollingEnabled = true
        offersRecyclerView.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        offersRecyclerView.addItemDecoration(decoration)
        adapter = OfferAdapter(activity, viewModel)
        offersRecyclerView.adapter = adapter

        viewModel.isLoading.observe(activity, Observer {
            progressBar.visibility = if (it) VISIBLE else INVISIBLE
        })

        viewModel.allOffers.observe(activity, Observer {
            adapter.submitList(it)
        })

        viewModel.offerToShowInFull.observeExclusively(activity, Observer {
            showDetailedOffer(activity, it)
        })
    }

    private fun showDetailedOffer(activity: AppCompatActivity, offer: Offer) {
        val intent = Intent(activity, DetailedOfferActivity::class.java)
        intent.putExtras(prepareBundle(offer))
        activity.startActivity(intent)
    }

    private fun prepareBundle(offer: Offer): Bundle {
        val serializer = Offer.serializer()
        val buffer = ProtoBuf.dump(serializer, offer)
        val bundle = Bundle()
        bundle.putByteArray(DetailedOfferActivity.Params.article, buffer)
        return bundle
    }
}