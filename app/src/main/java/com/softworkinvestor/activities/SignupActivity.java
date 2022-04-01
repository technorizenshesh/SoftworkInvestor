package com.softworkinvestor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.softworkinvestor.R;
import com.softworkinvestor.databinding.ActivitySignupBinding;
import com.softworkinvestor.retrofit.NetworkAvailablity;

import static com.softworkinvestor.retrofit.Constant.isValidEmail;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    private String strName = "",strEmail = "",strCcp="",strPhone="",strCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);

        binding.textViewLogin.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupActivity.this, LoginAct.class));
                }
                );

        binding.textViewSignUp.setOnClickListener(v ->

                {
                    strName = binding.editTextName.getText().toString().trim();
                    strEmail = binding.editTextEmail.getText().toString().trim();
                    strPhone = binding.etPhone.getText().toString().trim();
                    strCountry = binding.editTextCountry.getText().toString().trim();
                    strCcp = binding.ccp.getSelectedCountryCode();

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                            strPhone = binding.etPhone.getText().toString().trim();
                            signup();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void signup() {

        Intent intent = new Intent(SignupActivity.this, ChooseActivity.class);
        intent.putExtra("name",  strName);
        intent.putExtra("phone",  strPhone);
        intent.putExtra("email",  strEmail);
        intent.putExtra("country",  strCountry);
        intent.putExtra("ccp",  strCcp);
        startActivity(intent);

    }

    private boolean isValid() {
        if (strName.equalsIgnoreCase("")) {
            binding.editTextName.setError(getString(R.string.enter_first));
            return false;
        }else if (strEmail.equalsIgnoreCase("")) {
            binding.editTextEmail.setError(getString(R.string.enter_email));
            return false;
        }  else if (!isValidEmail(strEmail)) {
            binding.editTextEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPhone.equalsIgnoreCase("")) {
            binding.etPhone.setError(getString(R.string.please_enter_mobile_number));
            return false;
        }else if (strCountry.equalsIgnoreCase("")) {
            binding.editTextCountry.setError(getString(R.string.please_enter_pass));
            return false;
        }
        return true;
    }


}