package net.dilkyzhart.myresume.app.firebase.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;

/**
 * Created by dilky on 2017. 7. 2..
 */

public class AdminInfo {

    private static DatabaseReference adminUser;
    public static DatabaseReference getReferenceOfProfile() {
        if (adminUser == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            adminUser = database.getReference("admin_user");
        }
        return adminUser;
    }

    private static void newDatabaseInstance() {
        getReferenceOfProfile();
    }

    public static void Read(final ReceiveValueListener listener) {
        newDatabaseInstance();

        // Read from the database
        adminUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listener != null) {
                    listener.onDataReceive(dataSnapshot.getValue(AdminInfo.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dilky", "Failed to read value.", error.toException());
            }
        });
    }


    @PropertyName("email")
    public String adminEmail;
    @PropertyName("id")
    public String adminId;

    private boolean isAdmin;

    public void setAdminUser(String email) {
        try {
            isAdmin = email.equals(getInstance().adminEmail);
        } catch (Exception e) {
            e.printStackTrace();
            isAdmin = false;
        }
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private static AdminInfo sInstance;
    public static AdminInfo getInstance() {
        if (sInstance == null) {
            synchronized (AdminInfo.class) {
                if (sInstance == null) {
                    sInstance = new AdminInfo();
                }
            }
        }
        return sInstance;
    }

    public static void clearInstance() {
        sInstance = null;
    }

}
