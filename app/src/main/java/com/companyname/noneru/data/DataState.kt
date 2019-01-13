package com.companyname.noneru.data

abstract class DataState {
    data class Loading(val value: Boolean): DataState()
    data class Error(val exception: Throwable): DataState()
}