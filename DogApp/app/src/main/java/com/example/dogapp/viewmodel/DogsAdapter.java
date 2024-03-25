package com.example.dogapp.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.R;
import com.example.dogapp.databinding.DogsItemBinding;
import com.example.dogapp.model.DogBreed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable {

    public ArrayList<DogBreed> dogBreeds;
    public ArrayList<DogBreed> dogBreedsCopy;
    private static final int REQUEST_CODE_DETAIL = 1;


    public DogsAdapter(ArrayList<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.dogBreedsCopy = dogBreeds;
    }
    @NonNull
    @Override
    public DogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//        .inflate(R.layout.dogs_item, parent, false);

        DogsItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dogs_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DogsAdapter.ViewHolder holder, int position) {
        holder.binding.setDog(dogBreeds.get(position));
        DogBreed dog = dogBreeds.get(position);

        if (dog.isHearted()) {
            holder.binding.heart.setImageResource(R.drawable.clickedheart);
        } else {
            holder.binding.heart.setImageResource(R.drawable.heart);
        }

        holder.binding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // toggle the hearted state of the dog
                dog.setHearted(!dog.isHearted());

                // set the image resource based on the hearted state
                if (dog.isHearted()) {
                    holder.binding.heart.setImageResource(R.drawable.clickedheart);
                } else {
                    holder.binding.heart.setImageResource(R.drawable.heart);
                }
            }
        });

        Picasso.get().load(dogBreeds.get(position).getUrl()).into(holder.binding.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString().toLowerCase();
                List<DogBreed> filterDog = new ArrayList<DogBreed>();

                if (input.isEmpty()) {
                    filterDog.addAll(dogBreedsCopy);
                } else {
                    for (DogBreed dog: dogBreedsCopy) {
                        if (dog.getName().toLowerCase().contains(input)) {
                            filterDog.add(dog);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterDog;


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dogBreeds = new ArrayList<>();
                dogBreeds.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DogsItemBinding binding;

        public boolean heartClicked = false;

        public ViewHolder(DogsItemBinding itemBinding) {

            super(itemBinding.getRoot());

            this.binding = itemBinding;



            binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DogBreed dog = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dog);
                    Log.d("DEBUG", "CLICKED");
                    Navigation.findNavController(view).navigate(R.id.detailsFragment,bundle) ;
                }
            });

            binding.cvView.setOnTouchListener(new OnSwipeTouchListener() {
                @Override
                public void onSwipeLeft() {
                    if (binding.layout1.getVisibility() == View.GONE) {
                        binding.layout1.setVisibility(View.VISIBLE);
                        Log.d("DEBUG", "SWIPED");

                        binding.layout2.setVisibility(View.GONE);
                    } else {
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);

                    }
                }
            });

        }
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

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

    }

}
