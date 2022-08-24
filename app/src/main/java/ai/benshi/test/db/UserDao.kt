package ai.benshi.test.db

import ai.benshi.test.model.User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int) : User

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    fun isUserIsExist(id : Int) : Boolean

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}