package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.os.Parcel;

import android.util.Log;
import android.widget.EditText;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;

import org.json.JSONException;

public class Login extends AppCompatActivity {
    private Button login_button;
    private TextView fyp_text;
    private EditText username;
    private EditText password;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_button = (Button) findViewById(R.id.login_button);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passText);

        fyp_text = (TextView) findViewById(R.id.fyp);
        fyp_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendToFYP();
            }
        });

        hideNavBar();

        final Context context = this;

        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {


            @Override
            public void onSuccess(CognitoUserSession user_session, CognitoDevice new_device) {
                Log.i("login successful!", "snatch those tokens");
                go_to_landing();
            }

            @Override
            public void getAuthenticationDetails (AuthenticationContinuation continuation, String user_id) {
                Log.i("here", "in authenticate details");
                Log.i("pass", pass);
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(user_id, pass, null);
                continuation.setAuthenticationDetails(authenticationDetails);
                continuation.continueTask();

            }

            @Override
            public void getMFACode (MultiFactorAuthenticationContinuation mfa_continuation) { }

            @Override
            public void authenticationChallenge (ChallengeContinuation challenge_continuation) { }

            @Override
            public void onFailure(Exception exception) {
                Log.i("didn't work", "failure"+ exception.getLocalizedMessage());

            }
        };

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Login button clicked", "let'sgo");
                String user = username.getText().toString();
                Log.i("Login button clicked", user);
                pass = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password.getText().toString()) + "MENU";

                Region REGION = Region.getRegion(Regions.US_EAST_2);

                AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(),
                        new ClientConfiguration());

                identityProviderClient.setRegion(REGION);

                Parcel user_input = Parcel.obtain();
                user_input.writeString(user);

                CognitoSettings user_creds = new CognitoSettings(user_input);

                try { user_creds.getCredentials(context); }
                catch (JSONException e) { e.printStackTrace(); }

                String userPoolId = user_creds.getUserPoolId();
                String clientId = user_creds.getClientId();
                String clientSecret = user_creds.getClientSecret();

                CognitoUserPool userPool = user_creds.getUserPool();
                CognitoUser thisUser = userPool.getUser(user);

                thisUser.getSessionInBackground(authenticationHandler);

                // save username as current user
                ((MenuApp) Login.this.getApplication()).setCurrentUser(user);

                // get the user favorite restaurants hashmap from shared preferences
                ((MenuApp) Login.this.getApplication()).getHashMap();

            }
        });
    }

    private void go_to_landing() {
        Intent landing_intent = new Intent(this, Landing.class);
        startActivity(landing_intent);
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
