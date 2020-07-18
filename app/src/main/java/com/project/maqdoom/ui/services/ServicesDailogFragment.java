package com.project.maqdoom.ui.services;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.maqdoom.R;
import com.project.maqdoom.ui.sellerAddPackage.SellerAddPackageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServicesDailogFragment extends DialogFragment {

    RecyclerView recyclerServices;
    List<ServicesChecklistItems> servicesChecklist = new ArrayList<>();
    ServicesItemAdapter servicesItemAdapter;
    public static final String TAG = ServicesDailogFragment.class.getSimpleName();
    private ArrayList<String> selectedData = new ArrayList<>();
    Button btnSubmit;

    public ServicesDailogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ServicesDailogFragment newInstance(String title) {
        ServicesDailogFragment frag = new ServicesDailogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        return inflater.inflate(R.layout.dialog_services, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerServices = view.findViewById(R.id.recycler_services);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        // Get field from view
       /* mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();*/
        String[] itemServices = getResources().getStringArray(R.array.item_services);
        for (int i = 0; i < itemServices.length; i++) {

            ServicesChecklistItems servicesChecklistItems = new ServicesChecklistItems(itemServices[i], false);
            servicesChecklist.add(servicesChecklistItems);
        }
        servicesItemAdapter = new ServicesItemAdapter(getActivity(), servicesChecklist);

        servicesItemAdapter.setOnItemClickListener(new ServicesItemAdapter.onItemclickListener() {
            @Override
            public void onItemclickListener(View view, int position, String data) {
                //selectedData.add(data);
                selectedData.addAll(Arrays.asList(data));


                //String datavalue=data;
                Log.d("seelcted", selectedData.toString());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("listdata", selectedData);
                SellerAddPackageFragment mapFragment = new SellerAddPackageFragment();
                mapFragment.setArguments(bundle);

                Objects.requireNonNull(getDialog()).dismiss();


            }
        });
        recyclerServices.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerServices.setAdapter(servicesItemAdapter);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}
