package org.d3if0112.mobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "karyawan")
data class Karyawan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val noKaryawan: String,
    val posisi: String
)
