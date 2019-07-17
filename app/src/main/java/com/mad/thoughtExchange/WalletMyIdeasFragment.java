package com.mad.thoughtExchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class WalletMyIdeasFragment extends Fragment {

    private TextView textCounter;
    private EditText newPostContent;
    private Button submitBtn;

    public WalletMyIdeasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_content, container, false);

        newPostContent = view.findViewById(R.id.newpost_content);
        textCounter = view.findViewById(R.id.text_counter);
        submitBtn = view.findViewById(R.id.newpost_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }





}
