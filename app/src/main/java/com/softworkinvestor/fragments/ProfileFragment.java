package com.softworkinvestor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.activities.HomeAct;
import com.softworkinvestor.activities.VerifyActivity;
import com.softworkinvestor.databinding.FragmentProfileBinding;
import com.softworkinvestor.model.SuccessResGetProfile;
import com.softworkinvestor.model.SuccessResVerifyOtp;
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

import static com.softworkinvestor.retrofit.Constant.USER_ID;
import static com.softworkinvestor.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private SuccessResGetProfile.Result userDetail = null;

    private SoftworkInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);

        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);

            if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                getProfile();

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }

        return binding.getRoot();
    }

    private void getProfile()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetProfile> signupCall = apiInterface.getProfile(map);
        signupCall.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProfile data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        userDetail = data.getResult();
                        setUserDetail();

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                          } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setUserDetail()
    {

        binding.tvName.setText(userDetail.getUserName());
        binding.tvNumber.setText(userDetail.getCountryCode()+" "+userDetail.getMobile());

    }

}