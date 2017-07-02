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

    private String Name;
    private String BirthDate;
    private String Gender;
    private String CellPhoneNo;
    private String Email;
    private String Address;

    public String getName() {
        return Name;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public String getGender() {
        return Gender;
    }

    public String getCellPhoneNo() {
        return CellPhoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public static class Builder {
        private static MyProfile builder;
        public Builder() {
            builder = new MyProfile();
        }

        public Builder setName(String name) {
            builder.Name = name;
            return this;
        }

        public Builder setBirthDate(String birthdate) {
            builder.BirthDate = birthdate;
            return this;
        }

        public Builder setGender(String gender) {
            builder.Gender = gender;
            return this;
        }

        public Builder setCellPhoneNo(String cellphoneno) {
            builder.CellPhoneNo = cellphoneno;
            return this;
        }

        public Builder setEmail(String email) {
            builder.Email = email;
            return this;
        }

        public Builder setAddress(String address) {
            builder.Address = address;
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

    public static void Write(MyProfile profileInfo) {
        // Write a message to the database
        newDatabaseInstance();

        setProfileValue("name", profileInfo.getName());
        setProfileValue("birthdate", profileInfo.getBirthDate());
        setProfileValue("gender", profileInfo.getGender());
        setProfileValue("cellphone", profileInfo.getCellPhoneNo());
        setProfileValue("email", profileInfo.getEmail());
        setProfileValue("address", profileInfo.getAddress());

    }

    public static void Read(final ReceiveValueListener listener) {
        newDatabaseInstance();

        // Read from the database
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                MyProfile profile = new MyProfile();
                profile.Name = dataSnapshot.child("name").getValue(String.class);
                profile.BirthDate = dataSnapshot.child("birthdate").getValue(String.class);
                profile.Gender = dataSnapshot.child("gender").getValue(String.class);
                profile.CellPhoneNo = dataSnapshot.child("cellphone").getValue(String.class);
                profile.Email = dataSnapshot.child("email").getValue(String.class);
                profile.Address = dataSnapshot.child("address").getValue(String.class);

                if (listener != null)
                    listener.onDataReceive(profile);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dilky", "Failed to read value.", error.toException());
            }
        });
    }

}
