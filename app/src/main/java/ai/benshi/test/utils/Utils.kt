package ai.benshi.test.utils

import android.content.Context
import android.provider.Settings
import java.security.MessageDigest

class Utils {
    companion object {

        fun String.sha256(): String {
            return hashString(this, "SHA-256")
        }

        private fun hashString(input: String, algorithm: String): String {
            return MessageDigest
                .getInstance(algorithm)
                .digest(input.toByteArray())
                .fold("") { str, it -> str + "%02x".format(it) }
        }

        fun getDeviceId(context: Context) : String{
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

    }
}