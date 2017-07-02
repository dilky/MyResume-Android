package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;

import java.util.ArrayList;

/**
 * Created by dilky on 2017. 6. 28..
 * 경력 및 학력
 */

public class MyTimeline {

    private static DatabaseReference timelineRef;
    public static DatabaseReference getReferenceOfProfile() {
        if (timelineRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            timelineRef = database.getReference("timeline");
        }
        return timelineRef;
    }

    private static void newDatabaseInstance() {
        getReferenceOfProfile();
    }

    public static void Write(PostInfo timeline) {

    }

    public static void Read(final ReceiveValueListener listener) {
        newDatabaseInstance();

        // Read from the database
        timelineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<PostInfo>> t = new GenericTypeIndicator<ArrayList<PostInfo>>() {};
                ArrayList<PostInfo> arrayList = dataSnapshot.getValue(t);


                if (listener != null)
                    listener.onDataReceive(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dilky", "Failed to read value.", error.toException());
            }
        });
    }
}
