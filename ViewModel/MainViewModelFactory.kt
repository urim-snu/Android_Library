package com.tripaimap.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.package.maps.database.DaoClass

class MainViewModelFactory(
    private val dataSource: DaoClass
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}