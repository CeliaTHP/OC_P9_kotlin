package com.example.oc_p9_kotlin.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.util.Date

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate")
    fun getAll(): Observable<MutableList<Estate>>

    @Query("SELECT * FROM estate WHERE id = :estateId")
    fun getById(estateId: String): Estate

    @Query("SELECT * FROM estate WHERE type LIKE :estateType")
    fun getByType(estateType: EstateType): Observable<MutableList<Estate>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertEstate(estate: Estate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAllEstates(estateList: List<Estate>)

    @Update
    fun updateEstate(estate: Estate)

    @Delete
    fun delete(estate: Estate)

    @Query(
        " SELECT * FROM estate WHERE type LIKE :estateType AND price_in_euros BETWEEN :priceMin " +
                "AND :priceMax AND surface_in_square_meters BETWEEN :surfaceMin AND :surfaceMax AND " +
                "rooms BETWEEN :roomsMin AND :roomsMax AND bathrooms BETWEEN :bathroomsMin AND :bathroomsMax " +
                "AND bedrooms BETWEEN :bedroomsMin AND :bedroomsMax AND mediaCount BETWEEN :photosMin AND :photosMax AND entry_date >= :entryDate AND sale_date >= :saleDate "
    )
    fun getWithFiltersWithSaleDate(
        estateType: String,
        priceMin: Int,
        priceMax: Int,
        surfaceMin: Int,
        surfaceMax: Int,
        roomsMin: Int,
        roomsMax: Int,
        bathroomsMin: Int,
        bathroomsMax: Int,
        bedroomsMin: Int,
        bedroomsMax: Int,
        photosMin: Int,
        photosMax: Int,
        entryDate: Date,
        saleDate: Date

    ): Observable<MutableList<Estate>>

    @Query(
        " SELECT * FROM estate WHERE type LIKE :estateType AND price_in_euros BETWEEN :priceMin " +
                "AND :priceMax AND surface_in_square_meters BETWEEN :surfaceMin AND :surfaceMax AND " +
                "rooms BETWEEN :roomsMin AND :roomsMax AND bathrooms BETWEEN :bathroomsMin AND :bathroomsMax " +
                "AND bedrooms BETWEEN :bedroomsMin AND :bedroomsMax AND mediaCount BETWEEN :photosMin AND :photosMax AND entry_date >= :entryDate"
    )
    fun getWithFiltersWithoutSaleDate(
        estateType: String,
        priceMin: Int,
        priceMax: Int,
        surfaceMin: Int,
        surfaceMax: Int,
        roomsMin: Int,
        roomsMax: Int,
        bathroomsMin: Int,
        bathroomsMax: Int,
        bedroomsMin: Int,
        bedroomsMax: Int,
        photosMin: Int,
        photosMax: Int,
        entryDate: Date,
    ): Observable<MutableList<Estate>>


}