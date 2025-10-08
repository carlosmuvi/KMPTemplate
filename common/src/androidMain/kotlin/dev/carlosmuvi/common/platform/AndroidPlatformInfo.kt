package dev.carlosmuvi.common.platform

import android.os.Build

class AndroidPlatformInfo : PlatformInfo {
    override fun getPlatformName(): String = "Android"

    override fun getOSVersion(): String = Build.VERSION.RELEASE
}
