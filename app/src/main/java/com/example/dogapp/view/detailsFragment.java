package com.example.dogapp.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dogapp.DataBinderMapperImpl;
import com.example.dogapp.R;
import com.example.dogapp.databinding.FragmentDetailsBinding;
import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;


public class detailsFragment extends Fragment {
    private DogBreed dogBreed;
    private FragmentDetailsBinding binding;
    private ImageButton toolbarImageButton;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dogBreed = (DogBreed) getArguments().getSerializable("dogBreed");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.fragment_details,
                null, false);
        View viewRoot = binding.getRoot();
        toolbarImageButton = binding.include.findViewById(R.id.btn_CloseEdit);
        toolbarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để quay lại Fragment danh sách
                navigateToListFragment();
            }
        });
        Picasso.get().load(dogBreed.getUrl()).into(binding.ivDog);
        binding.setDog(dogBreed);
        return viewRoot;
    }
    private void navigateToListFragment() {
        // Sử dụng FragmentManager để thực hiện giao diện chuyển đổi về Fragment danh sách
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(); // Quay lại Fragment trước đó (Fragment danh sách)
    }
}