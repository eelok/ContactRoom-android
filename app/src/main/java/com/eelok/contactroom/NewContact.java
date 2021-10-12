package com.eelok.contactroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eelok.contactroom.model.Contact;
import com.eelok.contactroom.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {

    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";

    private int contact_id = 0;
    private Boolean isEdit = false;

    private EditText enterName;
    private EditText enterOccupation;
    private Button saveInfoButton;

    private Button updateButton;
    private Button deleteButton;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);


        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveInfoButton = findViewById(R.id.save_button);

        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        contactViewModel = new ViewModelProvider
                .AndroidViewModelFactory(NewContact.this.getApplication())
                .create(ContactViewModel.class);


        if (getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contact_id = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);

            contactViewModel.getContact(contact_id).observe(this, new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    if (contact != null) {
                        enterName.setText(contact.getName());
                        enterOccupation.setText(contact.getOccupation());
                    }
                }
            });

            isEdit = true;
        }

        saveInfoButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (!TextUtils.isEmpty(enterName.getText()) && !TextUtils.isEmpty(enterOccupation.getText())) {
                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION_REPLY, occupation);
                setResult(RESULT_OK, replyIntent);

            } else {
                setResult(RESULT_CANCELED, replyIntent);
            }
            finish();
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterName.getText().toString().trim();
                String occupation = enterOccupation.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)) {
                    Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact();
                    contact.setId(contact_id);
                    contact.setName(name);
                    contact.setOccupation(occupation);
                    ContactViewModel.updateContact(contact);
                    finish();
                }
            }
        });

        if(isEdit){
            saveInfoButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DELETE", "delete btn was clicked");
                String name = enterName.getText().toString().trim();
                String occupation = enterOccupation.getText().toString().trim();

                Contact contact = new Contact();
                contact.setId(contact_id);
                contact.setName(name);
                contact.setOccupation(occupation);
                ContactViewModel.deleteContact(contact);
                finish();
            }
        });

    }
}