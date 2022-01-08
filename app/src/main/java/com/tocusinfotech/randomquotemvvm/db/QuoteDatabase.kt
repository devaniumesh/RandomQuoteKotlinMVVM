package com.tocusinfotech.randomquotemvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tocusinfotech.randomquotemvvm.models.Result

@Database(entities = [Result::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun quoteDAO(): QuoteDAO

    companion object {

        @Volatile
        private var INSTACE: QuoteDatabase? = null

        fun getDatabase(context: Context): QuoteDatabase {

            if (INSTACE == null) {
                INSTACE = Room.databaseBuilder(
                    context,
                    QuoteDatabase::class.java,
                    "quoteDB"
                ).build()
            }
            return INSTACE!!
        }
    }
}