package com.kobbi.project.coronamask.network.retrofit.response

import com.kobbi.project.coronamask.network.retrofit.domain.Hospital

data class HospitalResponse (
    val resultCode:Int,
    val resultMsg:String,
    val numOfRows:Int,
    val pageNo:Int,
    val totalCount:Int,
    val items:List<Hospital>
)