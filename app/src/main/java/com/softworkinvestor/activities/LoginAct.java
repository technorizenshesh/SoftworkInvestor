package com.softworkinvestor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.databinding.ActivityLogin2Binding;
import com.softworkinvestor.model.SuccessResGetOtp;
import com.softworkinvestor.model.SuccessResSignUp;
import com.softworkinvestor.retrofit.ApiClient;
import com.softworkinvestor.retrofit.Constant;
import com.softworkinvestor.retrofit.NetworkAvailablity;
import com.softworkinvestor.retrofit.SoftworkInterface;
import com.softworkinvestor.utitlity.DataManager;
import com.softworkinvestor.utitlity.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softworkinvestor.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    ActivityLogin2Binding binding;

    private String number="",strCcp = "";

    private SoftworkInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login2);

        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);

        binding.textViewCreate.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this,SignupActivity.class));
                }
                );

        binding.textViewNext.setOnClickListener(v ->

                {
                    number = binding.etPhone.getText().toString().trim();
                    if (!number.equalsIgnoreCase("")) {
                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            number =  binding.ccp.getSelectedCountryCode()+binding.etPhone.getText().toString().trim();
                            login();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void login()
    {

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("mobile",number);

        Call<SuccessResGetOtp> signupCall = apiInterface.sendOtp(map);
        signupCall.enqueue(new Callback<SuccessResGetOtp>() {
            @Override
            public void onResponse(Call<SuccessResGetOtp> call, Response<SuccessResGetOtp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetOtp data = response.body();
                    if (data.status.equals("1")) {
                        showToast(LoginAct.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID,data.getResult().getId());

                        startActivity(new Intent(LoginAct.this,VerifyActivity.class));
                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetOtp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}