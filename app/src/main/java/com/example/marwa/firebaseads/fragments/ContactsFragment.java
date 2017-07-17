package com.example.marwa.firebaseads.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.marwa.firebaseads.R;
import com.example.marwa.firebaseads.model.SelectUser;
import com.example.marwa.firebaseads.adapter.SelectUserAdapter;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ContactsFragment extends Fragment {

    ArrayList<SelectUser> selectUsers;
    //Contact name list
    ArrayList<String> contactNames;

    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones;

    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        selectUsers = new ArrayList<SelectUser>();
        contactNames = new ArrayList<String>();

        resolver = getActivity().getContentResolver();
        listView = (ListView) v.findViewById(R.id.contacts_list);

        SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        int data = prefs.getInt("data", 0);
        Log.e("permission contact", Integer.toString(data));

        if (data == 1) {
            phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        } else {
            Toast.makeText(getActivity(), "Read Contacts permission denied", Toast.LENGTH_LONG).show();
        }
        search = (SearchView) v.findViewById(R.id.searchView);

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });
        return v;
    }

    //-------------------------------------------- Async task -------------------------------------------//
    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //--------------------------------------- do in background ------------------------------------------//
        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(getContext(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    try {
                        if (image_thumb != null) {
                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                        } else {
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SelectUser selectUser = new SelectUser();
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    //check for repeated names
                    if (!contactNames.contains(selectUser.getName())) {
                        contactNames.add(selectUser.getName());
                        selectUsers.add(selectUser);
                    }
                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        //----------------------------------------- on pre execute ----------------------------------------//
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(selectUsers, listView.getContext());
            listView.setAdapter(adapter);

            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");
                    SelectUser data = selectUsers.get(i);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getPhone(), null));
                    startActivity(intent);
                }
            });

            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (phones != null) {
            phones.close();
        }
    }


}






