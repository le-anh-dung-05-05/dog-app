package com.example.dogapp.view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
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

import androidx.appcompat.widget.Toolbar;

import com.example.dogapp.R;
import com.example.dogapp.model.DogBreed;
import com.example.dogapp.viewmodel.DogsAdapter;
import com.example.dogapp.viewmodel.DogsApiService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;






public class listFragment extends Fragment {
    private DogsApiService apiService;
    private ArrayList<DogBreed> dogBreeds;
    private RecyclerView rvDogs;
    private DogsAdapter dogsAdapter;
    private Toolbar toolbar;




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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        return view;



    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar1);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        MenuHost menuHost = requireActivity();
        // Thêm các mục menu mà không sử dụng API Menu của Fragment
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                // Thêm các mục menu ở đây


                menuInflater.inflate(R.menu.menu_search, menu);
                SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();
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
            public boolean onMenuItemSelected(MenuItem menuItem) {
                // Xử lý sự lựa chọn menu
                switch (menuItem.getItemId()) {
//                    case R.id.mi_s:
//                        // Xử lý khi chọn menu "settings"
//                        return true;
                    default:
                        return false;
                }
            }


        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);



        rvDogs = view.findViewById(R.id.rv_dogs);
        dogBreeds = new ArrayList<DogBreed>();
        dogsAdapter = new DogsAdapter(dogBreeds);
        rvDogs.setAdapter(dogsAdapter);
        rvDogs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        apiService = new DogsApiService();
        apiService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DogBreed> dogBreeds1) {
                        Log.d("DEBUG", "Success");
                        for(DogBreed dog: dogBreeds1) {
                            dogBreeds.add(dog);
                            dogsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d("DEBUG", "Error");

                    }
                });
    }



}