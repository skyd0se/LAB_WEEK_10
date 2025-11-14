package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()

        val totalRow = db.totalDao().getTotal(ID).firstOrNull()

        totalRow?.let {
            Toast.makeText(
                this,
                "${it.total.date}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onPause() {
        super.onPause()

        val currentObj = viewModel.total.value!!
        val updatedObj = currentObj.copy(
            date = Date().toString()
        )

        db.totalDao().update(
            Total(
                id = ID,
                total = updatedObj
            )
        )
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { obj ->
            updateText(obj.value)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(totalValue: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, totalValue)
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val rows = db.totalDao().getTotal(ID)

        if (rows.isEmpty()) {
            val initialObj = TotalObject(
                value = 0,
                date = Date().toString()
            )
            db.totalDao().insert(
                Total(id = ID, total = initialObj)
            )
            viewModel.setTotal(initialObj)
        } else {
            viewModel.setTotal(rows.first().total)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}
