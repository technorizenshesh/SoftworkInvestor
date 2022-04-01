package com.softworkinvestor.retrofit;

import com.softworkinvestor.model.SuccessResAddBussiness;
import com.softworkinvestor.model.SuccessResGetInvestmentPlan;
import com.softworkinvestor.model.SuccessResGetMontlyAmount;
import com.softworkinvestor.model.SuccessResGetOtp;
import com.softworkinvestor.model.SuccessResGetProfile;
import com.softworkinvestor.model.SuccessResSignUp;
import com.softworkinvestor.model.SuccessResVerifyOtp;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SoftworkInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignUp> signup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("send_otp")
    Call<SuccessResGetOtp> sendOtp(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("check_otp")
    Call<SuccessResVerifyOtp> verifyOtp(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getMonthly")
    Call<SuccessResGetMontlyAmount> getMonthlyIncome(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("getInvestmentplan")
    Call<SuccessResGetInvestmentPlan> getCategory(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("add_post_agriculture")
    Call<SuccessResAddBussiness> addPost(@FieldMap Map<String, String> paramHashMap);


}
