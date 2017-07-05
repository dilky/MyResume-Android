package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;

/**
 * Created by dilky on 2017. 6. 28..
 * 개인 프로필
 */
public class MyProfile {

    public String name;
    public String birthdate;
    public String gender;
    public String cellphone;
    public String email;
    public String address;
    public String photo_url;

    public static class Builder {
        private static MyProfile builder;
        public Builder() {
            builder = new MyProfile();
        }

        public Builder setName(String name) {
            builder.name = name;
            return this;
        }

        public Builder setBirthDate(String birthdate) {
            builder.birthdate = birthdate;
            return this;
        }

        public Builder setGender(String gender) {
            builder.gender = gender;
            return this;
        }

        public Builder setCellPhoneNo(String cellphoneno) {
            builder.cellphone = cellphoneno;
            return this;
        }

        public Builder setEmail(String email) {
            builder.email = email;
            return this;
        }

        public Builder setAddress(String address) {
            builder.address = address;
            return this;
        }

        public MyProfile build() {
            return builder;
        }
    }

    private static DatabaseReference profileRef;
    public static DatabaseReference getReferenceOfProfile() {
        if (profileRef == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            profileRef = database.getReference("profile");
        }
        return profileRef;
    }

    private static void newDatabaseInstance() {
        getReferenceOfProfile();
    }

    private static void setProfileValue(String key, String value) {
        if (value == null)
            return;
        profileRef.child(key).setValue(value);
    }

    /** 내 프로필 사진을 변경한다 */
    public static void updateMyPhotoUrl(String url) {
        newDatabaseInstance();

        profileRef.child("photo_url").setValue(url);
    }

    public static void Write(MyProfile profileInfo) {
        // Write a message to the database
        newDatabaseInstance();

        setProfileValue("name", profileInfo.name);
        setProfileValue("birthdate", profileInfo.birthdate);
        setProfileValue("gender", profileInfo.gender);
        setProfileValue("cellphone", profileInfo.cellphone);
        setProfileValue("email", profileInfo.email);
        setProfileValue("address", profileInfo.address);
    }

    public static void Read(final ReceiveValueListener listener) {
        newDatabaseInstance();

        profileRef.addValueEventListener(new CustomValueEventListener(listener));

//        // Read from the database
//        profileRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                MyProfile profile = dataSnapshot.getValue(MyProfile.class);
//                if (listener != null)
//                    listener.onDataReceive(profile);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("dilky", "Failed to read value.", error.toException());
//            }
//        });
    }

    public static void ReadSingleEvent(final ReceiveValueListener listener) {
        newDatabaseInstance();

        // Read from the database
        profileRef.addListenerForSingleValueEvent(new CustomValueEventListener(listener));
    }


    static class CustomValueEventListener implements ValueEventListener {

        ReceiveValueListener listener;
        public CustomValueEventListener(ReceiveValueListener listener) {
            this.listener = listener;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            MyProfile profile = dataSnapshot.getValue(MyProfile.class);
            if (listener != null)
                listener.onDataReceive(profile);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w("dilky", "Failed to read value.", error.toException());
        }
    }

}
