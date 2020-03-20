package com.kobbi.project.coronamask

import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.network.crawling.CrawlingController
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkSplitCsv() {
        val text = "55,Y,서울,용산구,용산구보건소,\"서울특별시 용산구 녹사평대로 150 (이태원동, 용산구종합행정타운)\",\"02-2199-8371, 02-2199-8374\""
        val splits = ClinicDatabase.splitCsvString(text)
        splits.forEach {
            println("result : $it")
        }
        assertEquals(splits.size, 7)
    }

    @Test
    fun convertFloatToInt() {
        val testData = 12345671.424523f
        println("testData/1000 : ${testData/1000}")
        println("testData/1000f : ${testData/1000f}")
    }
}
