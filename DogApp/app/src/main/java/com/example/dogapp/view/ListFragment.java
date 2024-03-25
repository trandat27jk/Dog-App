package com.example.dogapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogapp.MainActivity;
import com.example.dogapp.R;
import com.example.dogapp.model.DogBreed;
import com.example.dogapp.viewmodel.DogsAdapter;
import com.example.dogapp.viewmodel.DogsApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ListFragment extends Fragment {


    private DogsApiService apiService;

    private RecyclerView rvDogs;
    private ArrayList<DogBreed> dogBreeds;
    private DogsAdapter dogsAdapter;
    MenuItem menuItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        requireActivity().addMenuProvider(new MenuProvider() {
//            private boolean isSearchViewAdded = false;
//
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                if (!isSearchViewAdded) {
//                    menuInflater.inflate(R.menu.menu_search, menu);
//                    SearchView searchView1 = (SearchView) menu.findItem(R.id.search).getActionView();
//                    searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                        @Override
//                        public boolean onQueryTextSubmit(String s) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onQueryTextChange(String s) {
//                            dogsAdapter.getFilter().filter(s);
//                            return false;
//                        }
//                    });
//
//                    isSearchViewAdded = true;
//                }
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                // Handle option Menu Here
//                return false;
//            }
//        });

        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvDogs = view.findViewById(R.id.rv_dogs);

        dogBreeds = new ArrayList<DogBreed>();
        dogsAdapter = new DogsAdapter(dogBreeds);


        rvDogs.setAdapter(dogsAdapter);
        rvDogs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        apiService = new DogsApiService();

        apiService.getDogs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DogBreed> dogBreeds1) {
                        Log.d("Debug", "Success");

                        for (DogBreed dog: dogBreeds1) {
                            dogBreeds.add(dog);
                            dogsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d("Debug", "Fail" + e.getMessage());

                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dogsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the hearted state in the DogsAdapter
        dogsAdapter.notifyDataSetChanged();
    }

}