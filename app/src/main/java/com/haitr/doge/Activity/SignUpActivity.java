package com.haitr.doge.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haitr.doge.Constants;
import com.haitr.doge.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity {
    private static final String TAG = "SignupActivity";

    EditText firstNameText, lastNameText, emailText, passwordText;
    Button signUpButton;
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameText = (EditText) findViewById(R.id.input_first_name);
        lastNameText = (EditText) findViewById(R.id.input_last_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        signUpButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    public void signUp() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String first_name = firstNameText.getText().toString();
        String last_name = lastNameText.getText().toString();
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own signUp logic here.
        Ion.with(getApplicationContext())
                .load(Constants.BASE_URL + Constants.SIGN_UP_URL)
                .setBodyParameter("fname", first_name)
                .setBodyParameter("lname", last_name)
                .setBodyParameter("email", email)
                .setBodyParameter("pass", password)
                .setBodyParameter(Constants.SIGN_UP, Constants.SIGN_UP)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d(TAG, result);

                        if (result.equalsIgnoreCase(Constants.SUCCESS)) {
                            EMAIL = email;
                            IS_LOGIN = true;
                            savePreferences();
                            Ion.with(getApplicationContext())
                                    .load("GET", Constants.BASE_URL + Constants.ACCOUNT_PROFILE_URL)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            if(result.equalsIgnoreCase(Constants.ERROR))
                                                return;
                                            try {
                                                JSONArray jsonArray = new JSONArray(result);
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    FIRST_NAME = jsonObject.getString(Constants.FIRST_NAME);
                                                    LAST_NAME = jsonObject.getString(Constants.LAST_NAME);
                                                    PHONE = jsonObject.getString(Constants.PHONE);
                                                    ADDRESS = jsonObject.getString(Constants.ADDRESS);
                                                    BIRTHDAY = jsonObject.getString(Constants.BIRTHDAY);
                                                }
                                                savePreferences();
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignUpSuccess or onSignUpFailed
                        // depending on success
                        if(IS_LOGIN) onSignUpSuccess(); else onSignUpFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignUpSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(SignUpActivity.this, "Welcome " + LAST_NAME + " " + FIRST_NAME + "!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed. Please try again.", Toast.LENGTH_LONG).show();

        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
