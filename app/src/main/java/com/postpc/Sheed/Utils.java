package com.postpc.Sheed;

public interface Utils {

    String SP_KEY_FOR_USER_ID = "sp_user_id";
    String FS_USERS_COLLECTION = "users";
    String FS_CHATS_COLLECTION = "chats";
    String USER_ID_KEY = "user_id";
    String USER_INTENT_SERIALIZABLE_KEY = "user_ser";

    String USER1_TEST = "111";
    String USER2_TEST = "222";

    String USER1_EMAIL = "chandler@gmail.com";
    String USER2_EMAIL = "monica@gmail.com";

    String WORKER_LAST_I = "worker_last_i";
    String WORKER_LAST_J = "worker_last_j";
    String WORK_MANAGER_TAG = "MakeMatches";

    String WORKER_DIFF_ARRAY = "diff_array";

    String IMAGE_URL_INTENT = "img_url";

    int SECOND = 1000;

    // chat:
    int MSG_TYPE_LEFT = 0;
    int MSG_TYPE_RIGHT = 1;

    // style:
    String PRIMARY_ORANGE = "#FFC107";
    String ALMOST_BLACK = "#303030";

    String WORKER_JOB_END_TIME = "worker_end_time";

    Long ALGO_RUN_INTERVAL_HOURS = 4L;
    Long ALGO_RUN_INTERVAL_MINS = 0L; //ALGO_RUN_INTERVAL_HOURS * 60

}
