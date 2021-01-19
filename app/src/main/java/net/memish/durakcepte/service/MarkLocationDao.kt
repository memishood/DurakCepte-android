package net.memish.durakcepte.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import net.memish.durakcepte.model.MarkLocation

@Dao
interface MarkLocationDao {
    @Query(value = "SELECT * from MarkLocation ORDER BY createdAt DESC")
    suspend fun getAll(): List<MarkLocation>

    @Query(value = "DELETE from MarkLocation")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg markLocations: MarkLocation)

    @Delete
    suspend fun delete(markLocation: MarkLocation)
}