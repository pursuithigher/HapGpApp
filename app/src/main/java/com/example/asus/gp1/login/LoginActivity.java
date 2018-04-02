package com.example.asus.gp1.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.gp1.activities.MainActivity;
import com.example.asus.gp1.R;
import com.example.asus.gp1.activities.RegistClassIdActivity;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.helper.RequestUtil;
import com.example.asus.gp1.task.UserLoginTask;
import com.example.asus.gp1.utils.DialogHelper;
import com.example.asus.gp1.utils.ProfileQuery;
import com.google.android.gms.plus.PlusOneButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,LoginHandler.OnLoginCallBack, OnClickListener {

    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView,loginIp,loginPort;
    private View signIn;
    //private View mProgressView;
    private DialogHelper dialogHelper;

    private boolean isNetWorkOpen=true;

    private boolean SkipLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        Button mEmailSignInButton = (Button) findViewById(R.id.email_login);
        mEmailSignInButton.setOnClickListener(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        loginIp = findViewById(R.id.login_et1);
        loginPort = findViewById(R.id.login_et2);
        mPasswordView = (EditText) findViewById(R.id.password);
        signIn = findViewById(R.id.email_signin);
        signIn.setOnClickListener(this);
        dialogHelper = new DialogHelper();
        initData();
    }

    private void initData() {
        populateAutoComplete();
    }

    @Override
    public void onReuqestComplete() {
        showProgress(false);
    }

    @Override
    public void onLoginResponse(boolean hasNetwork) {
        isNetWorkOpen = hasNetwork;
    }

    @Override
    public void onLoginIdNotFind() {
        Intent in = new Intent(this, RegistClassIdActivity.class);
        startActivity(in);
    }

    @Override
    public void onLoginSuccess(JSONObject json,String userName,String passWord) throws JSONException {
        MetaData.isLogin=true;
        MetaData.LID = userName;
        MetaData.PWD = passWord;
        MetaData.LoginToken=(String)json.get("userLoginToken");
        JSONObject ext=(JSONObject)json.get("extResult");
        MetaData.Name=(String)ext.get("Name");
        MetaData.Role=(String)ext.get("Role");
        Intent in = new Intent(this, MainActivity.class);
        startActivity( in );
    }

    @Override
    public void onHandleError() {
        //b[0] =true;
        showProgress(false);
        Toast.makeText(this, R.string.request_failed, Toast.LENGTH_SHORT).show();

        if(SkipLogin) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity( in );
        }
    }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * 登陆
     */
    private void performLogin() {
        RequestUtil.IP = loginIp.getText().toString();
        RequestUtil.Port = loginPort.getText().toString();
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, this);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {
        if(show) {
            dialogHelper.createProcessDialog(this, "").show(false, true);
        }else{
            dialogHelper.dismiss();
        }
    }



    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_signin:
                Intent in = new Intent(this, RegistClassIdActivity.class);
                startActivity(in);
                break;
            case R.id.email_login:
                performLogin();
                break;
        }
    }
}

