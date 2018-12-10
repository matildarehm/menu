package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FYP extends AppCompatActivity {
    private Button reset_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fyp);

        hideNavBar();
        reset_pass = (Button) findViewById(R.id.fyp_button);
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_back();

            }
        });
    }

    public void hideNavBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |

                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        );
    }

    private void go_back() {
        Intent welcome_intent = new Intent(this, MainActivity.class);
        startActivity(welcome_intent);
    }
}


