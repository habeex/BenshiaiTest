package ai.benshi.test.preferences

import android.content.Context
import android.content.SharedPreferences

class PrefManager {
    var pref: SharedPreferences
    var edior: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        edior = pref.edit()
    }

    var email: String
        get() = pref.getString(KEY_EMAIL, "").toString()
        set(value) = edior.putString(KEY_EMAIL, value).apply()

    companion object {

        val PREF_NAME: String = "benshi-test"
        val KEY_EMAIL: String = "email"

    }

}