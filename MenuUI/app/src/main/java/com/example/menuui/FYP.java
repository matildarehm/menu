package com.example.menuui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FYP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fyp);

        hideNavBar();
    }

    public void hideNavBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |

                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        );
    }
}


