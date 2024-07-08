package com.example.fooddelivery.chefFoodPanel;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.R;

public class ChefProfileFragment extends Fragment {

    Button postDish;
    ConstraintLayout backimg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_prifile, container, false);
        getActivity().setTitle("Post Dish");

        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.bg2), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img2), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img3), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img4), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img5), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img6), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img7), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img8), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.bg3), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img9), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img10), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img11), 3000);
        animationDrawable.addFrame(AppCompatResources.getDrawable(requireContext(), R.drawable.img11), 3000);

        animationDrawable.setOneShot(false);
        animationDrawable.setEnterFadeDuration(850);
        animationDrawable.setExitFadeDuration(1600);

        backimg = v.findViewById(R.id.back1);
        backimg.setBackground(animationDrawable);
        animationDrawable.start();

        postDish = v.findViewById(R.id.post_dish);

        postDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), chef_postDish.class));
            }
        });

        return v;
    }
}
