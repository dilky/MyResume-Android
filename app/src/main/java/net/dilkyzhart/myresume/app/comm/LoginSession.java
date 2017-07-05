package net.dilkyzhart.myresume.app.comm;

import net.dilkyzhart.myresume.app.firebase.models.AdminInfo;

/**
 * Created by dilky on 2017. 7. 4..
 * 로그인 사용자에 대한 싱글톤 객체
 */

public class LoginSession {

    private static LoginSession sInstance;
    public static LoginSession getInstance() {
        if (sInstance == null) {
            synchronized (LoginSession.class) {
                if (sInstance == null) {
                    sInstance = new LoginSession();
                }
            }
        }
        return sInstance;
    }

    public static void clearInstance() {
        sInstance = null;
    }

    public void initialize(String name, String email, String uuid) {
        userName = name;
        userEmail = email;
        userUUID = uuid;
        isLogin = true;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public boolean isLogin() {
        return isLogin;
    }

    private String userName;
    private String userEmail;
    private String userUUID;
    private boolean isLogin;


}
