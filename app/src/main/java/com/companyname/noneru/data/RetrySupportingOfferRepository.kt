package com.companyname.noneru.data

import androidx.paging.LivePagedListBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.companyname.noneru.SourceProxy

class RetrySupportingOfferRepository(private val api: N1Api): OfferRepository {
    private val retryingDataSources: MutableList<N1DataSource> = mutableListOf()
    override fun retrieveOffers(limit: Long): LiveData<SourceProxy<Offer>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(limit.toInt())
            .setPageSize(limit.toInt()).build()

        val pagedListLiveData = LivePagedListBuilder(object: DataSource.Factory<Long, Offer>() {
            override fun create(): DataSource<Long, Offer> {
                return N1DataSource(api)
            }
        }, pagedListConfig).build()

        return Transformations.map(pagedListLiveData) {
            val source = it.dataSource as N1DataSource

            retryingDataSources.add(source)
            source.addInvalidatedCallback {
                retryingDataSources.remove(source)
            }

            return@map SourceProxy(it, source.state)
        }
    }

    fun notifyConnectionIsRestored() {
        for(source in retryingDataSources) {
            source.retry()
        }
    }
}