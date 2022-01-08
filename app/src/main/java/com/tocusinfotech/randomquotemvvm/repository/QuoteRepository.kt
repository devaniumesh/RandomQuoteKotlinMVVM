package com.tocusinfotech.randomquotemvvm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tocusinfotech.randomquotemvvm.api.QuoteService
import com.tocusinfotech.randomquotemvvm.db.QuoteDatabase
import com.tocusinfotech.randomquotemvvm.models.QuoteList
import com.tocusinfotech.randomquotemvvm.util.NetworkUtil
import java.lang.Exception

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context
) {

    private val quotesLiveData = MutableLiveData<Response<QuoteList>>()

    val quotes: LiveData<Response<QuoteList>>
        get() = quotesLiveData


    suspend fun getQuotes(page: Int) {

        if (NetworkUtil.isNetworkAvailable(applicationContext)) {
            try {
                val result = quoteService.getQuotes(page)
                if (result?.body() != null) {
                    //insert result into database
                    quoteDatabase.quoteDAO().addQuotes(result.body()!!.results)
                    quotesLiveData.postValue(Response.Success(result.body()))
                }else
                {
                    quotesLiveData.postValue(Response.Error("API Error"))
                }
            }catch (e : Exception){
                quotesLiveData.postValue(Response.Error(e.message.toString()))
            }
        } else {
            val quotes = quoteDatabase.quoteDAO().getQuotes()

            //set dummy data to other parameter to convert above result into quoteList
            val quoteList = QuoteList(1, 1, 1, quotes, 1, 1)
            quotesLiveData.postValue(Response.Success(quoteList))
        }
    }

    suspend fun getQuotesBackground(){
        val randomPage = (Math.random() * 10).toInt()
        val result = quoteService.getQuotes(randomPage)
        if(result?.body() != null){
            quoteDatabase.quoteDAO().addQuotes(result.body()!!.results)
        }
    }
}