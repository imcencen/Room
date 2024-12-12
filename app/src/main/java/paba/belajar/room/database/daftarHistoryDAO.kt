package paba.belajar.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface daftarHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(daftar: historyBarang)

    @Query("UPDATE historyBarang SET tanggal=:isi_tanggal, item=:isi_item, jumlah=:isi_jumlah, status=:isi_status " +
            "WHERE id=:pilihid")
    fun update(isi_tanggal: String, isi_item: String, isi_jumlah: String, isi_status: Int,
               pilihid: Int)

    @Query("SELECT * FROM historyBarang WHERE id=:isi_id")
    suspend fun getItem(isi_id : Int) : daftarBelanja

    @Delete
    fun delete (daftar: historyBarang)

    @Query("SELECT * FROM historyBarang ORDER BY id asc")
    fun selectAll(): MutableList<historyBarang>
}