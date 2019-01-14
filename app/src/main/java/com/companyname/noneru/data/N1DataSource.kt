package com.companyname.noneru.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class N1DataSource(private val restApi: N1Api) : PageKeyedDataSource<Long, Offer>() {

    val state: MutableLiveData<DataState> = MutableLiveData()

    private var retryCallback: (() -> Any) = {

    }

    fun retry() {
        retryCallback.invoke()
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, Offer>
    ) {
        state.postValue(DataState.Loading(true))
        val page = 1L
        val nextPage = page + 1L

        restApi
            .retrieveOffers(
                limit = params.requestedLoadSize.toLong(),
                offset = page * params.requestedLoadSize.toLong())
            .enqueue(object : Callback<N1Response> {
                override fun onResponse(call: Call<N1Response>, response: Response<N1Response>) {
                    if (response.isSuccessful) {
                        retryCallback = {}
                        val n1Response = response.body()!!
                        val offers = extractOffers(n1Response)
                        callback.onResult(offers, null, nextPage)
                    }
                    else {
                        retryCallback = {
                            loadInitial(params, callback)
                        }
                        state.postValue(DataState.Error(DataLayerException(response.errorBody()!!.string())))
                    }
                    state.postValue(DataState.Loading(false))
                }

                override fun onFailure(call: Call<N1Response>, t: Throwable?) {
                    state.postValue(DataState.Error(DataLayerException(t!!.message!!)))
                    retryCallback = {
                        loadInitial(params, callback)
                    }
                }
            })
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, Offer>
    ) {

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, Offer>
    ) {
        state.postValue(DataState.Loading(true))
        restApi
            .retrieveOffers(
                limit = params.requestedLoadSize.toLong(),
                offset = params.key * params.requestedLoadSize.toLong())
            .enqueue(object : Callback<N1Response> {
                override fun onResponse(call: Call<N1Response>, response: Response<N1Response>) {
                    if (response.isSuccessful) {
                        retryCallback = {}
                        val n1Response = response.body()!!
                        val offers = extractOffers(n1Response)
                        val offset = params.key * params.requestedLoadSize;
                        val nextKey =
                            if (offset == n1Response.metadata.resultSet.count) null else params.key + 1
                        callback.onResult(offers, nextKey)
                    }
                    else {
                        retryCallback = {
                            loadAfter(params, callback)
                        }
                        state.postValue(DataState.Error(DataLayerException(response.errorBody()!!.string())))
                    }
                    state.postValue(DataState.Loading(false))
                }

                override fun onFailure(call: Call<N1Response>, t: Throwable?) {
                    state.postValue(DataState.Error(DataLayerException(t!!.message!!)))
                    retryCallback = {
                        loadAfter(params, callback)
                    }
                }
            })
    }

    private fun extractOffers(response: N1Response): List<Offer> {
        val offers = mutableListOf<Offer>()
        val results = response.results
        val SQUARE_DECIMETER_TO_SQUARE_METER_RATIO = 100
        for(result in results) {
            val params = result.params
            val offer = Offer(
                floor = params.floor,
                floorCount = params.floorCount,
                photos = result.photos.map {
                    it.url
                },
                totalArea = params.totalArea / SQUARE_DECIMETER_TO_SQUARE_METER_RATIO,
                livingArea = params.livingArea / SQUARE_DECIMETER_TO_SQUARE_METER_RATIO,
                kitchenArea = params.kitchenArea / SQUARE_DECIMETER_TO_SQUARE_METER_RATIO,
                houseNumber = params.houseAddresses?.first()?.houseNumber ?: "",
                streetName = params.houseAddresses?.first()?.street?.name ?: "",
                roomCount = params.roomCount,
                price = params.price
            )
            offers.add(offer)
        }
        return offers
    }
}