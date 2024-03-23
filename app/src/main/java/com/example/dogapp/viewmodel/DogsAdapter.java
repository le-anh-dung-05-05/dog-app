package com.example.dogapp.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.R;
import com.example.dogapp.databinding.DogsItemBinding;
import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable {
    private ArrayList<DogBreed> listDog;
    private ArrayList<DogBreed> listDogCopy;

    public DogsAdapter(ArrayList<DogBreed> dogList) {
        this.listDog = dogList;
        this.listDogCopy = listDog;
    }

    @NonNull
    @Override
    public DogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DogsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.dogs_item,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DogBreed dog = listDog.get(position);
       holder.binding.setDog(listDog.get(position));

        //Set ảnh từ URL vào ImageView
        Picasso.get().load(dog.getUrl()).into(holder.binding.ivDog);


    }

    @Override
    public int getItemCount() {
        return listDog.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String input = constraint.toString().toLowerCase();
                List<DogBreed> filteredDog = new ArrayList<DogBreed>();
                if(input.isEmpty()){
                    filteredDog.addAll(listDogCopy);

                } else{
                    for(DogBreed dog: listDogCopy){
                        if(dog.getName().toLowerCase().contains(input)) {
                            filteredDog.add(dog);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDog;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listDog = new ArrayList<>();
                listDog.addAll((List)results.values);
                notifyDataSetChanged();



            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView dogAva;
//        private TextView dogName, dogInfo;
//        private ImageButton btnLike;
         private DogsItemBinding binding;
        private boolean isLayout1Visible = true;

        public ViewHolder(DogsItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            // Define click listener for the ViewHolder's View

//            dogAva = (ImageView) view.findViewById(R.id.iv_dog);
//            dogName = (TextView) view.findViewById(R.id.tv_name);
//            dogInfo = (TextView) view.findViewById(R.id.tv_info);
//            btnLike = (ImageButton) view.findViewById(R.id.ib_like);
            binding.ivDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DogBreed dogBreed = listDog.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dogBreed);
                    Navigation.findNavController(v).navigate(R.id.detailsFragment, bundle);


                }
            });


            binding.getRoot().setOnTouchListener(new onSwipeTouchListener() {
                @Override
                public void onSwipeLeft() {
                    if (binding.layout1.getVisibility() == View.GONE) {
                        binding.layout1.setVisibility(View.VISIBLE);
                        binding.layout2.setVisibility(View.GONE);
                    } else {
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);
                    }
                    super.onSwipeLeft();

                }


            });

        }


    }
    public static class onSwipeTouchListener implements View.OnTouchListener{

        private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());



        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return true;
            }



            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

        public void onClick() {
        }

        public void onLongPress() {
        }
    }
}
