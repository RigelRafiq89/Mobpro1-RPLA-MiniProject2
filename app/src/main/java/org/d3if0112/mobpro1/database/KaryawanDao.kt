package org.d3if0112.mobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0112.mobpro1.model.Karyawan

@Dao
interface KaryawanDao {
    @Insert
    suspend fun insert(karyawan: Karyawan)

    @Update
    suspend fun update(karyawan: Karyawan)

    @Query("SELECT * FROM karyawan ORDER BY id DESC")
    fun getMahasiswa(): Flow<List<Karyawan>>

    @Query("SELECT * FROM karyawan WHERE id = :id")
    suspend fun getCatatanById(id:Long): Karyawan?

    @Query("DELETE FROM karyawan WHERE id = :id")
    suspend fun deleteById(id: Long)
}