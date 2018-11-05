package com.example.menuui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;

public class PrivateRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bundle user_credentials = getIntent().getExtras();
        final String user_name = user_credentials.getString("username");
        final String user_email  = user_credentials.getString("email");
        final String user_pass = user_credentials.getString("password");

        CognitoUserAttributes user_attributes = new CognitoUserAttributes();
        String fake_number = "+17185647598";

        user_attributes.addAttribute("phone_number", fake_number);
        user_attributes.addAttribute("given_name",user_name);
        user_attributes.addAttribute("email",user_email);

        String userPoolId = "";
        String clientId = "";
        String clientSecret = "";
        Region REGION = Region.getRegion(Regions.US_EAST_2);

        AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(),
                                                                                                             new ClientConfiguration());

        identityProviderClient.setRegion(REGION);
        CognitoUserPool userPool = new CognitoUserPool(this, userPoolId, clientId, clientSecret, identityProviderClient);
        SignUpHandler signupCallback = new SignUpHandler() {

            @Override
            public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                // Sign-up was successful
                Log.i("user confirmation: ", "confirmation" + userConfirmed);


                // Check if this user (cognitoUser) needs to be confirmed
                if(!userConfirmed) {
                    Log.i("user confirmation: ", "confirmation" + cognitoUserCodeDeliveryDetails.getDestination());
                    // This user must be confirmed and a confirmation code was sent to the user
                    // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
                    // Get the confirmation code from user
                    verify_user(user_name);
                }
                else {
                    Log.i("user confirmation", "signup success confirmed");
                    // The user has already been confirmed
                }
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i("user confirmation", "signup success confirmed"+ exception.getLocalizedMessage());
            }
        };
        userPool.signUpInBackground(user_name, user_pass, user_attributes, null, signupCallback);
    }

    private void verify_user(String user) {
        Intent verify_intent = new Intent(this, VerifyUser.class);
        verify_intent.putExtra("username", user);
        startActivity(verify_intent);
    }
}
