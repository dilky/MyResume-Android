package net.dilkyzhart.myresume.app.comm;

import android.text.TextUtils;

/**
 * Created by dilky on 2017. 7. 2..
 */

public class FormatUtils {

    static final String DATE_DELIMITER = ".";

    public static String getDate(String value) {
        if (TextUtils.isEmpty(value))
            return value;
        if (!isOnlyNumber(value))
            return value;
        StringBuffer sbr = new StringBuffer();
        if (value.length() >= 8) {
            sbr.append(value.substring(0,4));
            sbr.append(DATE_DELIMITER);
            sbr.append(value.substring(4,6));
            sbr.append(DATE_DELIMITER);
            sbr.append(value.substring(6,value.length()));
            return sbr.toString();
        } else if (value.length() >= 6) {
            sbr.append(value.substring(0,4));
            sbr.append(DATE_DELIMITER);
            sbr.append(value.substring(4,value.length()));
            return sbr.toString();
        } else if (value.length() >= 4) {
            sbr.append(value.substring(0,4));
            sbr.append(DATE_DELIMITER);
            sbr.append(value.substring(4, value.length()));
            return sbr.toString();
        }
        return value;
    }


    /**
     * 입력값에 숫자만 포함되어있는지 여부
     * @param value 입력값
     * @return 입력값에 포함된 숫자
     */
    public static boolean isOnlyNumber(String value) {
        final String regEx = "[0-9]";
        if (value.matches(regEx)) return true;

        return false;
    }

    /**
     * 입력값에 포함된 숫자만 반환한다.
     * @param value 입력값
     * @return 입력값에 포함된 숫자
     */
    public static String getNumber(String value) {
        final String regEx = "[0-9]";
        if (value.matches(regEx)) return value;

        StringBuffer sb = new StringBuffer();
        for(int idx = 0; idx < value.length(); idx++) {
            if(value.substring(idx, idx+1).matches(regEx))
                sb.append(value.charAt(idx));
        }
        return sb.toString();
    }
}
