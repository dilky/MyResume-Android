package net.dilkyzhart.myresume.app.write;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.dilkyzhart.myresume.app.MyProfileFragment;
import net.dilkyzhart.myresume.app.R;
import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;
import net.dilkyzhart.myresume.app.firebase.models.MyProfile;

import java.io.ByteArrayOutputStream;

/**
 * Created by dilky on 2017. 7. 2..
 */

public class WriteMyProfile extends AppCompatActivity {
    private final int REQCD_GALLERY = 1000;
    private final int REQCD_PERMISSIONS_READ_EXTERNAL_STORAGE = 2000;

    private EditText etName;
    private EditText etBirthdate;
    private EditText etGender;
    private EditText etCellphoneNo;
    private EditText etEmail;
    private EditText etAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_modify_profile);

        getMyProfileInfo();
    }

    /** firebase 서버에서 내 정보를 가져온다 */
    private void getMyProfileInfo() {
        MyProfile.ReadSingleEvent(new ReceiveValueListener() {
            @Override
            public void onDataReceive(Object data) {

                if (!(data instanceof MyProfile))
                    return;

                final MyProfile myProfile = (MyProfile)data;

                final ImageView iv_MyPhoto = (ImageView) findViewById(R.id.iv_MyPhoto);
                if (!TextUtils.isEmpty(myProfile.photo_url) )
                    Glide.with(WriteMyProfile.this).load(myProfile.photo_url).into(iv_MyPhoto);

                etName = (EditText) findViewById(R.id.et_Name);
                etBirthdate = (EditText) findViewById(R.id.et_Birthdate);
                etGender = (EditText) findViewById(R.id.et_Gender);
                etCellphoneNo = (EditText) findViewById(R.id.et_CellphoneNo);
                etEmail = (EditText) findViewById(R.id.et_Email);
                etAddress = (EditText) findViewById(R.id.et_Address);

                etName.setText(myProfile.name);

                etBirthdate.setText(myProfile.birthdate);
                etGender.setText(myProfile.gender);
                etCellphoneNo.setText(myProfile.cellphone);
                etEmail.setText(myProfile.email);
                etAddress.setText(myProfile.address);
            }
        });
    }

    @Override
    public void finish() {
        writeMyProfileInfo();
        super.finish();
    }

    /** firebase 서버에서 내 정보를 저장한다 */
    private void writeMyProfileInfo() {

        MyProfile.Write(new MyProfile.Builder()
                             .setName(etName.getText().toString())
                             .setBirthDate(etBirthdate.getText().toString())
                             .setGender(etGender.getText().toString())
                             .setCellPhoneNo(etCellphoneNo.getText().toString())
                             .setEmail(etEmail.getText().toString())
                             .setAddress(etAddress.getText().toString())
                             .build()
        );

    }

    /** 내 프로필 사진 클리 */
    public void onSelectMyPhotoClick(View view) {

        // SD카드 읽기 권한 체크
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // 사용자에게 해당 권한이 필요한 이유에 대해 설명하고 requestPermissions 을 호출한다.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQCD_PERMISSIONS_READ_EXTERNAL_STORAGE);
            } else {

                // 권한허용 팝업에서 "다시묻지않기" 체크를 선택한 경우
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQCD_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        } else {
            // 이미 권한이 혀용된 경우
            startActivityWithImageSelect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQCD_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 필요한 권한 획득시
                    startActivityWithImageSelect();
                } else {
                    Toast.makeText(this, "권한을 허용하지 않을 경우 사진을 변경할 수 없습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    private void startActivityWithImageSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQCD_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQCD_GALLERY:
                    SendPicture(data); //갤러리에서 가져오기
                    break;
                default:
                    break;
            }
        }
    }

    private void SendPicture(Intent data) {

        Uri imgUri = data.getData();
        String imagePath = getRealPathFromURI(imgUri); // path 경로

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환

        // upload to firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://myresume-android.appspot.com/"); // Create a storage reference from my app
        StorageReference myphotoRef = storageRef.child("myphoto.jpg");              // Create a reference to "myphoto.jpg"
        StorageReference myphotoImagesRef = storageRef.child("images/myphoto.jpg"); // Create a reference to 'images/myphoto.jpg'

        // While the file names are the same, the references point to different files
        myphotoRef.getName().equals(myphotoImagesRef.getName());    // true
        myphotoRef.getPath().equals(myphotoImagesRef.getPath());    // false

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteData = baos.toByteArray();

        UploadTask uploadTask = myphotoRef.putBytes(byteData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                StringBuilder sbr = new StringBuilder();
                sbr.append(downloadUrl.getScheme());
                sbr.append("://");
                sbr.append(downloadUrl.getHost());
                sbr.append(downloadUrl.getPath());
                sbr.append("?");
                sbr.append(downloadUrl.getQuery());

                // 변경된 프로필 사진의 경로를 Firebase 디비에 저장한다.
                MyProfile.updateMyPhotoUrl(sbr.toString());
                Log.d("dilky", sbr.toString());

                ImageView ivMyPhoto = (ImageView) findViewById(R.id.iv_MyPhoto);
                Glide.with(WriteMyProfile.this).load(sbr.toString()).into(ivMyPhoto);
            }
        });
    }

    /**
     * 사진의 Uri 를 통해 절대경로 구한다.
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
