package com.example.menuui;

// JSON Simple imports for reading creds file
import android.content.Context;
import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;


class CognitoSettings implements Parcelable {

    private String user_email;
    private String user_name;
    private String user_pass;
    private String number;

    private String userPoolId;
    private String clientId;
    private String clientSecret;

    private Context setting_context;

    public void getCredentials(Context context) throws JSONException {
        setting_context = context;
        JSONParser parser = new JSONParser();
        try {
            InputStream input_json = context.getResources().openRawResource(R.raw.generalusers);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input_json,"UTF-8"),8);
            StringBuilder json_builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                json_builder.append(line + "\n");
            }
            input_json.close();
            String userpool_json = json_builder.toString();
            JSONObject pool_obj = new JSONObject(userpool_json);
            JSONObject user_obj = pool_obj.getJSONObject("general_users");

            String pool =  user_obj.getString("userPoolId");
            this.userPoolId = pool;

            String client = user_obj.getString("clientId");
            this.clientId = client;

            String secret = user_obj.getString("clientSecret");
            this.clientSecret = secret;
        }
        catch (IOException | JSONException  e) { e.printStackTrace(); }
    }

    public CognitoSettings(Parcel in) {
        user_name = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data into parcel
        dest.writeString(this.user_name);
    }

    // This is to de-serialize the object
    public static final Parcelable.Creator<CognitoSettings> CREATOR = new Parcelable.Creator<CognitoSettings>(){
        public CognitoSettings createFromParcel(Parcel in) {
            return new CognitoSettings(in);
        }

        public CognitoSettings[] newArray(int size) {
            return new CognitoSettings[size];
        }
    };

    public String getEmail() { return this.user_email; }
    public String getUsername() { return this.user_name; }
    public String getPassword() { return this.user_pass; }

    public void setEmail(String email) { this.user_email = email; }
    public void setUsername(String user) { this.user_name = user; }
    public void setPhone(String phone_number) {
        this.number = phone_number;
        isFake(this.number);
    }
    public Boolean isFake(String phone) {
        return phone.equals("+17185647598");
    }

    public void getuserEntry (String pass_word) {
        storePassword(pass_word);
    }

    private void storePassword(String password) {
        this.user_pass = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password) + "MENU";
    }

    public String getUserPoolId() { return this.userPoolId; }
    public String getClientId() { return this.clientId; }
    public String getClientSecret() { return this.clientSecret; }

    public CognitoUserPool getUserPool() {
        Regions cognitoRegion = Regions.US_EAST_2;
        return new CognitoUserPool(this.setting_context, this.userPoolId, this.clientId, this.clientSecret, cognitoRegion);
    }
}