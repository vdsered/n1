package com.companyname.noneru

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.companyname.noneru.data.Offer

class OfferView(private val activity: AppCompatActivity,
                private val view: View) {
    private val pricePattern = activity.getString(R.string.price)
    private val addressPattern = activity.getString(R.string.full_address)
    private val areaPattern = activity.getString(R.string.area_values)
    private val roomPattern = activity.getString(R.string.room_count)
    private val floorPattern = activity.getString(R.string.floor_detailed_information)

    private val area: TextView = view.findViewById(R.id.area)
    private val images: RecyclerView = view.findViewById(R.id.images)
    private val price: TextView = view.findViewById(R.id.price)
    private val address: TextView = view.findViewById(R.id.address)
    private val roomCount: TextView = view.findViewById(R.id.roomCount)
    private val floorInfo: TextView = view.findViewById(R.id.floorInfo)
    private lateinit var adapter: ImageCollectionAdapter
    fun onCreate(offer: Offer) {
        price.text = String.format(pricePattern, offer.price.toString())
        area.text = String.format(areaPattern,
            if (offer.kitchenArea == 0) '-' else offer.kitchenArea,
            if (offer.livingArea == 0) '-' else offer.livingArea,
            offer.totalArea)
        roomCount.text = String.format(roomPattern, offer.roomCount)
        floorInfo.text = String.format(floorPattern, offer.floor, offer.floorCount)
        if(offer.houseNumber.isNullOrBlank() && offer.streetName.isNullOrBlank()) {
            address.text = activity.getString(R.string.unknown_address)
        }
        else {
            address.visibility = View.VISIBLE
            address.text = String.format(addressPattern, offer.houseNumber, offer.streetName)
        }
        images.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        adapter = ImageCollectionAdapter(activity, offer.photos)
        images.adapter = adapter
    }

    fun setOnClickListener(listener: () -> Unit) {
        /*
            We set listeners to item views, because a nested RecyclerView does not invoke a click listener.
            So this is a workaround.
         */
        adapter.clickListener = View.OnClickListener {
            listener.invoke()
        }
        /*
            This operation is expensive, but it is called only when we set a listener which is rather rare case.
            We need it, because a listener may be set after binding.
        */
        adapter.notifyDataSetChanged()
        view.setOnClickListener {
            listener.invoke()
        }
    }
}
