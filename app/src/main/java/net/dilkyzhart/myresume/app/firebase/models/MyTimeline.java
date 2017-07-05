package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void writeNewPost(PostInfo post) {

        newDatabaseInstance();

//
//        // Create new post at /user-posts/$userid/$postid and at
//        // /posts/$postid simultaneously
//        String key = timelineRef.child("posts").push().getKey();
//        Post post = new Post(userId, username, title, body);
//        Map<String, Object> postValues = post.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/posts/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
//
//        mDatabase.updateChildren(childUpdates);

        String key = timelineRef.push().getKey();
//        PostInfo post = new PostInfo();
//        post.title = "test";
//        post.period = "20110901 ~ 20170101";
//        post.belong_to = "웹케시";
//        post.rate = "100%";
//        post.description = "afdsfasfdsfsaf 하하핳 ㅋㅋㅋㅋ";

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, post.toMap());

        timelineRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d("dilky", "TEST");
            }
        });

    }

    public static void Read(final ReceiveValueListener listener) {
        newDatabaseInstance();

        // Read from the database ( 정렬하여 가져오기 )
        timelineRef.orderByChild("dk_seq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PostInfo> arrayList = new ArrayList<>();
                for( DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrayList.add(ds.getValue(PostInfo.class));
                }

                if (listener != null)
                    listener.onDataReceive(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dilky", "Failed to read value.", databaseError.toException());
            }
        });

        /**
        // Read from the database
        timelineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                GenericTypeIndicator<ArrayList<PostInfo>> t = new GenericTypeIndicator<ArrayList<PostInfo>>() {};
//                ArrayList<PostInfo> arrayList = dataSnapshot.getValue(t);

                ArrayList<PostInfo> arrayList = new ArrayList<>();
                for( DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrayList.add(ds.getValue(PostInfo.class));
                }

                if (listener != null)
                    listener.onDataReceive(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dilky", "Failed to read value.", error.toException());
            }
        });
         */
    }
}
