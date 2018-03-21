package ca.majime.dolater

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ca.majime.dolater.util.database
import org.jetbrains.anko.db.select

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initData()
    }

    private fun initData() {
        database.use {
            select("Reminder").exec {
                Log.d("List", "what? ${this}")
            }
        }
    }
}
