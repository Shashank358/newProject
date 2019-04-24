package com.ssproduction.shashank.newproject.Fragment;

import com.ssproduction.shashank.newproject.Notifications.MyResponse;
import com.ssproduction.shashank.newproject.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
        {
            "Content-Type:application/json",
            "Authorization:key=AAAASL8H-Hs:APA91bHmhOMYjxq4w50s2HAnID5XVRbjnX1bNRPkdm1hnLcdJ_KVpFfXwNcjP-vvTwlXCC66tr2nGSFBZLotTqzRpBty2J9oxDQAk9ZZXsU0Dt2ixKAEzjdaQKpNyG5itfCa-BdmyiYn"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
