package net.memish.durakcepte.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class MarkLocation(
    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "path")
    val path: String?,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "latitude")
    val latitude: Double?,

    @ColumnInfo(name = "longitude")
    val longitude: Double?,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long?
): Serializable {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}