package com.kobbi.project.coronamask.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kobbi.project.coronamask.R
import com.kobbi.project.coronamask.database.ClinicDatabase
import com.kobbi.project.coronamask.network.crawling.CrawlingController
import com.kobbi.project.coronamask.util.DLog
import com.kobbi.project.coronamask.util.SharedPrefHelper

class SplashActivity : AppCompatActivity() {
    private var mTotalCount = 0

    companion object {
        private val NEED_PERMISSIONS =
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        startService()
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val needPermission: Array<String> = ArrayList<String>().run {
                NEED_PERMISSIONS.forEach {
                    val result = checkSelfPermission(it)
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        this.add(it)
                    }
                }
                this.toArray(arrayOf())
            }
            needPermission.run {
                if (isNotEmpty()) {
                    requestPermissions(
                        this,
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    startService()
                }
            }
        } else {
            startService()
        }
    }

    private fun startService() {
        applicationContext?.let { context ->
            ClinicDatabase.initializeDB(context, object : ClinicDatabase.CompleteListener {
                override fun onStart(fileName: String, totalCount: Int) {
                    DLog.i(tag = "Database", message = "init start... ($fileName, $totalCount)")
                    mTotalCount = totalCount
                }

                override fun onLoad(count: Int) {
                    DLog.i(tag = "Database", message = "init loading... ($count/$mTotalCount)")
                }

                override fun onComplete() {
                    DLog.i(tag = "Database", message = "init complete")
                    SharedPrefHelper.setBool(context, SharedPrefHelper.KEY_DB_INITIALIZED, true)
                    CrawlingController(applicationContext).updateClinicData()
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()
                }

                override fun onError(t: Throwable) {
                    DLog.e(tag = "Database", message = "init error >> ${t.cause},${t.message}")
                    t.printStackTrace()
                    finish()
                }
            })
        }
    }
}
