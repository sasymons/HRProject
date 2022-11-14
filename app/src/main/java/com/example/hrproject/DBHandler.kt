package com.example.hrproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context, factory: SQLiteDatabase.CursorFactory?)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    private val TableName:String = "Person"
    private val KeyID:String = "ID"
    private val KeyName:String = "NAME"
    private val KeyMobile:String = "MOBILE"
    private val KeyEmail:String = "EMAIL"
    private val KeyAddress:String = "ADDRESS"
    private val KeyImageFile:String = "IMAGEFILE"

    companion object{
        const val DATABASE_NAME = "HRManager.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        //create sql statement for table
        val createTable:String = "CREATE TABLE $TableName" +
                "($KeyID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KeyName TEXT, $KeyImageFile TEXT, $KeyAddress TEXT, " +
                "$KeyMobile TEXT, $KeyEmail TEXT)"
        // execute sql
        db.execSQL(createTable)
        // add one sample record using ContentValue object
        val cv = ContentValues()
        cv.put(KeyName,"Scott Symons")
        cv.put(KeyMobile,"123456789")
        cv.put(KeyAddress,"Sydney")
        cv.put(KeyImageFile,"first")
        cv.put(KeyEmail,"Scott.Symons1@studytafensw.edu.au")
        // use insert method
        db.insert(TableName, null, cv)

    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        //drop existing table
        db.execSQL("DROP TABLE IF EXISTS $TableName")
        //recreate database
        onCreate(db)
    }

    //get all the records from database
    fun getAllPersons():ArrayList<Person>{
        //declare a arraylist to fill all records in contact object
        val personList = ArrayList<Person>()
        // create a sql query
        val selectQuery:String = "SELECT * FROM $TableName"
        // get database for readable
        val db = this.readableDatabase
        //run query and put result in cursor
        val cursor = db.rawQuery(selectQuery, null)
        // move through cursor and read all the records
        if(cursor.moveToFirst()){
            //loop to read all possible records
            do{
                //create a contact object
                val person = Person()
                //read values from cursor and fill contact object
                person.id = cursor.getInt(0)
                person.name = cursor.getString(1)
                person.imageFile = cursor.getString(2)
                person.address = cursor.getString(3)
                person.mobile = cursor.getString(4)
                person.email = cursor.getString(5)
                //add contact to object arraylist
                personList.add(person)
            }while(cursor.moveToNext())
        }

        //close cursor and database
        cursor.close()
        db.close()
        //return arrayList of contact object
        return personList
    }

    // add new record to DB
    fun addPerson(person: Person) {
        //get writable db
        val db = this.writableDatabase
        //create content value object
        val cv = ContentValues()
        cv.put(KeyName,person.name)
        cv.put(KeyImageFile,person.imageFile)
        cv.put(KeyAddress,person.address)
        cv.put(KeyMobile,person.mobile)
        cv.put(KeyEmail,person.email)

        //use insert method
        db.insert(TableName,null,cv)
        //close db
        db.close()
    }

    fun deletePerson(id: Int) {
        // get writable db
        val db = this.writableDatabase
        db.delete(TableName,"$KeyID=?", arrayOf(id.toString()))
        // close db
        db.close()
    }

    fun getPerson(id: Int): Person {
        //get readable db
        val db = this.readableDatabase
        //create contact object to fill data
        val person = Person()
        // create cursor based on query
        val cursor = db.query(TableName,
        arrayOf(KeyID,KeyName,KeyImageFile,KeyAddress,KeyMobile,KeyEmail),
        "$KeyID=?",
            arrayOf(id.toString()),
            null,
            null,
            null)
        // check cursor and read value and put in contact
        if(cursor!=null){
            cursor.moveToFirst()
            person.id = cursor.getInt(0)
            person.name = cursor.getString(1)
            person.imageFile = cursor.getString(2)
            person.address = cursor.getString(3)
            person.mobile = cursor.getString(4)
            person.email = cursor.getString(5)
        }
        // close cursor and db
        cursor.close()
        db.close()
        return person
    }

    fun updatePerson(person: Person) {
        //get writable db
        val db = this.writableDatabase
        // create content value and put values from conact object
        val cv = ContentValues()
        cv.put(KeyName,person.name)
        cv.put(KeyImageFile,person.imageFile)
        cv.put(KeyAddress,person.address)
        cv.put(KeyMobile,person.mobile)
        cv.put(KeyEmail,person.email)
        val id:Int = db.update(TableName,
            cv,
            "$KeyID=?",
            arrayOf(person.id.toString()))
        //close db
        db.close()
    }

}