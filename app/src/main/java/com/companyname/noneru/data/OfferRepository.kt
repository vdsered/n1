package com.companyname.noneru.data

import androidx.lifecycle.LiveData
import com.companyname.noneru.SourceProxy

interface OfferRepository {
    fun retrieveOffers(limit: Long): LiveData<SourceProxy<Offer>>
}