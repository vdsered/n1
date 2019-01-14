package com.companyname.noneru

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.companyname.noneru.data.DataState
import com.companyname.noneru.data.Offer
import com.companyname.noneru.data.OfferRepository

class OfferDisplayCaseViewModel(offerRepository: OfferRepository): ViewModel() {
    private val pageSize = 20L

    var allOffers: LiveData<PagedList<Offer>>
        get() = mutableAllOffers
        private set(value) = TODO()

    var isLoading: LiveData<Boolean>
        get() = mutableIsLoading
        private set(value) = TODO()

    val offerToShowInFull: SingleLiveEvent<Offer> = SingleLiveEvent()

    private var mutableIsLoading = MutableLiveData<Boolean>()
    private var mutableAllOffers: MutableLiveData<PagedList<Offer>> = MutableLiveData()

    private var newsRetrival: LiveData<SourceProxy<Offer>>

    private val newsRetrievalObserver = Observer<SourceProxy<Offer>> { it ->
        mutableAllOffers.postValue(it.items)

        it.state.observeForever { state ->
            when(state) {
                is DataState.Loading -> mutableIsLoading.postValue(state.value)
                is DataState.Error -> {
                    // Handling errors happened while loading
                    Log.e("Error", "Display case has not worked out correctly", state.exception)
                }
            }
        }
    }

    init {
        newsRetrival = offerRepository.retrieveOffers(pageSize)
        newsRetrival.observeForever(newsRetrievalObserver)
    }

    override fun onCleared() {
        super.onCleared()
        newsRetrival.removeObserver(newsRetrievalObserver)
    }

    fun onUserNeedDetailedOffer(offer: Offer) {
        offerToShowInFull.value = offer
    }

}