package com.rosenea.codingexercise

import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * Simple unit tests which verify the functionality of the implemented code.
 */
class CodingExerciseUnitTests {
    /**
     * This test is responsible for ensuring the return from the [source] method matches what was
     * manually cached.
     */
    @Test
    fun `verify data retrieved from URL matches data manually acquired`() {
        assertEquals(
            "The retrieved data does not match manually acquired data.",
            TEST_JSON,
            SOURCE_URL.source
        )
    }

    /**
     * This test ensures the correct number of data elements where returned by the testing code.
     *
     * **NOTE:** This method utilizes the [TEST_JSON] to avoid unnecessary requests to [SOURCE_URL].
     */
    @Test
    fun `verify the JSON data can be properly parsed`() {
        val allData = Json.decodeFromString<List<Data>>(TEST_JSON)

        // Verify the total number of elements
        assertEquals(
            "The deserialized list is not the expected size.",
            EXPECTED_DATA_ELEMENTS,
            allData.size
        )

        // Verify a specific element
        assertEquals(906, allData[19].id)
        assertEquals(2, allData[19].listId)
        assertEquals("Item 906", allData[19].name)
    }

    /**
     * This verifies that all the data can be acquired and processed correctly.
     */
    @Test
    fun `verify the requestData method is behaving as expected`() {
        val allData = mutableListOf<Data>()
        requestData {
            allData.addAll(it)
        }

        // Wait at most 1 second for the data to be acquired.
        Thread.sleep(TimeUnit.SECONDS.toMillis(1L))

        // Verify the total number of elements
        assertEquals(
            "The deserialized list is not the expected size.",
            EXPECTED_DATA_ELEMENTS,
            allData.size
        )

        // Verify a specific element
        assertEquals(906, allData[19].id)
        assertEquals(2, allData[19].listId)
        assertEquals("Item 906", allData[19].name)
    }

    companion object {
        /**
         * The number of elements in the unfiltered list of data elements.
         */
        private const val EXPECTED_DATA_ELEMENTS = 1000
    }
}