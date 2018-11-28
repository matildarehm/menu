package com.example.menuui;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PrivateLogin extends AppCompatActivity {
    private Button login_button;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_button = (Button) findViewById(R.id.login_button);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passText);

        final Context context = this;

        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession user_session, CognitoDevice new_device) {
                Log.i("login sucessful!", "snatch those tokens");
                go_to_landing();
            }

            @Override
            public void getAuthenticationDetails (AuthenticationContinuation continuation, String user_id) {
                Log.i("here", "in authenticate details");
                Log.i("pass", String.valueOf(password.getText()));
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(user_id, String.valueOf(password.getText()), null);
                continuation.setAuthenticationDetails(authenticationDetails);
                continuation.continueTask();

            }

            @Override
            public void getMFACode (MultiFactorAuthenticationContinuation mfa_continuation) {

            }

            @Override
            public void authenticationChallenge (ChallengeContinuation challenge_continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.i("didn't work", "failure"+ exception.getLocalizedMessage());

            }
        };

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("button clicked", "let'sgo");
                String user = username.getText().toString();
                String pass = password.getText().toString();

                Region REGION = Region.getRegion(Regions.US_EAST_2);

                AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(),
                        new ClientConfiguration());

                identityProviderClient.setRegion(REGION);
                CognitoUserPool userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, identityProviderClient);
                CognitoUser thisUser = userPool.getUser();

                thisUser.getSessionInBackground(authenticationHandler);


            }
        });
    }

    private void go_to_landing() {
        Intent landing_intent = new Intent(this, Landing.class);
        startActivity(landing_intent);
    }
}
