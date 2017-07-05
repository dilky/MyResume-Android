package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.R;
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

        String key = timelineRef.push().getKey();
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
                    final PostInfo postInfo = ds.getValue(PostInfo.class);
                    postInfo.postKey = ds.getKey();
                    arrayList.add(postInfo);

//                    // TODO : 타이밍 이슈 있음.
//                    PostFeecback.ReadLikes(postInfo.postKey, new ReceiveValueListener() {
//                        @Override
//                        public void onDataReceive(Object data) {
//                            PostFeecback.PostLikes postLikes = (PostFeecback.PostLikes) data;
//                            postInfo.clickable = postLikes.clickable;
//                            postInfo.countLikes = postLikes.countLikes;
//
//                        }
//                    });
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
