package com.shield.resident.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shield.resident.Adapter.ContactListAdapter;
import com.shield.resident.GlobalClass.GlobalClass;
import com.shield.resident.GlobalClass.Shared_Preference;
import com.shield.resident.R;
import com.shield.resident.dialogs.LoaderDialog;
import com.shield.resident.model.Contact;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        ContactListAdapter.ViewClickListener {


    GlobalClass globalClass;
    Shared_Preference prefrence;

    ImageView iv_back;
    SearchView searchView;
    RecyclerView recycler_view;

    ArrayList<Contact> contactArrayList;
    LoaderDialog loaderDialog;
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_activity);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(ContactList.this);

        loaderDialog = new LoaderDialog(this, android.R.style.Theme_Translucent,
                false, "");

        iv_back = findViewById(R.id.iv_back);
        searchView = findViewById(R.id.searchView);
        recycler_view = findViewById(R.id.recycler_view);

        searchView.setQueryHint("Search here");
        searchView.setSubmitButtonEnabled(true);


        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        contactArrayList = new ArrayList<>();


        loaderDialog.show();
        new LongAsync().execute();


        iv_back.setOnClickListener(v -> {
            finish();
        });


    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //The framework will take care of closing the
        // old cursor once we return.
      //  List<String> contacts = contactsFromCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
    }


    class LongAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void...arg0) {

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {

                while (cur != null && cur.moveToNext()) {

                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
                        String[] argument = new String[]{ id };

                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, selection, argument, null);

                        while (pCur.moveToNext()) {

                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            String photo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                              Log.i("TAG", "Name: " + name);
                              Log.i("TAG", "Phone Number: " + phoneNo);
                            // Log.i("TAG", "photo: " + photo);


                            Contact contact = new Contact();
                            contact.setName(name);
                            contact.setImage(photo);

                            if (phoneNo.length() > 10){

                                String sss = phoneNo.trim();
                                sss = sss.replaceAll(" ", "");
                                sss = sss.replaceAll("\\s","");
                                sss = sss.replaceAll("-", "");

                                sss = sss.substring(sss.length() - 10);

                                contact.setMobile(sss);

                                contactArrayList.add(contact);

                            }else if (phoneNo.length() == 10){

                                contact.setMobile(phoneNo);

                                contactArrayList.add(contact);

                            }else {

                                contact.setMobile("");
                            }


                        }
                        pCur.close();
                    }
                }
            }
            if(cur!=null){
                cur.close();
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            ArrayList<Contact> list = removeDuplicates(contactArrayList);

            loaderDialog.dismiss();

            Log.i("TAG", "length: " + list.size());

            contactListAdapter = new ContactListAdapter(ContactList.this, list);
            recycler_view.setAdapter(contactListAdapter);
            contactListAdapter.setViewClickListener(ContactList.this);


            searchViewInit();
        }
    }



    public ArrayList<Contact> removeDuplicates(ArrayList<Contact> list) {

        ArrayList<Contact> newList = new ArrayList<Contact>();

        for (Contact element : list) {

            if (!isExits(element, newList)) {
                newList.add(element);
            }
        }

        return newList;
    }

    public boolean isExits(Contact element, ArrayList<Contact> newList){

        boolean is = false;

        for (Contact element2 : newList){

            if (element2.getMobile().equals(element.getMobile())) {
                is = true;
                break;
            }
        }

        return is;
    }


    public void searchViewInit(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query != null && query.length() > 0){
                    contactListAdapter.getFilter().filter(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && newText.length() > 0){
                    contactListAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });
    }


    @Override
    public void onItemClicked(Contact contact) {

        Intent intent = new Intent();
        intent.putExtra("name", contact.getName());
        intent.putExtra("number", contact.getMobile());
        setResult(RESULT_OK, intent);
        finish();

    }
}
