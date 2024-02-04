package com.example.yelpapipractice.feature.yelp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.yelpapipractice.feature.yelp.data.model.entity.BusinessEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {
    @Query("SELECT * FROM businesses")
    fun getBusinesses(): Flow<List<BusinessEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(businesses: List<BusinessEntity>)

    @Query("DELETE FROM businesses")
    suspend fun deleteAll()

    @Transaction
    suspend fun cleanAndCacheBusinesses(businesses: List<BusinessEntity>) {
        deleteAll()
        insert(businesses)
    }
}