package com.kobbi.project.coronamask.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kobbi.project.coronamask.database.entity.SafetyHospital

@Dao
interface SafetyHospitalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hospitals: List<SafetyHospital>)

    @Delete
    fun delete(vararg hospitals: SafetyHospital)

    @Query("SELECT * FROM SafetyHospital")
    fun loadAll(): List<SafetyHospital>

    @Query("SELECT * FROM SafetyHospital")
    fun loadLive(): LiveData<List<SafetyHospital>>

    @Query("SELECT * FROM SafetyHospital WHERE province = :province AND city = :city")
    fun find(province: String, city: String): List<SafetyHospital>
}