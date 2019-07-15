package com.mad.thoughtExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePostActivity extends AppCompatActivity {

    private TextView textCounter;
    private EditText newPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        newPostContent = findViewById(R.id.newpost_content);
        textCounter = findViewById(R.id.text_counter);

        setTextWatcherForPostCounter();
    }

    private void setTextWatcherForPostCounter() {
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                String newCount = 200 - s.length() + " characters left";
                textCounter.setText(newCount);
            }

            public void afterTextChanged(Editable s) {
            }
        };

        newPostContent.addTextChangedListener(mTextEditorWatcher);
    }


}
