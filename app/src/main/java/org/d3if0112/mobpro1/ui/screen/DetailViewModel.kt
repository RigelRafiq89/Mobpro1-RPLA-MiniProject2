package org.d3if0112.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0112.mobpro1.database.KaryawanDao
import org.d3if0112.mobpro1.model.Karyawan

class DetailViewModel(private val dao:KaryawanDao) : ViewModel() {

//    private val formatter = SimpleDateFormat("yyyy-")

    fun insert(nama: String, nomorKaryawan: String, posisi: String){
        val karyawan = Karyawan(
            name = nama,
            noKaryawan = nomorKaryawan,
            posisi = posisi
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.insert(karyawan)
        }
    }

    suspend fun getCatatan(id: Long): Karyawan?{
        return dao.getCatatanById(id)
    }

    fun update(id: Long, nama: String, noKaryawan: String, posisi: String){
        val karyawan = Karyawan(
            id = id,
            name = nama,
            noKaryawan = noKaryawan,
            posisi = posisi
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(karyawan)
        }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}