package net.dilkyzhart.myresume.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

    public static MyProfileFragment newInstance() {
        MyProfileFragment f = new MyProfileFragment();
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

                final MyProfile myProfile = (MyProfile)data;

                final ImageView iv_MyPhoto = (ImageView) contentView.findViewById(R.id.iv_MyPhoto);
                if (!TextUtils.isEmpty(myProfile.photo_url) )
                    Glide.with(MyProfileFragment.this).load(myProfile.photo_url).into(iv_MyPhoto);

                TextView tvName = (TextView) contentView.findViewById(R.id.tv_Name);
                TextView tvBirthdate = (TextView) contentView.findViewById(R.id.tv_Birthdate);
                TextView tvGender = (TextView) contentView.findViewById(R.id.tv_Gender);
                TextView tvCellphoneNo = (TextView) contentView.findViewById(R.id.tv_CellphoneNo);
                TextView tvEmail = (TextView) contentView.findViewById(R.id.tv_Email);
                TextView tvAddress = (TextView) contentView.findViewById(R.id.tv_Address);

                tvName.setText(myProfile.name);

                tvBirthdate.setText(myProfile.birthdate);
                tvGender.setText(myProfile.gender);
                tvCellphoneNo.setText(myProfile.cellphone);
                tvEmail.setText(myProfile.email);
                tvAddress.setText(myProfile.address);

            }
        });
    }

}
