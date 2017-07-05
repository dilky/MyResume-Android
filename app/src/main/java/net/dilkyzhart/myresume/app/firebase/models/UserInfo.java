package net.dilkyzhart.myresume.app.firebase.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by dilky on 2017. 7. 2..
 */
@IgnoreExtraProperties
public class UserInfo {
    public String name;
    public String email;
    public String reg_date;
    public String id;
    public String type; // google, facebook, custom 등을 구분
}
