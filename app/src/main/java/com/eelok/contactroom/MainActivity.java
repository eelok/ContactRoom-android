package com.eelok.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.eelok.contactroom.model.Contact;
import com.eelok.contactroom.model.ContactViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;

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
                contacts.forEach(contact ->
                        Log.d("TAG", "onCreate " + contacts.get(0).getName())
                );
            }
        });
    }
}