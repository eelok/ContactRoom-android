package com.eelok.contactroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eelok.contactroom.model.Contact;
import com.eelok.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactViewModel = new ViewModelProvider
                .AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(MainActivity.this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                StringBuilder stringBuilder = new StringBuilder();

                for (Contact contact : contacts) {
                    stringBuilder.append(" - ").append(contact.getName()).append(" ").append(contact.getOccupation());
                    Log.d("TAG", "onCreate " + contact.getName());
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_contact_fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewContact.class);

                startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

            Contact contact = new Contact(name, occupation);

            ContactViewModel.insert(contact);
            Log.d("TAGs", "onActivityResult: " + data.getStringArrayExtra(NewContact.NAME_REPLY));
            Log.d("TAGs", "onActivityResult: " + data.getStringArrayExtra(NewContact.OCCUPATION_REPLY));
        }
    }
}