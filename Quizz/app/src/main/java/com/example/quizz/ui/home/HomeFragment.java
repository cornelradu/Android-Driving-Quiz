package com.example.quizz.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizz.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        if(getArguments() != null && getArguments().containsKey("result")){
            if(getArguments().get("result").equals("Failed")){
                textView.setText("Test picat. Ati raspuns corect la " + getArguments().getString("correct") + " intrebari si gresit la " + getArguments().getString("wrong") + " intrebari.");
                setArguments(null);
            } else {
                textView.setText("Test reusit. Ati raspuns corect la " + getArguments().getString("correct") + " intrebari si gresit la " + getArguments().getString("wrong") + " intrebari.");
                setArguments(null);
            }
        } else {
            textView.setText("Accesati simulare pentru a face un chestionar.");

        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}