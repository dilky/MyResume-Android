package net.dilkyzhart.myresume.app.firebase.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dilky on 2017. 6. 29..
 * Post 데이터 모델
 */
@IgnoreExtraProperties
public class PostInfo {

    @PropertyName("dk_seq")
    public int seq;                // 일련번호
    @PropertyName("dk_title")
    public String title;           // 프로젝트명 (학교명)
    @PropertyName("dk_description")
    public String description;     // 설명 (학교생활)
    @PropertyName("dk_part")
    public String part;            // 개발파트 (전공)
    @PropertyName("dk_rate")
    public String rate;            // 프로젝트 참여율
    @PropertyName("dk_period")
    public String period;          // 프로젝트 기간
    @PropertyName("dk_belong_to")
    public String belong_to;       // 소속

    public String postKey;
    public long countLikes = 0;
    public boolean clickable = false;
    public boolean isReadFeedback = false;


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("dk_seq", seq);
        result.put("dk_title", title);
        result.put("dk_description", description);
        result.put("dk_part", part);
        result.put("dk_rate", rate);
        result.put("dk_period", period);
        result.put("dk_belong_to", belong_to);

        return result;
    }
}
