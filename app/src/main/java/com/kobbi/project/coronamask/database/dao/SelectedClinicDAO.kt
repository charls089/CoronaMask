package com.kobbi.project.coronamask.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kobbi.project.coronamask.database.entity.SelectedClinic

@Dao
interface SelectedClinicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hospitals: List<SelectedClinic>)

    @Delete
    fun delete(vararg hospitals: SelectedClinic)

    @Query("SELECT * FROM SelectedClinic")
    fun loadAll(): List<SelectedClinic>

    @Query("SELECT * FROM SelectedClinic")
    fun loadLive(): LiveData<List<SelectedClinic>>

    @Query("SELECT * FROM SelectedClinic WHERE province = :province AND city = :city")
    fun find(province: String, city: String): List<SelectedClinic>
}