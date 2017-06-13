package com.haitr.doge.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.haitr.doge.Constants;
import com.haitr.doge.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends BaseActivity {
    private static final String TAG = "EditActivity";

    EditText firstNameText, lastNameText, phoneText, addressText, birthdayText, passwordText, rePasswordText;
    Button saveButton;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String myFormat = "yyyy-MM-dd";                                                 //In which you need put your format here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
    boolean SAVED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSharedPreferences();

        firstNameText = (EditText) findViewById(R.id.input_first_name);
        lastNameText = (EditText) findViewById(R.id.input_last_name);
        phoneText = (EditText) findViewById(R.id.input_phone);
        addressText = (EditText) findViewById(R.id.input_address);
        birthdayText = (EditText) findViewById(R.id.input_DOB);
        passwordText = (EditText) findViewById(R.id.input_password);
        rePasswordText = (EditText) findViewById(R.id.input_password_confirm);
        saveButton = (Button) findViewById(R.id.btn_save);

        firstNameText.setText(FIRST_NAME);
        lastNameText.setText(LAST_NAME);
        phoneText.setText(PHONE);
        addressText.setText(ADDRESS);

        phoneText.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting; // this is a flag which prevents the  stack overflow.
            private int mAfter;
            String a;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here..
            }

            //called before the text is changed...
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to do here...
                mAfter  =   after; // flag to detect backspace..

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Make sure to ignore calls to afterTextChanged caused by the work done below
                if (!mFormatting) {
                    mFormatting = true;
                    // using US formatting...
                    if(mAfter!=0) {// in case back space ain't clicked...
//                        PhoneNumberUtils.formatNumber(s, PhoneNumberUtils.getFormatTypeForLocale(Locale.US));

//                        boolean flag = true;
//                        if (flag) {
//
//                            if (((s.length() + 1) % 4) == 0)
//                            {
//                                if (s.toString().split("-").length <= 2)
//                                {
//                                    phoneText.setText(s + "-");
//                                    phoneText.setSelection(s.length());
//                                }
//                            }
//                            a = s.toString();
//                        } else {
//                            phoneText.setText(a);
//                        }
                        if(s.length() == 3){
                            phoneText.setText("(" + s + ") ");
                            phoneText.setSelection(phoneText.getText().length());
                        } else if( s.length() == 9){
                            phoneText.setText(s + "-");
                            phoneText.setSelection(phoneText.getText().length());
                        }
                    }
                    mFormatting = false;
                }
            }
        });

        Log.d("birthday", BIRTHDAY);

        myCalendar = Calendar.getInstance();

        try {
            myCalendar.setTime(sdf.parse(BIRTHDAY));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateLabel();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthdayText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void updateLabel() {
        birthdayText.setText(sdf.format(myCalendar.getTime()));
    }

    public void saveChanges() {
        Log.d(TAG, "save changes");

        if (!validate()) {
            onSaveFailed();
            return;
        }

        saveButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(EditActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving Your Profile...");
        progressDialog.show();

        JSONObject obj = new JSONObject();

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String phone = phoneText.getText().toString();
        String address = addressText.getText().toString();
        String birth = birthdayText.getText().toString();
        String password = passwordText.getText().toString();

        Log.d(TAG,phone);

        try {
            obj.put("fname", firstName);
            obj.put("lname", lastName);
            obj.put("phone", phone);
            obj.put("address", address);
            obj.put("dob", birth);
            obj.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,obj.toString());

        // TODO: Implement your own saveChanges logic here.
        Ion.with(getApplicationContext())
                .load(Constants.BASE_URL + Constants.USER)
                .setBodyParameter("submit", "[" + obj.toString() + "]")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d(TAG, result);

                        if (result.equalsIgnoreCase(Constants.SUCCESS)) {
                            SAVED = true;
                            Ion.with(getApplicationContext())
                                    .load("GET", Constants.BASE_URL + Constants.ACCOUNT_PROFILE_URL)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {
                                            if (result.equalsIgnoreCase(Constants.ERROR))
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

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSaveSuccess or onSaveFailed
                        // depending on success
                        if (SAVED) onSaveSuccess();
                        else onSaveFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSaveSuccess() {
        saveButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(EditActivity.this, "Your profile was saved!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onSaveFailed() {
        Toast.makeText(getBaseContext(), "Save failed. Please try again.", Toast.LENGTH_LONG).show();

        saveButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String phone = phoneText.getText().toString();
        String address = addressText.getText().toString();
        String birth = birthdayText.getText().toString();
        String password = passwordText.getText().toString();
        String rePassword = rePasswordText.getText().toString();

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

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            phoneText.setError("enter a valid phone number");
            valid = false;
        } else {
            phoneText.setError(null);
        }

        if (address.isEmpty() || address.length() < 3) {
            addressText.setError("enter a valid address");
            valid = false;
        } else {
            addressText.setError(null);
        }

        if(birth.isEmpty()){
            birthdayText.setError("not input your birthday yet");
            valid = false;
        }else{
            birthdayText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (rePassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            rePasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if(!password.equals(rePassword)) {
            rePasswordText.setError("your password doesn't match");
            valid = false;
        }else{
            rePasswordText.setError(null);
        }

        return valid;
    }
}
