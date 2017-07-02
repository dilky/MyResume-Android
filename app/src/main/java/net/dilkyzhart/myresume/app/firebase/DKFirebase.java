package net.dilkyzhart.myresume.app.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dilky on 2017. 6. 27..
 */

public class DKFirebase {

    static final String TAG = DKFirebase.class.getSimpleName();

    static DatabaseReference myRef;

    public static DatabaseReference getDatabaseReferenceInstance() {
        if (myRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("");
        }
        return myRef;
    }

    public static DatabaseReference getDRInstanceOfProfile() {
        if (myRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("profile");
        }
        return myRef;
    }

    public static DatabaseReference getDRInstanceOfLicensed() {
        if (myRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("licensed");
        }
        return myRef;
    }

    public static DatabaseReference getDRInstanceOfTimeline() {
        if (myRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("timeline");
        }
        return myRef;
    }

    public static void simpleWrite() {
        // Write a message to the database
        getDatabaseReferenceInstance();

        myRef.setValue("Hello, World!");
    }

    public static void simpleRead() {
        getDatabaseReferenceInstance();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
