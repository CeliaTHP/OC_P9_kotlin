package com.example.oc_p9_kotlin.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.oc_p9_kotlin.models.Estate
import com.example.oc_p9_kotlin.models.EstateType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.Date

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate")
    fun getAll(): Observable<MutableList<Estate>>

    @Query("SELECT * FROM estate WHERE id = :estateId")
    fun getById(estateId: String): Single<Estate>

    @Query("SELECT * FROM estate WHERE type LIKE :estateType")
    fun getByType(estateType: EstateType): Observable<MutableList<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEstate(estate: Estate): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEstates(estateList: List<Estate>): Completable

    @Update
    fun updateEstate(estate: Estate): Completable

    @Delete
    fun delete(estate: Estate): Completable

    @Query(
        " SELECT * FROM estate WHERE type LIKE :estateType " +
                "AND price_in_euros BETWEEN :priceMin AND :priceMax " +
                "AND surface_in_square_meters BETWEEN :surfaceMin AND :surfaceMax " +
                "AND rooms BETWEEN :roomsMin AND :roomsMax " +
                "AND bathrooms BETWEEN :bathroomsMin AND :bathroomsMax " +
                "AND bedrooms BETWEEN :bedroomsMin AND :bedroomsMax " +
                "AND media_count BETWEEN :photosMin AND :photosMax " +
                "AND entry_date >= :entryDate AND (sale_date IS NULL OR sale_date >= :saleDate) " +
                "AND is_near_station LIKE :isNearStation AND is_near_pub LIKE :isNearPub " +
                "AND is_near_hostel LIKE :isNearHostel AND is_near_hospital LIKE :isNearHospital " +
                "AND is_near_school LIKE :isNearSchool AND is_near_park LIKE :isNearPark " +
                "AND is_near_restaurant LIKE :isNearRestaurant AND is_near_other LIKE :isNearOther"
    )
    fun getWithFilters(
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
        saleDate: Date?,
        isNearStation: Boolean,
        isNearPub: Boolean,
        isNearHostel: Boolean,
        isNearHospital: Boolean,
        isNearSchool: Boolean,
        isNearPark: Boolean,
        isNearRestaurant: Boolean,
        isNearOther: Boolean

    ): Observable<MutableList<Estate>>

}