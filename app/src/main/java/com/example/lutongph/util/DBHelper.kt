package com.example.lutongph.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DB_NAME = "LutongPH"
        private const val DATABASE_VERSION = 1
    }

    private val databasePath: String = context.getDatabasePath(DB_NAME).path

    init {
        if (!isDatabaseExists()) {
            try {
                copyDatabaseFromAssets(context)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun isDatabaseExists(): Boolean {
        val dbFile = File(databasePath)
        return dbFile.exists()
    }

    private fun copyDatabaseFromAssets(context: Context) {
        val inputStream: InputStream = context.assets.open(DB_NAME)
        val outputStream = FileOutputStream(databasePath)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }
    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertdata (username:String, password:String, phone:String, email:String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("username",username)
        cv.put("password",password)
        cv.put("phone",phone)
        cv.put("email",email)
        val result = db.insert("USERDATA", null, cv)
        if (result == -1 .toLong()){
            return false
        }
        db.close()
        return true
    }

   fun updatedata (status:String, userID: Int, eventDate: String, eventTime:String) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("Status",status)

       val whereClause = "User_ID = ? AND EventDate = ? AND EventTime = ?"
       val whereArgs = arrayOf(userID.toString(), eventDate, eventTime)

        db.update("calendar", cv, whereClause, whereArgs)
        db.close()
    }

    fun get_foodid(foodname:String):Int?{
        val db = this.readableDatabase
        val fid_cursor = db.rawQuery("SELECT FoodID FROM recipes WHERE FoodName='$foodname'", null)

        var foodId: Int? = null

        if (fid_cursor != null && fid_cursor.moveToFirst()) {
            foodId = fid_cursor.getInt(0)
            fid_cursor.close()
        }

        return foodId
    }

    fun insert_calendardata (ename:String, edate:String, etime:String, status:String, uid:Int, fid:Int?):Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("EventName",ename)
        cv.put("EventDate",edate)
        cv.put("EventTime",etime)
        cv.put("Status",status)
        cv.put("User_ID",uid)
        cv.put("FoodID",fid)
        val result = db.insert("calendar", null, cv)
        if (result == -1 .toLong()){
            return false
        }
        db.close()
        return true
    }

    fun display_calendardata(query:String): Cursor? {
        val db = this.readableDatabase
        val dcd_cursor = db.rawQuery(query, null)
        return dcd_cursor
    }


    fun validation(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM USERDATA WHERE Username='$username' AND Password='$password'"
        val cursor = db.rawQuery(query, null)
        val accountExists = cursor.count > 0
        cursor.close()
        return accountExists
    }

    fun checkAccountExists(username: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM userdata WHERE username='$username'"
        val cursor = db.rawQuery(query, null)
        val accountExists = cursor.count > 0
        cursor.close()
        return accountExists
    }

    fun getID(username:String, password:String): Int {
        val db = this.writableDatabase
        val query = "SELECT * FROM userdata WHERE username='$username' AND password='$password'"
        val cursor = db.rawQuery(query, null)
        var userID = -1
        if (cursor.moveToFirst()) {
            userID = cursor.getInt(0)
        }
        cursor.close()
        return userID
    }

    fun getrecipe(): Cursor? {
        val db = this.readableDatabase
        val r_cursor = db.rawQuery("SELECT * FROM RECIPES", null)
        return r_cursor
    }

    fun getrecipe_filter(query:String): Cursor? {
        val db = this.readableDatabase
        val rf_cursor = db.rawQuery(query, null)
        return rf_cursor
    }

    fun getingredients(): Cursor? {
        val db = this.readableDatabase
        val i_cursor = db.rawQuery("SELECT * FROM INGREDIENTS GROUP BY IngredientName", null)
        return i_cursor
    }

    fun getingredients_specific(choice:String): Cursor? {
        val db = this.readableDatabase
        val si_cursor = db.rawQuery("SELECT IngredientName, IngredientImage FROM INGREDIENTS WHERE IngredientName LIKE '$choice' GROUP BY IngredientName", null)
        return si_cursor
    }

    fun getrecipe_specific(query:String): Cursor? {
        val db = this.readableDatabase
        val sr_cursor = db.rawQuery(query, null)
        return sr_cursor
    }

    fun get_dateID(sql:String): Int{
        val db = this.writableDatabase
        val query = sql
        val cursor = db.rawQuery(query, null)
        var dateID = -1
        if (cursor.moveToFirst()) {
            dateID = cursor.getInt(0)
        }
        cursor.close()
        return dateID
    }
}