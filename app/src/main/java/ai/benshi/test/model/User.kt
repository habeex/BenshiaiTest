package ai.benshi.test.model

import androidx.room.*

@Entity(tableName = "user")
data class User (
        @PrimaryKey
        val id: Int,
        val name: String,
        val username: String,
        val email: String?,
        val phone: String?,
        val website: String?,
        @Embedded val address: Address
    )


data class PostUser (
    val name: String,
    val username: String,
    val email: String?,
    val phone: String?,
    val website: String?,
    @Embedded val address: Address
)


data class Address (
    val street: String?,
    val suite: String?,
    val city: String?,
    val zipcode: String?,
    @Embedded val geo: Geo
)

data class Geo (
    val lat: String,
    val lng: String,
)
