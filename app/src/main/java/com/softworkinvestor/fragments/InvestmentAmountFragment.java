package com.softworkinvestor.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softworkinvestor.R;
import com.softworkinvestor.adapters.MonthlyIncome;
import com.softworkinvestor.databinding.FragmentInvestmentAmountBinding;
import com.softworkinvestor.model.SuccessResGetMontlyAmount;
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
 * Use the {@link InvestmentAmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvestmentAmountFragment extends Fragment implements MonthlyIncomeClick {

    FragmentInvestmentAmountBinding binding;

    private SoftworkInterface apiInterface;
    int selectedPosition = -1;

    private String monthlyIncome;

    private ArrayList<SuccessResGetMontlyAmount.Result> monthlyList = new ArrayList<>();

    private MonthlyIncome monthlyIncomeAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InvestmentAmountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvestmentAmountFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static InvestmentAmountFragment newInstance(String param1, String param2) {
        InvestmentAmountFragment fragment = new InvestmentAmountFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_investment_amount, container, false);
        apiInterface = ApiClient.getClient().create(SoftworkInterface.class);
        monthlyIncomeAdapter = new MonthlyIncome(getActivity(),monthlyList,InvestmentAmountFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.recyclerView.setAdapter(monthlyIncomeAdapter);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getMonthlyIncome();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        Bundle bundle = getArguments();

        if(bundle!=null)
        {
            monthlyIncome = bundle.getString("monthlyIncome");
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
                        bundle1.putString("investment",monthlyList.get(selectedPosition).getIncome());
                        bundle1.putString("monthlyIncome",monthlyIncome);
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
        Call<SuccessResGetMontlyAmount> signupCall = apiInterface.getMonthlyIncome(map);
        signupCall.enqueue(new Callback<SuccessResGetMontlyAmount>() {
            @Override
            public void onResponse(Call<SuccessResGetMontlyAmount> call, Response<SuccessResGetMontlyAmount> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetMontlyAmount data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        monthlyList.clear();
                        monthlyList.addAll(data.getResult());
                        monthlyIncomeAdapter.notifyDataSetChanged();
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetMontlyAmount> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void selectedPosition(int position) {

       selectedPosition = position ;

    }
}