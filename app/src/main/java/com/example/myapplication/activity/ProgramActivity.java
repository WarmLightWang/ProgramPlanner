package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.engine.Validation;

public class ProgramActivity extends AppCompatActivity {

    Validation validation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {
        //if the view is not EditText, hide keyboard
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard(v);
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public boolean checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            System.out.println(wifiInfo.toString());
            return wifiInfo.getLinkSpeed() != WifiInfo.LINK_SPEED_UNKNOWN && wifiInfo.getLinkSpeed() != 0;
        } else {
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
    }

    public void setValidation(String username, String projectId) {
        validation = new Validation(username, projectId);
    }

    public boolean validateWifi(boolean isBack) {
        if (!checkWifi()) {
            if (isBack)
                onBackPressed();
            else {
                String message = "No Connection. Please check your internet connection";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    public boolean validateRole() {
        return validateRole(false);
    }

    public boolean validateRole(boolean isBack) {
        boolean isValid = true;
        String message = null;
        if (!validateWifi(isBack))
            return false;
        if (validation.isExist()) {
            String roles = validation.getRoles();
            if (roles.equalsIgnoreCase("client")) {
                message = "Your role has been altered";
                isValid = false;
            }
        } else {
            message = "You have been kicked out of the project !";
            isValid = false;
        }
        if (!isValid) {
            backToProjectPage(message);
        }
        return isValid;
    }

    private void backToProjectPage(String message) {
        if (message == null) {
            message = "Encountered unexpected error";
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProjectMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
