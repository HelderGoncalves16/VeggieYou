package com.example.veggieyou.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [CarrinhoItem::class], exportSchema = false)
abstract class CarrinhoDatabase : RoomDatabase() {
    abstract fun carrinhoDao(): CarrinhoDAO

    companion object {
        private var instance: CarrinhoDatabase? = null

        fun getInstance(context: Context): CarrinhoDatabase {
            if (instance == null)
                instance = Room.databaseBuilder<CarrinhoDatabase>(
                    context,
                    CarrinhoDatabase::class.java,
                    "VeggieYouDB"
                ).build()
            return instance!!
        }
    }
}