package ai.benshi.test.model

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "user_actions")
data class UserActions (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val appId: String,

        val action: String,
        val resourceId: Int,
        val userId: String,
        @Embedded val meta: Meta,
    )

data class Meta (
        val timestamp: Long,
        @Embedded val  location: Location
    )

data class Location (
        val lat: String,
        @SerializedName("long")
        val lng: String,
    )
