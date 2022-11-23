package com.demothemedappicon

import android.app.Application
import com.google.api.GoogleAPI

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        /*GoogleAPI.setHttpReferrer("http://code.google.com/p/google-api-translate-java/")
        GoogleAPI.setKey("AIzaSyBi7y4EgC0B-Q9UZg-tOzsOI0sLQ8F1Oow")
*/    }
}