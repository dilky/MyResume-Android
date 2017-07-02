package net.dilkyzhart.myresume.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;
import net.dilkyzhart.myresume.app.firebase.models.MyProfile;

/**
 * Created by dilky on 2017. 6. 29..
 */

public class MyProfileFragment extends Fragment {

    private final String PHOTO_URL = "https://firebasestorage.googleapis.com/v0/b/myresume-android.appspot.com/o/profile_photo.png?alt=media&token=46d2f063-15d0-4a5c-a08d-6d5271016ce0";

    public static MyProfileFragment newInstance() {
        MyProfileFragment f = new MyProfileFragment();

        // Supply index input as an argument.
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        f.setArguments(args);

        return f;
    }

    private View contentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_myprofile, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        MyProfile.Read(new ReceiveValueListener() {
            @Override
            public void onDataReceive(Object data) {

                if (!(data instanceof MyProfile))
                    return;

                MyProfile myProfile = (MyProfile)data;

                final ImageView iv_MyPhoto = (ImageView) contentView.findViewById(R.id.iv_MyPhoto);
                Glide.with(getActivity()).load(PHOTO_URL).into(iv_MyPhoto);

                TextView tvName = (TextView) contentView.findViewById(R.id.tv_Name);
                TextView tvBirthdate = (TextView) contentView.findViewById(R.id.tv_Birthdate);
                TextView tvGender = (TextView) contentView.findViewById(R.id.tv_Gender);
                TextView tvCellphoneNo = (TextView) contentView.findViewById(R.id.tv_CellphoneNo);
                TextView tvEmail = (TextView) contentView.findViewById(R.id.tv_Email);
                TextView tvAddress = (TextView) contentView.findViewById(R.id.tv_Address);

                tvName.setText(myProfile.getName());

                tvBirthdate.setText(myProfile.getBirthDate());
                tvGender.setText(myProfile.getGender());
                tvCellphoneNo.setText(myProfile.getCellPhoneNo());
                tvEmail.setText(myProfile.getEmail());
                tvAddress.setText(myProfile.getAddress());
            }
        });
    }

}
