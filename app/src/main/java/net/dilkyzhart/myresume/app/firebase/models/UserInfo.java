package net.dilkyzhart.myresume.app.firebase.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by dilky on 2017. 7. 2..
 */
@IgnoreExtraProperties
public class UserInfo {
    public String name;
    public String email;
    public String reg_timestamp;
    public String uuid;
    //public String type; // google, facebook, custom 등을 구분

    public UserInfo(String uuid, String email, String name) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    private static DatabaseReference userRef;
    public static DatabaseReference getReferenceOfProfile() {
        if (userRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference("users");
        }
        return userRef;
    }

    private static void newDatabaseInstance() {
        getReferenceOfProfile();
    }

    public static void AddUserWithGoogle(UserInfo userInfo) {
        newDatabaseInstance();

        userRef.child("google").child(userInfo.uuid).child("email").setValue(userInfo.email);
        userRef.child("google").child(userInfo.uuid).child("name").setValue(userInfo.name);
        userRef.child("google").child(userInfo.uuid).child("reg_timestamp").setValue(userInfo.reg_timestamp);
    }
}
