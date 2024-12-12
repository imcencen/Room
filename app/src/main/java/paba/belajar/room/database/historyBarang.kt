package paba.belajar.room.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class historyBarang (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int = 0,

    @ColumnInfo(name="tanggal")
    var tanggal: String? = null,

    @ColumnInfo(name="item")
    var item: String? = null,

    @ColumnInfo(name="jumlah")
    var jumlah: String? = null,

    @ColumnInfo(name="status")
    var status: Int = 0
)