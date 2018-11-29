package com.example.menuui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raycoarana.codeinputview.CodeInputView;

// AWS Client
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

// AWS Cognito imports
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.raycoarana.codeinputview.CodeInputView;

public class VerifyUser extends AppCompatActivity {
    private Button verify_button;
    private CodeInputView code;
    Context context = this;
    private AnimationDrawable cutlery_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        Bundle user_credentials = getIntent().getExtras();
        final String user_name = user_credentials.getString("username");
        code = (CodeInputView) findViewById(R.id.code_verify); ;

        ImageView fork_spoon = (ImageView) findViewById(R.id.verify_header);
        fork_spoon.setImageResource(R.drawable.verify_anim);
        cutlery_anim = (AnimationDrawable) fork_spoon.getDrawable();

        verify_button = (Button) findViewById(R.id.verify_pass);
        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verify_code = code.getCode();
                new Confirm().execute(verify_code, user_name);

            }
        });
        TextView resend_verification = (TextView) findViewById(R.id.resend_verify);
        resend_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    private class Confirm extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            final String[] result = new String[1];
            final GenericHandler confirmationCallback = new GenericHandler() {
                @Override
                public void onSuccess() {
                    result[0] = "Succeeded!";
                }

                @Override
                public void onFailure(Exception exception) {
                    result[0] = "Failed!" + exception.getMessage();
                }
            };

            Region REGION = Region.getRegion(Regions.US_EAST_2);

            AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(),
                    new ClientConfiguration());

            identityProviderClient.setRegion(REGION);
            CognitoUserPool userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, identityProviderClient);
            CognitoUser cognito_user = userPool.getUser(strings[1]);
            cognito_user.confirmSignUp(strings[0], false, confirmationCallback);

            return result[0];

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("confirm",  "confirm result");
            go_to_landing();

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        cutlery_anim.start();
    }

    public void go_to_landing() {
        Log.i("here", "I get here.");
        Intent landing_intent = new Intent(this, Landing.class);
        startActivity(landing_intent);
    }


}
