package com.softworkinvestor.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.adapters.InvestmentCategoryAdapter;
import com.softworkinvestor.databinding.FragmentSelectCategoryBinding;
import com.softworkinvestor.model.SuccessResGetInvestmentPlan;
import com.softworkinvestor.model.SuccessResGetInvestmentPlan;
import com.softworkinvestor.retrofit.ApiClient;
import com.softworkinvestor.retrofit.NetworkAvailablity;
import com.softworkinvestor.retrofit.SoftworkInterface;
import com.softworkinvestor.utitlity.DataManager;
import com.softworkinvestor.utitlity.MonthlyIncomeClick;
import com.softworkinvestor.utitlity.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softworkinvestor.retrofit.Constant.USER_ID;
import static com.softworkinvestor.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectCategoryFragment extends Fragment implements MonthlyIncomeClick {

    FragmentSelectCategoryBinding binding;
    SoftworkInterface apiInterface;

    int selectedPosition = -1;

    private String monthlyIncome,investmentMoney;

    private ArrayList<SuccessResGetInvestmentPlan.Result> resultArrayList = new ArrayList<>();

    private InvestmentCategoryAdapter investmentCategoryAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectCategoryFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static SelectCategoryFragment newInstance(String param1, String param2) {
        SelectCategoryFragment fragment = new SelectCategoryFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_select_category, container, false);
        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);

        investmentCategoryAdapter = new InvestmentCategoryAdapter(getActivity(),resultArrayList,SelectCategoryFragment.this);

        Bundle bundle = getArguments();

        if(bundle!=null)
        {
            monthlyIncome = bundle.getString("monthlyIncome");
            investmentMoney = bundle.getString("investment");
        }

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(investmentCategoryAdapter);

        binding.nestedScrollView.setNestedScrollingEnabled(false);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getMonthlyIncome();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.textViewNext.setOnClickListener(v ->
                {
                    if(selectedPosition==-1)
                    {
                        showToast(getActivity(),"Please select a income.");
                    }
                    else
                    {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("investment",investmentMoney);
                        bundle1.putString("monthlyIncome",monthlyIncome);
                        bundle1.putString("category",resultArrayList.get(selectedPosition).getImage());
                        Navigation.findNavController(v).navigate(R.id.action_investmentAmountFragment_to_selectCategoryFragment,bundle1);
                    }
                }
        );

        return binding.getRoot();
    }

    private void getMonthlyIncome()
    {
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetInvestmentPlan> signupCall = apiInterface.getCategory(map);
        signupCall.enqueue(new Callback<SuccessResGetInvestmentPlan>() {
            @Override
            public void onResponse(Call<SuccessResGetInvestmentPlan> call, Response<SuccessResGetInvestmentPlan> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInvestmentPlan data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        resultArrayList.clear();
                        resultArrayList.addAll(data.getResult());

                        investmentCategoryAdapter.notifyDataSetChanged();

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetInvestmentPlan> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void selectedPosition(int position) {

        selectedPosition = position;

    }
}