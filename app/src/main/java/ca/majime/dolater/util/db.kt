package ca.majime.dolater.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "dolater") {

    companion object {
        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Reminder", true,
                "_id" to INTEGER + PRIMARY_KEY,
                "text" to TEXT,
                "date" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)