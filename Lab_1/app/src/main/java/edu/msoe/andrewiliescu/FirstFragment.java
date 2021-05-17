package edu.msoe.andrewiliescu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

public class FirstFragment extends Fragment {
    //Arbitrary variable used to set image to visible=0 and invisible=1
    int temp  = 0;
    ImageView img;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = (ImageView) view.findViewById(R.id.imageView2);


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                Snackbar.make(view, "Whoa you did it!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I made this button :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(temp == 0){
                    img.setVisibility(view.VISIBLE);
                    temp++;
                } else {
                    img.setVisibility(view.INVISIBLE);
                    temp--;
                }
            }
        });
    }
}