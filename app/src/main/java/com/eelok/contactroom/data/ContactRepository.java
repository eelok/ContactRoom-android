package com.eelok.contactroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.eelok.contactroom.model.Contact;
import com.eelok.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private LiveData<List<Contact>> allContacts;
    private ContactDao contactDao;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData(){
        return allContacts;
    }

    public void insert(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }
}
