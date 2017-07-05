package net.dilkyzhart.myresume.app.write;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import net.dilkyzhart.myresume.app.R;
import net.dilkyzhart.myresume.app.firebase.models.MyTimeline;
import net.dilkyzhart.myresume.app.firebase.models.PostInfo;

/**
 * Created by dilky on 2017. 7. 4..
 * 프로젝트 또는 재직, 학력 정보를 타임라인에 등록한다.
 */

public class WriteCareer extends AppCompatActivity {
    EditText etTitle, etPeriod, etBelongto, etRate, etSeq, etDescription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_career);

        etTitle = (EditText) findViewById(R.id.et_Title);
        etPeriod = (EditText) findViewById(R.id.et_Period);
        etBelongto = (EditText) findViewById(R.id.et_BelongTo);
        etRate = (EditText) findViewById(R.id.et_Rate);
        etSeq  = (EditText) findViewById(R.id.et_Seq);
        etDescription = (EditText) findViewById(R.id.et_Description);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 타임라인에 등록하기
                PostInfo post = new PostInfo();
                post.title = etTitle.getText().toString();
                post.period = etPeriod.getText().toString();
                post.belong_to = etBelongto.getText().toString();
                post.rate = etRate.getText().toString();
                post.seq = TextUtils.isEmpty(etSeq.getText().toString()) ? 0 : Integer.parseInt(etSeq.getText().toString());
                post.description = etDescription.getText().toString();

                MyTimeline.writeNewPost(post);
                finish();
            }
        });

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
