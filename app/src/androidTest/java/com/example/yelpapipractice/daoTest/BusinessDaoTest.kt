package com.example.yelpapipractice.daoTest

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.yelpapipractice.core.data.database.YelpDatabase
import com.example.yelpapipractice.feature.yelp.data.datasource.local.BusinessDao
import com.example.yelpapipractice.utils.YelpTestFactory
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BusinessDaoTest {

    private lateinit var database: YelpDatabase
    private lateinit var businessDao: BusinessDao

    @Before
    fun setup() {
        // Create an in-memory version of the Room database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            YelpDatabase::class.java
        ).allowMainThreadQueries().build()

        businessDao = database.getBusinessDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadBusiness() = runBlocking {
        val businessList = listOf(
            YelpTestFactory.testBusinessEntity
        )
        businessDao.insert(businessList)

        val allBusiness = businessDao.getBusinesses().first()
        assertEquals(businessList, allBusiness)
    }

    @Test
    fun cleanAndCacheBusiness() = runBlocking {
        val initialBusiness = listOf(
            YelpTestFactory.testBusinessEntity,
        )
        val newBusiness = listOf(
            YelpTestFactory.testBusinessEntityById(2),
            YelpTestFactory.testBusinessEntityById(3),
        )

        businessDao.insert(initialBusiness)
        businessDao.cleanAndCacheBusinesses(newBusiness)

        val allBusiness = businessDao.getBusinesses().first()
        assertEquals(newBusiness, allBusiness)
    }

    @Test
    fun deleteAllBusiness() = runBlocking {
        val newBusiness = listOf(
            YelpTestFactory.testBusinessEntityById(1),
            YelpTestFactory.testBusinessEntityById(2),
        )

        businessDao.insert(newBusiness)
        businessDao.deleteAll()

        val allBusiness = businessDao.getBusinesses().first()
        TestCase.assertTrue(allBusiness.isEmpty())
    }
}