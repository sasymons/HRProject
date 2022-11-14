package com.example.hrproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.addedit.*

class ADDEDIT: Activity(), View.OnClickListener {
    //create DBHandler object
    val dbh = DBHandler(this,null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addedit)
        //set on click listener
        btnSave.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        val extras = intent.extras
        if(extras!=null){
            //read and assign id from intent
            val id:Int = extras.getInt("ID")
            // get contact based on id
            val person = dbh.getPerson(id)
            // edit text populate
            etID.setText(person.id.toString())
            etName.setText(person.name)
            etMobile.setText(person.mobile)
            etAddress.setText(person.address)
            etEmail.setText(person.email)
            etImageFile.setText(person.imageFile)
            ivImage.setImageResource(this.resources.getIdentifier(
                person.imageFile, "drawable",
                "com.example.hrproject"));
        }
    }

    override fun onClick(btn: View) {
        // code to run buttons
        when(btn.id){
            R.id.btnSave-> {
                //save code
                //read value from ID adn put 0 if no value
                val cid: Int = try {
                    etID.text.toString().toInt()
                } catch (e: Exception) {
                    0
                }
                // decide add or update
                if (cid == 0) {
                    addContact()
                } else {
                    editContact(cid)
                }
            }
            R.id.btnCancel ->{
                // cancel code
                goBack()
            }
        }
    }

    private fun editContact(cid: Int) {
    // create contact object and fill new values
        val person = dbh.getPerson(cid)
        person.name = etName.text.toString()
        person.mobile = etMobile.text.toString()
        person.address = etAddress.text.toString()
        person.email = etEmail.text.toString()
        person.imageFile = etImageFile.text.toString()
        // call dbhandler update function
        dbh.updatePerson(person)
        // display confirmation and go to Main Activity
        Toast.makeText(this, "Person has been updated",
        Toast.LENGTH_LONG).show()
        goBack()
    }

    private fun addContact() {
        // read values from edit text and assign to contact object
        val person = Person()
        person.name = etName.text.toString()
        person.mobile = etMobile.text.toString()
        person.address = etAddress.text.toString()
        person.email = etEmail.text.toString()
        person.imageFile = etImageFile.text.toString()

        //call dbhandler function to add
        dbh.addPerson(person)
        // display confirmation
        Toast.makeText(this, "New Person created", Toast.LENGTH_LONG).show()
        goBack()
    }

    private fun goBack() {
        // go back to Main Activity
        val mainact = Intent(this, MainActivity::class.java)
        // to start another activity
        startActivity(mainact)
    }
}
