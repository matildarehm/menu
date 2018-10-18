package com.example.menuui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private TextView fyp_text;
    Typeface login_button_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        fyp_text = (TextView) findViewById(R.id.fyp);
        fyp_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendToFYP();
            }
        });

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

    public void sendToFYP() {
        Intent fyp_intent = new Intent(this, FYP.class);
        startActivity(fyp_intent);

    }
}
