package com.example.oc_p9_kotlin.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.oc_p9_kotlin.databases.EstateDatabase
import com.example.oc_p9_kotlin.models.Estate


class EstateContentProvider : ContentProvider() {

    companion object {

        const val AUTHORITY = "com.example.oc_p9_kotlin.providers"
        val TABLE_NAME = "estate_database"
        val URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        context?.let {
            val cursor: Cursor =
                EstateDatabase.getDatabase(it).estateDao().getAllWithCursor()
            cursor.setNotificationUri(it.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        if (contentValues != null) {
            context?.let {
                val id = EstateDatabase.getDatabase(it).estateDao()
                    .insertEstateForContentProvider(Estate.fromContentValues(contentValues))

                if (id != 0L) {
                    it.contentResolver.notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, id);
                }
            }
        }
        throw IllegalArgumentException("Failed to insert row into $uri");

    }

    override fun delete(uri: Uri, string: String?, strings: Array<out String>?): Int {
        context?.let {
            val count = EstateDatabase.getDatabase(it).estateDao()
                .deleteWithId(ContentUris.parseId(uri));

            it.contentResolver.notifyChange(uri, null);
            return count;
        }
        throw IllegalArgumentException("Failed to delete row into " + uri);
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        string: String?,
        strings: Array<out String>?
    ): Int {
        context?.let {
            if (contentValues != null) {
                val count = EstateDatabase.getDatabase(it).estateDao()
                    .updateEstateForContentProvider(Estate.fromContentValues(contentValues));

                it.contentResolver.notifyChange(uri, null);
                return count;
            }
        }
        throw IllegalArgumentException("Failed to update row into $uri");
    }

}