package com.arjun.infotech.bondera.Constance;

/**
 * Created by Ravi on 1/9/2018.
 */

public class Constant {


    public static final int REQUEST_CURRENCY = 441;

    public static enum Dialogcode {
        WARNING, ACCOUNT_NOT_ACTIVATE, SUCCESS, NOTIFY, NOTIFY_CONTACTINFO, DELETE_BUSINESS_CONFIRMATION
    }

    public static enum Sortcode {
        SORT_BY_DATE_CREATED, SORT_BY_NAME, SORT_BY_CATEGORIES, SORT_BY_PUBLISHED_UNPUBLISHED, DATE, MOST_VIEWED
    }

    public static final int GET_IMAGE_FROM_GALLERY = 0;
    public static final int GET_IMAGE_BY_CAMERS = 1;
    public static String MyPREFERENCES = "MyPREFERENCES";

    public static String REMOVEINSTUCT = "REMOVEINSTUCT";


    public static final String REGULAR_EXPRESSION_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d$&+,:;=?@#|'<>.-^*()%!]{8,}$";

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

//    public static final String YOUTUBE_PATTERN = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";


//    public static final String YOUTUBE_PATTERN = "/^.*(?:(?:youtu\\.be\\/|v\\/|vi\\/|u\\/\\w\\/|embed\\/)|(?:(?:watch)?\\?v(?:i)?=|\\&v(?:i)?=))([^#\\&\\?]*).*/";

    public static final String YOUTUBE_PATTERN = "^(https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$";
//    public static final String YOUTUBE_PATTERN ="^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";

    public static final String DEVELOPER_KEY = "AIzaSyCo55ByjQIyBP9BibA3YCEl4FOG31V0C0c";
    public static final String VIDEO_ID = "VideoId";


    public static final String YOUTUBE_VIDEO_ID = "/(?:https?:\\/{2})?(?:w{3}\\.)?youtu(?:be)?\\.(?:com|be)(?:\\/watch\\?v=|\\/)([^\\s&]+)/";


    // Api call
    public static final String URL = "http://app.robotresponder.com/api/";
    public static final String send_sms = "http://app.robotresponder.com/api/sendSms";
    public static final String Email = "email";
    public static final String Pass = "password";


    public static final String sms_to_number  = "sms_to_number";
    public static final String sms_text = "sms_text";
    public static final String user_id ="user_id";
    public static final String dob = "dob";
}
