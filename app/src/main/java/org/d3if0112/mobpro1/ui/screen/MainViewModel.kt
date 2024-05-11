package org.d3if0112.mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0112.mobpro1.database.KaryawanDao
import org.d3if0112.mobpro1.model.Karyawan

class MainViewModel(dao: KaryawanDao) : ViewModel() {
    val data: StateFlow<List<Karyawan>> = dao.getMahasiswa().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private  fun getDataDummy(): List<Karyawan>{
        val data = mutableListOf<Karyawan>()
        for (i in 1 until 10 step 3){
            data.add(
                Karyawan(
                    i.toLong(),
                    "Rizza Indah Mega Mandasari",
                    "6706244601",
                    "D3IF-46-01"
                )
            )
            data.add(
                Karyawan(
                    i.toLong()+1,
                    "Indra Azimi",
                    "6706244602",
                    "D3IF-46-02"
                )
            )
            data.add(
                Karyawan(
                    i.toLong()+2,
                    "Reza Budiawan",
                    "6706244612",
                    "D3IF-46-02"
                )
            )
        }
        return data
    }
}