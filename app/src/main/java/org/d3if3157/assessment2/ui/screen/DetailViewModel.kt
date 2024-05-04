package org.d3if3157.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3157.assessment2.database.MahasiswaDao
import org.d3if3157.assessment2.model.Mahasiswa

class DetailViewModel(private val dao: MahasiswaDao) : ViewModel(){
    fun insert(nama: String, nilai: Float, kelas: String){
        val mahasiswa = Mahasiswa(
            nama = nama,
            nilai = nilai,
            kelas = kelas
        )

        viewModelScope.launch(Dispatchers.IO) { dao.insert(mahasiswa) }
    }

    suspend fun getMahasiswa(id: Long): Mahasiswa? {
        return dao.getMahasiswaById(id)
    }
    fun update(id: Long, nama: String, nilai: Float, kelas: String){
        val mahasiswa = Mahasiswa(
            id = id,
            nama = nama,
            nilai = nilai,
            kelas = kelas
        )

        viewModelScope.launch(Dispatchers.IO) { dao.update(mahasiswa) }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) { dao.deleteById(id) }
    }
}