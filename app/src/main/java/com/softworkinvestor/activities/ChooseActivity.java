package com.softworkinvestor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.databinding.ActivityChooseBinding;
import com.softworkinvestor.model.SuccessResSignUp;
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

import static com.softworkinvestor.retrofit.Constant.showToast;

public class ChooseActivity extends AppCompatActivity {

    ActivityChooseBinding binding;

    private String strName = "",strEmail = "",strCcp="",strPhone="",strCountry,strUserType="";

    private RadioButton selectedRadioButton;

    private SoftworkInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose);
        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);
        Intent intent = getIntent();
        strName =  intent.getStringExtra("name");
        strEmail = intent.getStringExtra("email");
        strPhone = intent.getStringExtra("phone");
        strCountry = intent.getStringExtra("country");
        strCcp = intent.getStringExtra("ccp");

        binding.textViewNext.setOnClickListener(v ->
                {
                    if(binding.radioGroup.getCheckedRadioButtonId()==-1)
                    {
                        Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // get selected radio button from radioGroup
                        int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        selectedRadioButton = (RadioButton)findViewById(selectedId);
//                        Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
                        strUserType =    selectedRadioButton.getText().toString();
                        signup();
                    }
                }
                );
    }

    private void signup()
    {

        DataManager.getInstance().showProgressMessage(ChooseActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("name",strName);
        map.put("mobile",strPhone);
        map.put("email",strEmail);
        map.put("country_id",strCountry);
        map.put("country_code",strCcp);
        map.put("type",strUserType);

        Call<SuccessResSignUp> signupCall = apiInterface.signup(map);
        signupCall.enqueue(new Callback<SuccessResSignUp>() {
            @Override
            public void onResponse(Call<SuccessResSignUp> call, Response<SuccessResSignUp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignUp data = response.body();
                    if (data.status.equals("1")) {
                        showToast(ChooseActivity.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        startActivity(new Intent(ChooseActivity.this, LoginAct.class));
                    } else if (data.status.equals("0")) {
                        showToast(ChooseActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResSignUp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}