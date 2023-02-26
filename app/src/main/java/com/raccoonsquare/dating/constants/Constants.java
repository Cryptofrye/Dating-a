package com.raccoonsquare.dating.constants;

public interface Constants {

    // Attention! You can only change values of following constants:

    // EMOJI_KEYBOARD, WEB_SITE_AVAILABLE, GOOGLE_PAY_TEST_BUTTON, FACEBOOK_AUTHORIZATION,
    // APP_TEMP_FOLDER, VIDEO_FILE_MAX_SIZE, BILLING_KEY, CLIENT_ID, API_DOMAIN, WEB_SITE,
    // GHOST_MODE_COST, VERIFIED_BADGE_COST, DISABLE_ADS_COST,
    // PRO_MODE_COST, HASHTAGS_COLOR

    // It is forbidden to change value of constants, which are not indicated above !!!

    int VOLLEY_REQUEST_SECONDS = 15; //SECONDS TO REQUEST

    Boolean EMOJI_KEYBOARD = true; // false = Do not display your own Emoji keyboard | true = allow display your own Emoji keyboard

    public static final Boolean GOOGLE_AUTHORIZATION = true; // Allow login, signup with Google and "Services" section in Settings
    public static final Boolean FACEBOOK_AUTHORIZATION = true; // Allow login, signup with Facebook and "Services" section in Settings

    Boolean WEB_SITE_AVAILABLE = true; // false = Do not show menu items (Open in browser, Copy profile link) in profile page | true = show menu items (Open in browser, Copy profile link) in profile page

    Boolean GOOGLE_PAY_TEST_BUTTON = true; // false = Do not show google pay test button in section upgrades

    int VIDEO_FILE_MAX_SIZE = 7340035; //Max size for video file in bytes | For example 5mb = 5*1024*1024

    String WEB_SITE = "https://datingnet.xyz/";  //web site url address

    String APP_TEMP_FOLDER = "chat"; //directory for temporary storage of images from the camera

    // Google Pay settings | Settings from In-App Purchasing for Android | See documentation

    String BILLING_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCA";

    // Client ID For identify the application | Must be the same with CLIENT_ID from server config: db.inc.php

    String CLIENT_ID = "1";                         // old 11; Correct example: 12567 | Incorrect example: 0987

    // Client Secret | Text constant | Must be the same with CLIENT_SECRET from server config: db.inc.php

    String CLIENT_SECRET = "wFt4*KBoNN_kjSdG13m1k3k=";    // Example: "wFt4*KBoNN_kjSdG13m1k3k="

    String API_DOMAIN = "https://datingnet.xyz/";  // url address to which the application sends requests | with back slash "/" at the end | example: https://mysite.com/ | for emulator on localhost: http://10.0.2.2/

    String API_FILE_EXTENSION = "";                 // Don`t change value for this constant!
    String API_VERSION = "v2";                      // Don`t change value for this constant!

    String METHOD_NOTIFICATIONS_CLEAR = API_DOMAIN + "api/" + API_VERSION + "/method/notifications.clear" + API_FILE_EXTENSION;
    String METHOD_GUESTS_CLEAR = API_DOMAIN + "api/" + API_VERSION + "/method/guests.clear" + API_FILE_EXTENSION;

    String METHOD_ACCOUNT_GET_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.getSettings" + API_FILE_EXTENSION;
    String METHOD_DIALOGS_NEW_GET = API_DOMAIN + "api/" + API_VERSION + "/method/dialogs_new.get" + API_FILE_EXTENSION;
    String METHOD_CHAT_UPDATE = API_DOMAIN + "api/" + API_VERSION + "/method/chat.update" + API_FILE_EXTENSION;

    String METHOD_ACCOUNT_LOGIN = API_DOMAIN + "api/" + API_VERSION + "/method/account.signIn" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SIGNUP = API_DOMAIN + "api/" + API_VERSION + "/method/account.signUp" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_AUTHORIZE = API_DOMAIN + "api/" + API_VERSION + "/method/account.authorize" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SET_GCM_TOKEN = API_DOMAIN + "api/" + API_VERSION + "/method/account.setGcmToken" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_LOGINBYFACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.signInByFacebook" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_RECOVERY = API_DOMAIN + "api/" + API_VERSION + "/method/account.recovery" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SETPASSWORD = API_DOMAIN + "api/" + API_VERSION + "/method/account.setPassword" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_DEACTIVATE = API_DOMAIN + "api/" + API_VERSION + "/method/account.deactivate" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SAVE_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.saveSettings" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_CONNECT_TO_FACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.connectToFacebook" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_DISCONNECT_FROM_FACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.disconnectFromFacebook" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_LOGOUT = API_DOMAIN + "api/" + API_VERSION + "/method/account.logOut" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SET_ALLOW_MESSAGES = API_DOMAIN + "api/" + API_VERSION + "/method/account.setAllowMessages" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SET_GEO_LOCATION = API_DOMAIN + "api/" + API_VERSION + "/method/account.setGeoLocation" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_UPLOADPHOTO = API_DOMAIN + "api/" + API_VERSION + "/method/account.uploadPhoto" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SET_ALLOW_PHOTOS_COMMENTS = API_DOMAIN + "api/" + API_VERSION + "/method/account.setAllowPhotosComments" + API_FILE_EXTENSION;

    String METHOD_SUPPORT_SEND_TICKET = API_DOMAIN + "api/" + API_VERSION + "/method/support.sendTicket" + API_FILE_EXTENSION;

    String METHOD_SETTINGS_PRIVACY = API_DOMAIN + "api/" + API_VERSION + "/method/account.privacy" + API_FILE_EXTENSION;

    String METHOD_PROFILE_GET = API_DOMAIN + "api/" + API_VERSION + "/method/profile.get" + API_FILE_EXTENSION;
    String METHOD_PROFILE_REPORT = API_DOMAIN + "api/" + API_VERSION + "/method/profile.report" + API_FILE_EXTENSION;
    String METHOD_PROFILE_FANS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/profile.getFans" + API_FILE_EXTENSION;
    String METHOD_PROFILE_PEOPLE_NEARBY_GET = API_DOMAIN + "api/" + API_VERSION + "/method/profile.getPeopleNearby" + API_FILE_EXTENSION;
    String METHOD_PROFILE_ILIKED_GET = API_DOMAIN + "api/" + API_VERSION + "/method/profile.getILiked" + API_FILE_EXTENSION;
    String METHOD_PROFILE_LIKE = API_DOMAIN + "api/" + API_VERSION + "/method/profile.like" + API_FILE_EXTENSION;

    String METHOD_BLACKLIST_GET = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.get" + API_FILE_EXTENSION;
    String METHOD_BLACKLIST_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.add" + API_FILE_EXTENSION;
    String METHOD_BLACKLIST_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.remove" + API_FILE_EXTENSION;

    String METHOD_FRIENDS_REQUEST = API_DOMAIN + "api/" + API_VERSION + "/method/friends.sendRequest" + API_FILE_EXTENSION;
    String METHOD_FRIENDS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/friends.get" + API_FILE_EXTENSION;
    String METHOD_FRIENDS_ACCEPT = API_DOMAIN + "api/" + API_VERSION + "/method/friends.acceptRequest" + API_FILE_EXTENSION;
    String METHOD_FRIENDS_REJECT = API_DOMAIN + "api/" + API_VERSION + "/method/friends.rejectRequest" + API_FILE_EXTENSION;
    String METHOD_FRIENDS_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/friends.remove" + API_FILE_EXTENSION;

    String METHOD_NOTIFICATIONS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/notifications.get" + API_FILE_EXTENSION;


    String METHOD_APP_CHECKUSERNAME = API_DOMAIN + "api/" + API_VERSION + "/method/app.checkUsername" + API_FILE_EXTENSION;
    String METHOD_APP_TERMS = API_DOMAIN + "api/" + API_VERSION + "/method/app.terms" + API_FILE_EXTENSION;
    String METHOD_APP_THANKS = API_DOMAIN + "api/" + API_VERSION + "/method/app.thanks" + API_FILE_EXTENSION;
    String METHOD_APP_SEARCH = API_DOMAIN + "api/" + API_VERSION + "/method/app.search" + API_FILE_EXTENSION;

    String METHOD_APP_SEARCH_PRELOAD = API_DOMAIN + "api/" + API_VERSION + "/method/app.searchPreload" + API_FILE_EXTENSION;

    String METHOD_GIFTS_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.remove" + API_FILE_EXTENSION;
    String METHOD_GIFTS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.get" + API_FILE_EXTENSION;
    String METHOD_GIFTS_SELECT = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.select" + API_FILE_EXTENSION;
    String METHOD_GIFTS_SEND = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.send" + API_FILE_EXTENSION;

    String METHOD_GUESTS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/guests.get" + API_FILE_EXTENSION;


    String METHOD_CHAT_GET = API_DOMAIN + "api/" + API_VERSION + "/method/chat.get" + API_FILE_EXTENSION;
    String METHOD_CHAT_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/chat.remove" + API_FILE_EXTENSION;
    String METHOD_CHAT_GET_PREVIOUS = API_DOMAIN + "api/" + API_VERSION + "/method/chat.getPrevious" + API_FILE_EXTENSION;

    String METHOD_MSG_NEW = API_DOMAIN + "api/" + API_VERSION + "/method/msg.new" + API_FILE_EXTENSION;
    String METHOD_MSG_UPLOAD_IMG = API_DOMAIN + "api/" + API_VERSION + "/method/msg.uploadImg" + API_FILE_EXTENSION;

    // added for version 3.0

    String METHOD_REFERRALS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/referrals.get" + API_FILE_EXTENSION;

    // added for version 3.2

    String METHOD_GET_STICKERS = API_DOMAIN + "api/" + API_VERSION + "/method/stickers.get" + API_FILE_EXTENSION;

    // added for version 3.4

    String METHOD_CHAT_NOTIFY = API_DOMAIN + "api/" + API_VERSION + "/method/chat.notify" + API_FILE_EXTENSION;

    // added for version 3.6

    String METHOD_MATCHES_GET = API_DOMAIN + "api/" + API_VERSION + "/method/matches.get" + API_FILE_EXTENSION;
    String METHOD_HOTGAME_GET = API_DOMAIN + "api/" + API_VERSION + "/method/hotgame.get" + API_FILE_EXTENSION;

    // added for version 3.7

    String METHOD_FEELINGS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/feelings.get" + API_FILE_EXTENSION;
    String METHOD_ACCOUNT_SET_FEELING = API_DOMAIN + "api/" + API_VERSION + "/method/account.setFeeling" + API_FILE_EXTENSION;

    // added for version 4.1

    String TAG_UPDATE_BADGES = "update_badges";

    // added dor version 4.3

    String METHOD_APP_CHECK_EMAIL = API_DOMAIN + "api/" + API_VERSION + "/method/app.checkEmail" + API_FILE_EXTENSION;

    // added dor version 4.5

    String METHOD_ACCOUNT_UPLOAD_IMAGE = API_DOMAIN + "api/" + API_VERSION + "/method/profile.uploadImg" + API_FILE_EXTENSION;

    //

    public static final String METHOD_ACCOUNT_UPGRADE = API_DOMAIN + "api/" + API_VERSION + "/method/account.upgrade" + API_FILE_EXTENSION;

    public static final String METHOD_PAYMENTS_NEW = API_DOMAIN + "api/" + API_VERSION + "/method/payments.new" + API_FILE_EXTENSION;
    public static final String METHOD_PAYMENTS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/payments.get" + API_FILE_EXTENSION;

    public static final String METHOD_SPOTLIGHT_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/spotlight.add" + API_FILE_EXTENSION;
    public static final String METHOD_SPOTLIGHT_GET = API_DOMAIN + "api/" + API_VERSION + "/method/spotlight.get" + API_FILE_EXTENSION;

    //

    String METHOD_GALLERY_NEW = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.new" + API_FILE_EXTENSION;
    String METHOD_GALLERY_GET = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.get" + API_FILE_EXTENSION;
    String METHOD_GALLERY_REPORT = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.report" + API_FILE_EXTENSION;
    String METHOD_GALLERY_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.remove" + API_FILE_EXTENSION;
    String METHOD_GALLERY_LIKE = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.like" + API_FILE_EXTENSION;
    String METHOD_GALLERY_LIKES = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.likes" + API_FILE_EXTENSION;
    String METHOD_GALLERY_GET_ITEM = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.getItem" + API_FILE_EXTENSION;
    String METHOD_GALLERY_STREAM = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.stream" + API_FILE_EXTENSION;
    String METHOD_GALLERY_FEED = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.feed" + API_FILE_EXTENSION;

    String METHOD_GALLERY_UPLOAD_IMG = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.uploadImg" + API_FILE_EXTENSION;
    String METHOD_GALLERY_UPLOAD_VIDEO = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.uploadVideo" + API_FILE_EXTENSION;

    String METHOD_COMMENTS_NEW = API_DOMAIN + "api/" + API_VERSION + "/method/comments.new" + API_FILE_EXTENSION;
    String METHOD_COMMENTS_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/comments.remove" + API_FILE_EXTENSION;

    // for version 5.2

    String METHOD_ACCOUNT_OTP = API_DOMAIN + "api/" + API_VERSION + "/method/account.otp" + API_FILE_EXTENSION;

    // for version 5.2
    String METHOD_SEARCH_PEOPLE = API_DOMAIN + "api/" + API_VERSION + "/method/search.people" + API_FILE_EXTENSION;

    // for version 6.2

    public static final String METHOD_ACCOUNT_GOOGLE_AUTH = API_DOMAIN + "api/" + API_VERSION + "/method/account.google" + API_FILE_EXTENSION;

    // for version 6.4

    String METHOD_CHAT_SPAM = API_DOMAIN + "api/" + API_VERSION + "/method/chat.spam" + API_FILE_EXTENSION;

    int SEARCH_DISTANCE_DEFAULT = 30000;
    int HOTGAME_DISTANCE_DEFAULT = 30000;

    int APP_TYPE_ALL = -1;
    int APP_TYPE_MANAGER = 0;
    int APP_TYPE_WEB = 1;
    int APP_TYPE_ANDROID = 2;
    int APP_TYPE_IOS = 3;

    public static final int SIGNIN_EMAIL = 0;
    public static final int SIGNIN_OTP = 1;
    public static final int SIGNIN_FACEBOOK = 2;
    public static final int SIGNIN_GOOGLE = 3;
    public static final int SIGNIN_APPLE = 4;
    public static final int SIGNIN_TWITTER = 5;

    public static final int OAUTH_TYPE_FACEBOOK = 0;
    public static final int OAUTH_TYPE_GOOGLE = 1;

    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO = 1;                  //WRITE_EXTERNAL_STORAGE
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_COVER = 2;                  //WRITE_EXTERNAL_STORAGE
    int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 3;                               //ACCESS_COARSE_LOCATION
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;                        //WRITE_EXTERNAL_STORAGE

    int APP_BAR_WITH_ADMOB_HEIGHT = 127;
    int APP_BAR_WITHOUT_ADMOB_HEIGHT = 50;

    int LIST_ITEMS = 20;
    int HOTGAME_LIST_ITEMS = 500;

    int ENABLED = 1;
    int DISABLED = 0;

    int GCM_ENABLED = 1;
    int GCM_DISABLED = 0;

    int ADMOB_ENABLED = 1;
    int ADMOB_DISABLED = 0;

    int COMMENTS_ENABLED = 1;
    int COMMENTS_DISABLED = 0;

    int MESSAGES_ENABLED = 1;
    int MESSAGES_DISABLED = 0;

    int ERROR_SUCCESS = 0;

    int SEX_MALE = 0;
    int SEX_FEMALE = 1;
    int SEX_UNKNOWN = 2;

    int NOTIFY_TYPE_LIKE = 0;
    int NOTIFY_TYPE_FOLLOWER = 1;
    int NOTIFY_TYPE_MESSAGE = 2;
    int NOTIFY_TYPE_COMMENT = 3;
    int NOTIFY_TYPE_COMMENT_REPLY = 4;
    int NOTIFY_TYPE_FRIEND_REQUEST_ACCEPTED = 5;
    int NOTIFY_TYPE_GIFT = 6;

    int NOTIFY_TYPE_IMAGE_COMMENT = 7;
    int NOTIFY_TYPE_IMAGE_COMMENT_REPLY = 8;
    int NOTIFY_TYPE_IMAGE_LIKE = 9;

    int NOTIFY_TYPE_MEDIA_APPROVE = 10;
    int NOTIFY_TYPE_MEDIA_REJECT = 11;

    int NOTIFY_TYPE_PROFILE_PHOTO_APPROVE = 12;
    int NOTIFY_TYPE_PROFILE_PHOTO_REJECT = 13;

    int NOTIFY_TYPE_ACCOUNT_APPROVE = 14;
    int NOTIFY_TYPE_ACCOUNT_REJECT = 15;

    int NOTIFY_TYPE_PROFILE_COVER_APPROVE = 16;
    int NOTIFY_TYPE_PROFILE_COVER_REJECT = 17;

    int GCM_NOTIFY_CONFIG = 0;
    int GCM_NOTIFY_SYSTEM = 1;
    int GCM_NOTIFY_CUSTOM = 2;
    int GCM_NOTIFY_LIKE = 3;
    int GCM_NOTIFY_ANSWER = 4;
    int GCM_NOTIFY_QUESTION = 5;
    int GCM_NOTIFY_COMMENT = 6;
    int GCM_NOTIFY_FOLLOWER = 7;
    int GCM_NOTIFY_PERSONAL = 8;
    int GCM_NOTIFY_MESSAGE = 9;
    int GCM_NOTIFY_COMMENT_REPLY = 10;
    int GCM_FRIEND_REQUEST_INBOX = 11;
    int GCM_FRIEND_REQUEST_ACCEPTED = 12;
    int GCM_NOTIFY_GIFT = 14;
    int GCM_NOTIFY_SEEN = 15;
    int GCM_NOTIFY_TYPING = 16;
    int GCM_NOTIFY_URL = 17;

    int GCM_NOTIFY_IMAGE_COMMENT_REPLY = 18;
    int GCM_NOTIFY_IMAGE_COMMENT = 19;
    int GCM_NOTIFY_IMAGE_LIKE = 20;

    int GCM_NOTIFY_TYPING_START = 27;
    int GCM_NOTIFY_TYPING_END = 28;

    int GCM_NOTIFY_REFERRAL = 24;
    int GCM_NOTIFY_MATCH = 25;

    int GCM_NOTIFY_MEDIA_APPROVE = 1001;
    int GCM_NOTIFY_MEDIA_REJECT = 1002;

    int GCM_NOTIFY_PROFILE_PHOTO_APPROVE = 1003;
    int GCM_NOTIFY_PROFILE_PHOTO_REJECT = 1004;

    int GCM_NOTIFY_ACCOUNT_APPROVE = 1005;
    int GCM_NOTIFY_ACCOUNT_REJECT = 1006;

    int GCM_NOTIFY_PROFILE_COVER_APPROVE = 1007;
    int GCM_NOTIFY_PROFILE_COVER_REJECT = 1008;

    int GCM_NOTIFY_CHANGE_ACCOUNT_SETTINGS = 30;

    int ERROR_LOGIN_TAKEN = 300;
    int ERROR_EMAIL_TAKEN = 301;
    int ERROR_FACEBOOK_ID_TAKEN = 302;
    int ERROR_MULTI_ACCOUNT = 500;

    int ERROR_OTP_VERIFICATION = 506;
    int ERROR_OTP_PHONE_NUMBER_TAKEN = 507;

    int ACCOUNT_STATE_ENABLED = 0;
    int ACCOUNT_STATE_DISABLED = 1;
    int ACCOUNT_STATE_BLOCKED = 2;
    int ACCOUNT_STATE_DEACTIVATED = 3;

    int ACCOUNT_TYPE_USER = 0;
    int ACCOUNT_TYPE_GROUP = 1;

    int GALLERY_ITEM_TYPE_IMAGE = 0;
    int GALLERY_ITEM_TYPE_VIDEO = 1;

    int ERROR_UNKNOWN = 100;
    int ERROR_ACCESS_TOKEN = 101;

    int ERROR_ACCOUNT_ID = 400;

    int ERROR_CLIENT_ID = 19100;
    int ERROR_CLIENT_SECRET = 19101;

    int UPLOAD_TYPE_PHOTO = 0;
    int UPLOAD_TYPE_COVER = 1;

    int ACTION_NEW = 1;
    int ACTION_EDIT = 2;
    int SELECT_POST_IMG = 3;
    int VIEW_CHAT = 4;
    int CREATE_POST_IMG = 5;
    int SELECT_CHAT_IMG = 6;
    int CREATE_CHAT_IMG = 7;
    int FEED_NEW_POST = 8;
    int FRIENDS_SEARCH = 9;
    int ITEM_EDIT = 10;
    int STREAM_NEW_POST = 11;

    int SELECT_PHOTO_IMG = 20;
    int CREATE_PHOTO_IMG = 21;

    int PAGE_PROFILE = 1;
    int PAGE_GALLERY = 2;
    int PAGE_FRIENDS = 3;
    int PAGE_MATCHES = 4;
    int PAGE_MESSAGES = 5;
    int PAGE_NOTIFICATIONS = 6;
    int PAGE_GUESTS = 7;
    int PAGE_LIKES = 8;
    int PAGE_LIKED = 9;
    int PAGE_UPGRADES = 10;
    int PAGE_NEARBY = 11;
    int PAGE_MEDIA_STREAM = 12;
    int PAGE_MEDIA_FEED = 13;
    int PAGE_SEARCH = 14;
    int PAGE_SETTINGS = 15;
    int PAGE_HOTGAME = 16;
    int PAGE_FINDER = 17;
    int PAGE_MENU = 18;
    int PAGE_MAIN = 19;

    //

    public static final int PA_BUY_CREDITS = 0;
    public static final int PA_BUY_GIFT = 1;
    public static final int PA_BUY_VERIFIED_BADGE = 2;
    public static final int PA_BUY_GHOST_MODE = 3;
    public static final int PA_BUY_DISABLE_ADS = 4;
    public static final int PA_BUY_REGISTRATION_BONUS = 5;
    public static final int PA_BUY_REFERRAL_BONUS = 6;
    public static final int PA_BUY_MANUAL_BONUS = 7;
    public static final int PA_BUY_PRO_MODE = 8;
    public static final int PA_BUY_SPOTLIGHT = 9;
    public static final int PA_BUY_MESSAGE_PACKAGE = 10;
    public static final int PA_SEND_TRANSFER = 11;
    public static final int PA_RECEIVE_TRANSFER = 12;

    public static final int PT_UNKNOWN = 0;
    public static final int PT_CREDITS = 1;
    public static final int PT_CARD = 2;
    public static final int PT_GOOGLE_PURCHASE = 3;
    public static final int PT_APPLE_PURCHASE = 4;
    public static final int PT_ADMOB_REWARDED_ADS = 5;
    public static final int PT_BONUS = 6;

    String TAG = "TAG";

    String HASHTAGS_COLOR = "#5BCFF2";
}