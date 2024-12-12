package paba.belajar.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import paba.belajar.room.database.daftarBelanja
import paba.belajar.room.database.daftarBelanjaDB
import paba.belajar.room.database.historyBarang
import paba.belajar.room.database.historyBarangDB

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = daftarBelanjaDB.getDatabase(this)
        DB2 = historyBarangDB.getDatabase(this)
        adapterDaftar = adapterDaftar(arDaftar)

        val _rvDaftar = findViewById<RecyclerView>(R.id.rvDaftarBelanja)
        _rvDaftar.layoutManager = LinearLayoutManager(this)
        _rvDaftar.adapter = adapterDaftar

        adapterDaftar.setOnItemClickCallback(
            object : adapterDaftar.OnItemClickCallback {
                override fun delData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.fundaftarBelanjaDAO().delete(dtBelanja)
                        val daftar = DB.fundaftarBelanjaDAO().selectAll()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

                override fun finishData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {


                        // Create a new historyBarang object based on the dtBelanja (this depends on your entity's structure)
                        val historyItem = historyBarang(
                            id = dtBelanja.id,
                            tanggal = dtBelanja.tanggal,
                            item = dtBelanja.item,
                            jumlah = dtBelanja.jumlah,
                            status = dtBelanja.status
                        )

                        // Insert the new item into the historyBarangDB
                        DB2.fundaftarHistoryDAO().insert(historyItem)

                        // After inserting, delete the item from daftarBelanjaDB
                        DB.fundaftarBelanjaDAO().delete(dtBelanja)
                        val updatedList = DB.fundaftarBelanjaDAO().selectAll()

                        // Update the UI with the new list
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(updatedList)
                        }
                    }
                }
            }
        )

        val _fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }
    }

    private lateinit var DB : daftarBelanjaDB
    private lateinit var DB2 : historyBarangDB

    private lateinit var adapterDaftar: adapterDaftar
    private var arDaftar : MutableList<daftarBelanja> = mutableListOf()

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarBelanja = DB.fundaftarBelanjaDAO().selectAll()
            Log.d("data ROOM", daftarBelanja.toString())
            adapterDaftar.isiData(daftarBelanja)
        }
    }
}