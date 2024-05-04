package org.d3if3157.assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahasiswa")
class Mahasiswa (
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val nama: String,
    val nilai: Float,
    val kelas: String
)