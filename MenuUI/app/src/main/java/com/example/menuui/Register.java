package com.example.menuui;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.content.Intent;

// AWS Cognito Imports
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

import org.json.JSONException;

public class Register extends AppCompatActivity {
    private Button register_button;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = (TextInputEditText) findViewById(R.id.input_name);
        email = (TextInputEditText) findViewById(R.id.input_email);
        password = (TextInputEditText) findViewById(R.id.input_password);

        register_button = (Button) findViewById(R.id.btn_signup);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_user();
        }
    });
}
    private void register_user() {
        String user_name = username.getText().toString();
        String user_email = email.getText().toString();
        String user_pass = password.getText().toString();

        Parcel user_input = Parcel.obtain();
        user_input.writeString(user_name);

        CognitoSettings user_creds = new CognitoSettings(user_input);

        try { user_creds.getCredentials(this); }
        catch (JSONException e) { e.printStackTrace(); }

        user_creds.getuserEntry(user_pass);
        user_creds.setUsername(user_name);
        user_creds.setEmail(user_email);

        CognitoUserAttributes user_attributes = new CognitoUserAttributes();

        //  String fake_number = "+17185647598";
        //  user_attributes.addAttribute("phone_number", fake_number);
        user_attributes.addAttribute("given_name",user_name);
        user_attributes.addAttribute("email",user_email);

        String userPoolId = user_creds.getUserPoolId();
        String clientId = user_creds.getClientId();
        String clientSecret = user_creds.getClientSecret();
        Region REGION = Region.getRegion(Regions.US_EAST_2);

        Log.i("tagyoureit", userPoolId);
        Log.i("tagyoureit", clientId);
        Log.i("tagyoureit", clientSecret);

        AmazonCognitoIdentityProviderClient identityProviderClient =
                new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(),
                                                        new ClientConfiguration());
        identityProviderClient.setRegion(REGION);
        CognitoUserPool userPool = new CognitoUserPool(this, userPoolId, clientId,
                                                       clientSecret, identityProviderClient);

        handleSignUp(userPool, user_creds, user_attributes);
    }


    public void handleSignUp(CognitoUserPool userPool, final CognitoSettings user_credentials,
                             CognitoUserAttributes user_attr) {

        final String user_name = user_credentials.getUsername();
        final String user_pass = user_credentials.getPassword();

        SignUpHandler signupCallback = new SignUpHandler() {
            final String REGISTER_TAG = "Register Confirmation";

            @Override
            public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed,
                                  CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {

                // Sign-up was successful
                Log.i(REGISTER_TAG, "isSignedUp: " + userConfirmed);

                // Check if this user (cognitoUser) needs to be confirmed
                if (!userConfirmed) {
                    Log.i(REGISTER_TAG, "isUserConfirmed: "
                            + cognitoUserCodeDeliveryDetails.getDestination());

                    // This user needs to be confirmed and a confirmation code was sent to the user
                    // cognitoUserCodeDeliveryDetails will display email that code was sent to
                    // Get the confirmation code from user
                    verify_user (user_name);
                }
                else { Log.i(REGISTER_TAG, "User has previously been confirmed!"); }
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(REGISTER_TAG, "User was not confirmed because: "
                        + exception.getLocalizedMessage());
            }
        };

        userPool.signUpInBackground(user_name, user_pass, user_attr, null, signupCallback);
    }

    public void verify_user(String username) {
        Intent verify_intent = new Intent(this, VerifyUser.class);
        verify_intent.putExtra("username", username);
        startActivity(verify_intent);
    }
}
