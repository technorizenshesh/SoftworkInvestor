package com.softworkinvestor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Verify;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.databinding.ActivityVerifyBinding;
import com.softworkinvestor.model.SuccessResVerifyOtp;
import com.softworkinvestor.retrofit.ApiClient;
import com.softworkinvestor.retrofit.Constant;
import com.softworkinvestor.retrofit.SoftworkInterface;
import com.softworkinvestor.utitlity.DataManager;
import com.softworkinvestor.utitlity.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softworkinvestor.retrofit.Constant.USER_ID;
import static com.softworkinvestor.retrofit.Constant.showToast;

public class VerifyActivity extends AppCompatActivity {

    ActivityVerifyBinding binding;

    private SoftworkInterface apiInterface;

    private String finalOtp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verify);
        init();

        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);

        binding.textViewNext.setOnClickListener(v ->
                {
                    if (TextUtils.isEmpty(binding.et1.getText().toString().trim())) {
                        Toast.makeText(VerifyActivity.this, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et2.getText().toString().trim())) {
                        Toast.makeText(VerifyActivity.this, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et3.getText().toString().trim())) {
                        Toast.makeText(VerifyActivity.this, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.et4.getText().toString().trim())) {
                        Toast.makeText(VerifyActivity.this, getString(R.string.please_enter_com_otp), Toast.LENGTH_SHORT).show();
                    } else {
                        finalOtp =
                                binding.et1.getText().toString().trim() +
                                        binding.et2.getText().toString().trim() +
                                        binding.et3.getText().toString().trim() +
                                        binding.et4.getText().toString().trim()
                        ;

                        verifyOtp();

                    }
                }
        );

    }

    private void init() {
        binding.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et2.setText("");
                    binding.et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

        binding.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et3.setText("");
                    binding.et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });

        binding.et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.et4.setText("");
                    binding.et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        binding.et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

    }

    private void verifyOtp()
    {
        String userId = SharedPreferenceUtility.getInstance(VerifyActivity.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(VerifyActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("otp",finalOtp);
        Call<SuccessResVerifyOtp> signupCall = apiInterface.verifyOtp(map);
        signupCall.enqueue(new Callback<SuccessResVerifyOtp>() {
            @Override
            public void onResponse(Call<SuccessResVerifyOtp> call, Response<SuccessResVerifyOtp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResVerifyOtp data = response.body();
                    if (data.status.equals("1")) {
                        showToast(VerifyActivity.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(VerifyActivity.this).putString(USER_ID,data.getResult().getId());
                        startActivity(new Intent(VerifyActivity.this, HomeAct.class));
                    } else if (data.status.equals("0")) {
                        showToast(VerifyActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResVerifyOtp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}