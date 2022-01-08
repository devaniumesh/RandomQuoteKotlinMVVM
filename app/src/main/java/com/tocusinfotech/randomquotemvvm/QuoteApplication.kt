package com.tocusinfotech.randomquotemvvm

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.tocusinfotech.randomquotemvvm.api.QuoteService
import com.tocusinfotech.randomquotemvvm.api.RetrofitHelper
import com.tocusinfotech.randomquotemvvm.db.QuoteDatabase
import com.tocusinfotech.randomquotemvvm.repository.QuoteRepository
import com.tocusinfotech.randomquotemvvm.worker.QuoteWorker
import java.util.concurrent.TimeUnit

class QuoteApplication : Application() {

    lateinit var quoteRepository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initilize()
        setupWorker()
    }

    private fun setupWorker() {
        //we need network connection compulsory
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        //build every 30 minute request
        val workerRequest = PeriodicWorkRequest.Builder(QuoteWorker::class.java,30,TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        WorkManager.getInstance(this).enqueue(workerRequest)
    }

    private fun initilize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)

        val database = QuoteDatabase.getDatabase(applicationContext)
        quoteRepository = QuoteRepository(quoteService, database, applicationContext)
    }
}