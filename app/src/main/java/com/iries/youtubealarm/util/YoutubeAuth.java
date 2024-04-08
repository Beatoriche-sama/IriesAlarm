package com.iries.youtubealarm.util;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

import java.util.Collections;

public class YoutubeAuth {
    private static final String SCOPE = "https://www.googleapis.com/auth/youtube";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static GoogleSignInClient getSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(SCOPE))
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(context, gso);
    }

    public static YouTube getYoutube(Context context) {
        GoogleAccountCredential credential = authorize(context);
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Youtube")
                .build();
    }

    private static GoogleAccountCredential authorize(Context context) {
        GoogleSignInAccount signedInAccount
                = GoogleSignIn.getLastSignedInAccount(context);

        Account account = signedInAccount.getAccount();
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                context,
                Collections.singleton(SCOPE));
        credential.setSelectedAccount(account);
        return credential;
    }
}
