package com.kobbi.project.coronamask.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kobbi.project.coronamask.database.entity.ClinicBase

@Dao
interface ClinicBaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ClinicBase)

    @Delete
    fun delete(vararg data: ClinicBase)

    @Query("SELECT * FROM ClinicBase WHERE code = :code")
    fun getBaseLive(code: String): LiveData<ClinicBase>?
}