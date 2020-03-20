package com.kobbi.project.coronamask.database.dao

import androidx.room.*
import com.kobbi.project.coronamask.database.entity.ClinicInfo

@Dao
interface ClinicInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<ClinicInfo>)

    @Delete
    fun delete(vararg clinicInfo: ClinicInfo)

    @Query("SELECT * FROM ClinicInfo")
    fun loadAll(): List<ClinicInfo>

    @Query("SELECT * FROM ClinicInfo WHERE province = :province AND city = :city")
    fun find(province: String, city: String): List<ClinicInfo>

    @Query("SELECT * FROM ClinicInfo WHERE code = :code")
    fun findFromCode(code: String): List<ClinicInfo>

    @Query("SELECT * FROM ClinicInfo WHERE code = :code AND name = :name")
    fun findFromCode(code: String, name: String): List<ClinicInfo>
}