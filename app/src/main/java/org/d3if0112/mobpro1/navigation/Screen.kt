package org.d3if0112.mobpro1.navigation

import KEY_ID_KARYAWAN

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru:Screen("detailScreen")
    data object FormUbah:Screen("detailScreen/{$KEY_ID_KARYAWAN}"){
        fun withId(id: Long) = "detailScreen/$id"
    }
}