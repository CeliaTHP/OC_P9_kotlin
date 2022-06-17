package com.example.oc_p9_kotlin

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.providers.EstateContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class EstateContentProviderTests {
    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    private val TEST_ID = "testId"
    private val TEST_ADDRESS = "testAddress from Content Provider Test"
    private val TEST_PRICE = 45000

    private var database: EstateDatabase? = null

    // DATA SET FOR TEST
    private val ID: Long = 1

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            EstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver

    }

    @Test
    fun getItemsWhenNoItemInserted() {

        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ESTATE,
                ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())

        assertThat(cursor!!.count, `is`(0))

        cursor.close()
    }

    @Test
    fun insertAndGetItem() {

        // BEFORE : Adding demo item
        val userUri: Uri? =
            mContentResolver!!.insert(EstateContentProvider.URI_ESTATE, generateItem())

        // TEST
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ESTATE,
                ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())

        assertThat(cursor!!.count, `is`(1))

        assertThat(cursor.moveToFirst(), `is`(true))

        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow("id")),
            `is`(TEST_ID)
        )
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow("address")),
            `is`(TEST_ADDRESS)
        )
        assertThat(
            cursor.getInt(cursor.getColumnIndexOrThrow("price_in_euros")),
            `is`(TEST_PRICE)
        )


    }

    // ---

    // ---
    private fun generateItem(): ContentValues {
        val values = ContentValues()
        values.put("id", TEST_ID)
        values.put("address", TEST_ADDRESS)
        values.put("price_in_euros", TEST_PRICE)

        return values
    }

}