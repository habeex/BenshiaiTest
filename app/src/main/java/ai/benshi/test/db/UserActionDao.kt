package ai.benshi.test.db

import ai.benshi.test.model.UserActions
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserActionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actions: UserActions)

    @Query("SELECT * FROM user_actions")
    suspend fun getUserAction() : List<UserActions>


    @Query("DELETE FROM user_actions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM user_actions")
    suspend fun deleteAll()

}