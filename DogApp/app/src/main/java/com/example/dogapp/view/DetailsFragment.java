package com.example.dogapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogapp.R;
import com.example.dogapp.databinding.FragmentDetailsBinding;
import com.example.dogapp.databinding.FragmentDetailsBindingImpl;
import com.example.dogapp.model.DogBreed;

public class DetailsFragment extends Fragment {

    private DogBreed dogBreed;

    private FragmentDetailsBinding binding;

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
                R.layout.fragment_details, null, false);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                navigateToListFragment();
            }
        });


        View viewRoot = binding.getRoot();
        binding.setDog(dogBreed);
        return viewRoot;
    }

    private void navigateToListFragment() {
        // Use the Navigation component or FragmentManager to navigate back to the ListFragment

        // If you are using the Navigation component:
//        Navigation.findNavController(requireView()).navigateUp();

        // If you are using FragmentManager:
         requireActivity().getSupportFragmentManager().popBackStack();
    }

}