package com.companyname.noneru

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.companyname.noneru.data.DataState

/*
    This looks like a DTO at a first glance, but the fields of the class are actually
    live objects that are connected to a data source or data sources and observe their state.
    This is a more readable way to carry the objects compared to using Pair.
 */
data class SourceProxy<Data>(
    val items: PagedList<Data>,
    val state: LiveData<DataState>
)