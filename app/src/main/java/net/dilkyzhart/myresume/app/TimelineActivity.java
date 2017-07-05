package net.dilkyzhart.myresume.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.dilkyzhart.myresume.app.comm.LoginSession;
import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;
import net.dilkyzhart.myresume.app.firebase.models.AdminInfo;
import net.dilkyzhart.myresume.app.firebase.models.UserInfo;
import net.dilkyzhart.myresume.app.write.WriteCareer;
import net.dilkyzhart.myresume.app.write.WriteMyProfile;

public class TimelineActivity extends AppCompatActivity {

    public static final int REQ_SIGN_CD_GOOGLE = 2001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient googleApiClient;
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    private final String TAG = this.getClass().getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_left:
                    // Instantiate a new fragment.
                    replaceFragment(MyProfileFragment.newInstance());
                    return true;
                case R.id.navigation_home:
                    replaceFragment(TimelineFragment.newInstance());
                    return true;
                case R.id.navigation_right:
                    return true;
            }
            return false;
        }
    };

    private void showAuthDialogFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        AuthDialogFragment fragment = AuthDialogFragment.newInstance();
        fragment.setCancelable(false);
        fragment.show(ft, "dialog");
    }

    private void hideAuthDialogFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commit();
    }

    private void replaceFragment(Fragment newFragment) {

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fl_Container, newFragment, "CONTENTS");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        setFirebaseAuthListener();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Log.d("dilky", "default_web_client_id:" + getString(R.string.default_web_client_id));

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleConnectFailedListener())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        replaceFragment(new MyProfileFragment());
    }

    /** 구글 API 클라이언트 연결에 실패한 경우 리스너 */
    class GoogleConnectFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
        }
    }

    private Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_profile :
                // 내 프로필 수정하기
                startActivity(new Intent(this, WriteMyProfile.class));
                break;
            case R.id.add_career :
                // 경력 등록하기
                startActivity(new Intent(this, WriteCareer.class));
                Toast.makeText(this, "경력 수정하기...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout :
                signOut();
                break;
            case R.id.login :
                //showAuthDialogFragment();
                signIn();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /** 로그인, 로그아웃, 어드민 사용자의 메뉴를 보여준다 */
    private void updateLoginUI() {

        mMenu.findItem(R.id.modify_profile).setVisible(AdminInfo.getInstance().isAdmin());
        mMenu.findItem(R.id.add_career).setVisible(AdminInfo.getInstance().isAdmin());

        mMenu.findItem(R.id.login).setVisible(!LoginSession.getInstance().isLogin());
        mMenu.findItem(R.id.logout).setVisible(LoginSession.getInstance().isLogin());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        }
//        else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setFirebaseAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void processAuthComplete() {
        hideAuthDialogFragment();
        replaceFragment(new MyProfileFragment());
        updateLoginUI();
    }

    /** 구글계정으로 인증 완료시 처리하는 메서드 */
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(TimelineActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            processAuthComplete();
                        }
                    }
                });
    }


    // [START revokeAccess]
    /** 구글인증 연결 끊기 */
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQ_SIGN_CD_GOOGLE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        LoginSession.clearInstance();
                        AdminInfo.clearInstance();
                        processAuthComplete();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_SIGN_CD_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("dilky", acct.getDisplayName());
            Log.d("dilky", "id : " + acct.getId());
            Log.d("dilky", "id token : " + acct.getIdToken());
            Log.d("dilky", "account : " + acct.getAccount());
            Log.d("dilky", "email : " + acct.getEmail());

            // 로그인 정보 객체 초기화
            LoginSession.getInstance().initialize(acct.getDisplayName(), acct.getEmail(), acct.getId());

            // 사용자 정보 등록
            UserInfo.AddUserWithGoogle(new UserInfo(acct.getId(), acct.getEmail(), acct.getDisplayName()));

            // 어드민 정보 조회하기
            AdminInfo.Read(new ReceiveValueListener() {
                @Override
                public void onDataReceive(Object data) {
                    AdminInfo.getInstance();
                    AdminInfo.getInstance().adminEmail = ((AdminInfo)data).adminEmail;
                    AdminInfo.getInstance().adminId = ((AdminInfo)data).adminId;
                    AdminInfo.getInstance().setAdminUser(acct.getEmail());

                    firebaseAuthWithGoogle(acct);
                }
            });
        } else {
            LoginSession.clearInstance();
            AdminInfo.clearInstance();
            processAuthComplete();
        }
    }

}
