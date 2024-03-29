package com.example.yelpapipractice.feature.yelp.utils

import javax.inject.Qualifier

//Retention(AnnotationRetention.BINARY)
// how long will the annotation be stored

// BINARY: not stored in runtime, only compiled time, not in JVM, middle lifetime

// Source : only stored in source code, not in JVM, shortest lifetime

// RUNTIME: stored in runtime, available in JVM, longest lifetime

// used for Data transfer operations like reading/writing to database, network operations
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

// optimized to perform CPU-intensive work outside of the main thread. e.g. sorting a list and parsing JSON. If you don't specify a dispatcher, Dispatchers.Default is used
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

// used only for interacting with the UI and performing quick work, e.g. calling suspend functions, running Android UI framework operations, and updating LiveData objects.
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnconfinedDispatcher