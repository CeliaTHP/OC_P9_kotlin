package com.example.oc_p9_kotlin.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.oc_p9_kotlin.models.Estate

@Dao
interface EstateDao {

    @Query("SELECT * FROM estate")
    fun getAll(): List<Estate>

    @Query("SELECT * FROM estate WHERE id = :estateId")
    fun getById(estateId: String): Estate

    @Query("SELECT * FROM estate WHERE type LIKE :estateType")
    fun findByType(estateType: String): List<Estate>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertEstate(estate: Estate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAllEstates(estateList: List<Estate>)

    @Update
    fun updateEstate(estate: Estate): String

    @Delete
    fun delete(estate: Estate)


}