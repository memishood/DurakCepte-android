package net.memish.durakcepte.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.memish.durakcepte.model.MarkLocation

@Database(entities = [MarkLocation::class], version = 1)
abstract class MarkLocationDatabase : RoomDatabase() {
    abstract fun markLocationDao() : MarkLocationDao

    companion object {
        @Volatile private var instance: MarkLocationDatabase? = null
        private val any = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(any) {
            instance ?: Room
                .databaseBuilder(context, MarkLocationDatabase::class.java, context.packageName)
                .build().also { instance = it }
        }
    }
}