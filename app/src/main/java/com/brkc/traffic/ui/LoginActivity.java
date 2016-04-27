package com.brkc.traffic.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.brkc.traffic.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "BRKC_LoginActivity";
    private EditText editTextAccount;
    private EditText editTextPassword;
    private Button buttonLogin;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox_remenber_password;


    private static final String PREF_IS_REMEMBER = "login_remember_password";
    private static final String PREF_ACCOUNT = "login_account";
    private static final String PREF_PASSWORD = "login_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);

        editTextAccount = (EditText)findViewById(R.id.account);
        editTextPassword = (EditText)findViewById(R.id.password);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkBox_remenber_password = (CheckBox)findViewById(R.id.checkbox_remember_password);

        boolean is_remember = sharedPreferences.getBoolean(PREF_IS_REMEMBER,false);
        if(is_remember){
            checkBox_remenber_password.setChecked(true);
            String account = sharedPreferences.getString(PREF_ACCOUNT,"");
            String password = sharedPreferences.getString(PREF_PASSWORD,"");
            editTextAccount.setText(account);
            editTextPassword.setText(password);
        }

        buttonLogin = (Button)findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = editTextAccount.getText().toString();
                String password = editTextPassword.getText().toString();
                if (account.equals("admin") && password.equals("password")) {
                    // remember account and password
                    editor = sharedPreferences.edit();
                    if (checkBox_remenber_password.isChecked()) {
                        editor.putString(PREF_ACCOUNT, account);
                        editor.putString(PREF_PASSWORD, password);
                        editor.putBoolean(PREF_IS_REMEMBER, true);
                    } else {
                        editor.clear();
                    }
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
