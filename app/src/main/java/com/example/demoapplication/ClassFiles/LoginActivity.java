package com.example.demoapplication.ClassFiles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Database.DatabaseHandler;
import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.AppPreferences;
import com.example.demoapplication.UtilClass.AppUtils;
import com.example.demoapplication.UtilClass.CameraController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final int CAMERA_PHOTO_REQUEST_CODE = 1;
    String sEnterUserLogin, sEnterPassword;
    CameraController cameraController;
    Typeface normaltypeFace;
    private EditText etEmail, ePwd;
    private TextView btLogin, signUpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        normaltypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/aller_regular.ttf");
        btLogin = (TextView) findViewById(R.id.btnLogIn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        ePwd = (EditText) findViewById(R.id.etPwd);
        signUpTv = (TextView) findViewById(R.id.signUp_tv);
        etEmail.setTypeface(normaltypeFace);
        ePwd.setTypeface(normaltypeFace);
        signUpTv.setTypeface(normaltypeFace, Typeface.BOLD);
        btLogin.setTypeface(normaltypeFace, Typeface.BOLD);

        //Sign Up
        signUpTv.setMovementMethod(LinkMovementMethod.getInstance());
        signUpTv.setHighlightColor(Color.TRANSPARENT);
        final int colorForThisClickableSpan = Color.BLUE;
        SpannableString content1 = new SpannableString(getResources().getString(R.string.sign_up_text));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(colorForThisClickableSpan);
            }
        };
        content1.setSpan(clickableSpan, 23, content1.length(), 0);
        content1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Cyan)),
                23, content1.length(), 0);
        signUpTv.setText(content1);
        signUpTv.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogIn:
                sEnterUserLogin = etEmail.getText().toString().trim();
                sEnterPassword = ePwd.getText().toString().trim();
                if (TextUtils.isEmpty(sEnterUserLogin)) {
                    etEmail.setError("Please enter your Email address!");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
                    etEmail.setError("Please enter a valid email address.");
                } else if (TextUtils.isEmpty(sEnterPassword)) {
                    ePwd.setError("Please enter your Password!");
                } else {
                    DatabaseHandler databaseHandler = new DatabaseHandler(LoginActivity.this);
                    int matched_integer = databaseHandler.matchRegisteration(etEmail.getText().toString().trim(),
                            ePwd.getText().toString().trim());
                    if (matched_integer == 0) {
                        Toast.makeText(LoginActivity.this, "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
                    } else if (matched_integer == 1) {
                        AppPreferences.setIsLogin(LoginActivity.this, AppUtils.ISLOGIN, true);
                        AppPreferences.setUserEmail(LoginActivity.this, AppUtils.EMAIL,
                                etEmail.getText().toString().trim().toLowerCase());
                        Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PHOTO_REQUEST_CODE)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

