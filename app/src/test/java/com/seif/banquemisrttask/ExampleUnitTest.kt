package com.seif.banquemisrttask

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
// has internet connection
// has no internet connection -> show retry design
// has internet and response is successful
// has internet and response is failed
// swiped refresh and has internet -> request api
// swiped refresh and has no internet -> show retry design
