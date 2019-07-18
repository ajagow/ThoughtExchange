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


public class NewContentFragment extends Fragment {

    private TextView textCounter;
    private EditText newPostContent;
    private Button submitBtn;

    public NewContentFragment() {
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
                Fragment home = getFragmentManager().findFragmentByTag("1");

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.fade_out).hide(NewContentFragment.this).show(home).commit();
            }
        });

        setTextWatcherForPostCounter();

        return view;
    }





    private void setTextWatcherForPostCounter() {
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                String newCount = 100 - s.length() + " characters left";
                textCounter.setText(newCount);
            }

            public void afterTextChanged(Editable s) {
            }
        };

        newPostContent.addTextChangedListener(mTextEditorWatcher);
    }


}
