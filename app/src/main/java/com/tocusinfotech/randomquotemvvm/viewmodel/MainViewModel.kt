package com.tocusinfotech.randomquotemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tocusinfotech.randomquotemvvm.models.QuoteList
import com.tocusinfotech.randomquotemvvm.repository.QuoteRepository
import com.tocusinfotech.randomquotemvvm.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: QuoteRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes(1)
        }
    }

    val quotes: LiveData<Response<QuoteList>>
        get() = repository.quotes
}