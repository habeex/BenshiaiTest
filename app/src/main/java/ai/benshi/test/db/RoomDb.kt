package ai.benshi.test.db

import ai.benshi.test.model.Comment
import ai.benshi.test.model.Post
import ai.benshi.test.model.User
import ai.benshi.test.model.UserActions
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = [Post::class, User::class, Comment::class, UserActions::class],
    version = 3,
    exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    companion object {
        fun create(context: Context): RoomDb {
            val databaseBuilder = Room.databaseBuilder(context, RoomDb::class.java, "test_app.db")
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun postsDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun userActionDao(): UserActionDao
}