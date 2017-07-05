package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.comm.LoginSession;
import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;

import java.sql.Timestamp;

/**
 * Created by dilky on 2017. 7. 5..
 *
 *  포스트 반응에 대한 데이터 모델 샘플
 * "post-feedback" : {
     "@post-key" : {
         "post_likes" : {
             "@user_id" : {"timestamp":"1459361875337"},
             "@user_id" : {"timestamp":"1459361875337"}
        },
        "post_comment" : {
             "@user_id" : {"timestamp":"1459361875337", "comment":"안녕하세요", "writer_name":"최은경"},
             "@user_id" : {"timestamp":"1459361875337", "comment":"추천합니ㄷ", "writer_name":"최은경"}
        }
     }
 }
 */

public class PostFeecback {

    private static final String KEY_POST_LIKE = "post-likes";

    private static DatabaseReference postFeedback;
    public static DatabaseReference getReferenceOfProfile() {
        if (postFeedback == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            postFeedback = database.getReference("post-feedback");
        }
        return postFeedback;
    }

    private static void newDatabaseInstance() {
        getReferenceOfProfile();
    }

    public static class PostLikes {
        public boolean clickable;
        public long countLikes;
    }

    public static void LikeToIncrease(String postKey) {
        newDatabaseInstance();
        if (LoginSession.getInstance().isLogin()) {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            postFeedback.child(postKey).child(KEY_POST_LIKE).child(LoginSession.getInstance().getUserUUID()).child("timestamp").setValue(timestamp.getTime());
        }
    }

    /** 해당하는 포스트의 좋아요 정보를 읽어온다 */
    public static void ReadLikes(final String postKey, final ReceiveValueListener listener) {
        newDatabaseInstance();

        postFeedback.child(postKey).child(KEY_POST_LIKE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostLikes postLikes = new PostLikes();
                postLikes.countLikes = dataSnapshot.getChildrenCount();
                postLikes.clickable = false;
                if (LoginSession.getInstance().isLogin()) {
                    postLikes.clickable = !dataSnapshot.hasChild(LoginSession.getInstance().getUserUUID());
                }

                Log.d("dilky", postKey + " clickable :" + postLikes.clickable);

                if (listener != null) {
                    listener.onDataReceive(postLikes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
