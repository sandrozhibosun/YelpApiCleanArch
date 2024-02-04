package com.example.yelpapipractice.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yelpapipractice.feature.yelp.data.datasource.local.BusinessDao
import com.example.yelpapipractice.feature.yelp.data.model.entity.BusinessEntity

@Database(entities = [BusinessEntity::class], version = 1, exportSchema = false)
abstract class YelpDatabase : RoomDatabase() {
    abstract fun getBusinessDao(): BusinessDao
}