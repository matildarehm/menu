package com.example.menuui;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

public class CredentialClass {

    public CognitoCachingCredentialsProvider getcredentials(Context context) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-2:97e55140-b357-4790-ac02-48404aa723ce", // Identity pool ID
                Regions.US_EAST_2 // Region
        );

        return credentialsProvider;

    }
}
