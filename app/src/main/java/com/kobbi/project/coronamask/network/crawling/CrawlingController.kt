package com.kobbi.project.coronamask.network.crawling

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.database.entity.ClinicInfo
import com.kobbi.project.coronamask.util.Utils
import org.jsoup.Jsoup
import kotlin.concurrent.thread

class CrawlingController(private val mContext: Context) {
    enum class UrlType(val url: String, val code: String) {
        SAFETY_HOSPITAL("popup_200128", "HOSPITAL"),
        ENABLE_COLLECT_CLINIC("popup_200128_2", "CLINIC"),
        ALL_CLINIC("popup_200128_3", "CLINIC"),
        DRIVE_THROUGH("popup_200128_4", "DRIVE_THROUGH");

        companion object {
            fun getUrl(type: UrlType): String {
                return String.format("https://www.mohw.go.kr/react/%s.html", type.url)
            }
        }
    }

    fun updateClinicData() {
        val clinicInfoDao = ClinicDatabase.getDatabase(mContext).clinicInfoDao()
        UrlType.values().forEach { type ->
            thread {
                val results = mutableListOf<ClinicInfo>()
                Jsoup.connect(UrlType.getUrl(type)).get().run {
                    Log.e("####", "title : ${title()}")
                    title()?.let { title ->
                        val startIndex = title.indexOf('(')
                        val endIndex = title.indexOf('일')
                        val updateTime = title().substring(startIndex + 1, endIndex + 1)
                        Log.e("####", "updateTime : $updateTime")
                        val parseData = Utils.convertStringToDate("yy년 M월 dd일", updateTime)
                        Log.e("####", "parseData : $parseData")
                    }
                    getElementsByClass("tb_center").let { tb ->
                        tb.select("tr").forEach { row ->
                            row.select("td").let { col ->
                                if (col.size > 3) {
                                    val name = col[2].text().substringBefore(" *")
                                    if (clinicInfoDao.findFromCode(type.code, name).isEmpty()) {
                                        getAddressList(name)?.let { addressList ->
                                            if (addressList.isNotEmpty()) {
                                                val province = col[0].text()
                                                val city = col[1].text()
                                                var tel = col[3].text()
                                                var category = "O"
                                                when (type) {
                                                    UrlType.SAFETY_HOSPITAL -> {
                                                        tel = col[4].text()
                                                        category =
                                                            if (col[3].text().contains("입원"))
                                                                "B"
                                                            else
                                                                "A"
                                                    }
                                                    UrlType.DRIVE_THROUGH -> {
                                                        category = "O"
                                                    }
                                                    UrlType.ALL_CLINIC -> {
                                                        category =
                                                            if (col[2].text().contains('*'))
                                                                "O"
                                                            else
                                                                "X"
                                                    }
                                                    UrlType.ENABLE_COLLECT_CLINIC -> {
                                                        category = "O"
                                                    }
                                                }
                                                addressList[0].run {
                                                    results.add(
                                                        ClinicInfo(
                                                            type.code,
                                                            province,
                                                            city,
                                                            name,
                                                            getAddressLine(0),
                                                            tel,
                                                            category,
                                                            latitude,
                                                            longitude
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Log.e("####", "$type >> new Data : $results")
                        clinicInfoDao.insert(results)
                    }
                }
            }
        }
    }

    private fun getAddressList(name: String): List<Address>? {
        return try {
            Geocoder(mContext).getFromLocationName(name, 1)?.let {
                if (it.isNotEmpty()) it else null
            }
        } catch (e: Exception) {
            Log.e("####", "getAddressList.error >>>> ${e.cause} : ${e.message}")
            e.printStackTrace()
            null
        }
    }
}