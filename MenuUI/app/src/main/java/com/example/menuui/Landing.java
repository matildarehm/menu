package com.example.menuui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

public class Landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        findViewById(R.id.linLayout).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search for food, restaurants, ...");
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
    }
}
