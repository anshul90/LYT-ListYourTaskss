package com.example.demoapplication.ClassFiles;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Database.DatabaseHandler;
import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.AppPreferences;
import com.example.demoapplication.UtilClass.AppUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 29;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 28;
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_CODE = 2;
    Typeface normaltypeFace;
    CircularImageView circularImageView;
    boolean imageFromGallery;
    String imagePath;
    Uri outPutfileUri;
    private TextView title, tv_add_photo;
    private EditText firstNameETv;
    private EditText lastNameETv;
    private EditText emailETv;
    private EditText userPasswordETv;
    private EditText userConfirmPasswordETv;
    private TextView tvSignup;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private boolean IS_VALIDATEFIELD = true;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Permission Granted");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_CODE);
                    // initializeView();
                } else {
                    Log.d("Home", "Permission Failed");
                    Toast.makeText(this, "You must allow permission to your mobile device.", Toast.LENGTH_SHORT).show();
                    // finish();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                Log.i("Camera", "G : " + grantResults[0]);
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    final String dir = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + "/picFolder/";
                    File newdir = new File(dir);
                    if (!newdir.exists()) {
                        newdir.mkdir();
                    }
                    File[] files = newdir.listFiles();

                    int count = 0;
                    if (files == null) {
                        count = 0;
                    } else {
                        count = files.length;
                    }
                    count++;
                    String file_str = dir + count + ".jpg";
                    File newfile = new File(file_str);
                    try {
                        newfile.createNewFile();
                    } catch (IOException e) {
                    }

                    Uri fileUri = Uri.fromFile(newfile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        imagePath = fileUri.getPath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);

                    } else {
                        imagePath = fileUri.getPath();
                        File file = new File(fileUri.getPath());
                        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "You must allow permission to your mobile device.", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
                break;
            }
            // Add additional cases for other permissions you may have asked for
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("reached", "reached onActivityResult");
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                // Log.e(TAG, "Image from Gallery");
                try {
                    // recyle unused bitmaps
                    Uri uri = data.getData();
                    Log.e("uri", "" + uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), uri);
                    Log.e("uri", "" + uri);
                    Log.e("uri", "" + bitmap);
                    circularImageView.setImageBitmap(bitmap);
                    if (data.getData().toString().contains("document")) {
                        imagePath = getRealPathFromDocumentUri(data.getData());
                    } else {
                        imagePath = getRealPathFromURI(data.getData());
                    }
                    imageFromGallery = true;
                    //    uploadImage();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE
                    && resultCode == Activity.RESULT_OK) {

                String realPath = imagePath;// AppPreference

                // .getCapturedImagePath(AppUtils.CAPTURED_IMAGE_PATH);
                Log.e("TAG", "Path: " + realPath);
                int targetW = circularImageView.getWidth();
                int targetH = circularImageView.getHeight();
                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(realPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                //  Bitmap myBitmap = BitmapFactory.decodeSampledBitmapFromResource(image.getAbsolutePath(), 540, 360);
                Bitmap myBitmap = BitmapFactory.decodeFile(realPath, options);
                //Bitmap bitmap = BitmapFactory.decodeFile(realPath, bmOptions);
                Log.e("TAG", "Path: " + myBitmap);

                try {
                    ExifInterface ei = new ExifInterface(realPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            circularImageView.setImageBitmap(rotateImage(myBitmap, 90));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            circularImageView.setImageBitmap(rotateImage(myBitmap, 180));
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            circularImageView.setImageBitmap(rotateImage(myBitmap, 270));
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                            circularImageView.setImageBitmap(myBitmap);
                            break;
                        default:
                            circularImageView.setImageBitmap(myBitmap);
                            break;
                    }

                } catch (Exception e) {
                    Log.e("TAG", "Exception: " + e);
                }
                // uploadImage();
            }
        } else {
            imagePath = "";
        }
    }

    public String getRealPathFromDocumentUri(Uri uri) {
        String filePath = "";
        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            //Log.e(ImageConverter.class.getSimpleName(), "ID for requested image not found: " + uri.toString());
            return filePath;
        }
        String imgId = m.group();

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{imgId}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null,
                null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        normaltypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/aller_regular.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        firstNameETv = (EditText) findViewById(R.id.firstNameETv);
        tv_add_photo = (TextView) findViewById(R.id.tv_add_photo);
        lastNameETv = (EditText) findViewById(R.id.lastNameETv);
        emailETv = (EditText) findViewById(R.id.emailETv);
        userPasswordETv = (EditText) findViewById(R.id.userPasswordETv);
        userConfirmPasswordETv = (EditText) findViewById(R.id.userConfirmPasswordETv);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        circularImageView = (CircularImageView) findViewById(R.id.circularImageView);
        //Typefaces
        title.setTypeface(normaltypeFace);
        firstNameETv.setTypeface(normaltypeFace);
        lastNameETv.setTypeface(normaltypeFace);
        emailETv.setTypeface(normaltypeFace);
        userPasswordETv.setTypeface(normaltypeFace);
        userConfirmPasswordETv.setTypeface(normaltypeFace);
        tvSignup.setTypeface(normaltypeFace, Typeface.BOLD);
        tv_add_photo.setTypeface(normaltypeFace, Typeface.BOLD);
        //ONClick
        tvSignup.setOnClickListener(this);
        circularImageView.setOnClickListener(this);
        tv_add_photo.setOnClickListener(this);
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firstNameETv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // position the text type in the left top corner
                    firstNameETv.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    firstNameETv.setGravity(Gravity.CENTER);
                }
            }
        });
        lastNameETv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // position the text type in the left top corner
                    lastNameETv.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    lastNameETv.setGravity(Gravity.CENTER);
                }
            }
        });
        emailETv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // position the text type in the left top corner
                    emailETv.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    emailETv.setGravity(Gravity.CENTER);
                }
            }
        });
        userPasswordETv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // position the text type in the left top corner
                    userPasswordETv.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    userPasswordETv.setGravity(Gravity.CENTER);
                }
            }
        });
        userConfirmPasswordETv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // position the text type in the left top corner
                    userConfirmPasswordETv.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    userConfirmPasswordETv.setGravity(Gravity.CENTER);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean validateField() {
        if (firstNameETv.getText().toString().equals("")) {
            IS_VALIDATEFIELD = false;
            firstNameETv.setError("Please enter your first name.");
            return IS_VALIDATEFIELD;
        } /*else if (lastNameETv.getText().toString().equals("")) {
            IS_VALIDATEFIELD = false;
            alertDialogGlobal.openDialog(this, "Please enter your last name.");
            return IS_VALIDATEFIELD;
        }*/ else if (emailETv.getText().toString().equals("")) {
            IS_VALIDATEFIELD = false;
            emailETv.setError("Please enter your email address.");
            return IS_VALIDATEFIELD;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailETv.getText().toString().trim()).matches()) {
            IS_VALIDATEFIELD = false;
            emailETv.setError("Please enter a valid email address.");
            return IS_VALIDATEFIELD;
        } else if (userPasswordETv.getText().toString().equals("")) {
            IS_VALIDATEFIELD = false;
            userPasswordETv.setError("Please enter your password.");
            return IS_VALIDATEFIELD;
        } else if (userConfirmPasswordETv.getText().toString().equals("")) {
            IS_VALIDATEFIELD = false;
            userConfirmPasswordETv.setError("Please enter confirm password.");
            return IS_VALIDATEFIELD;
        } else if (!userConfirmPasswordETv.getText().toString().equals(userPasswordETv.getText().toString())) {
            IS_VALIDATEFIELD = false;
            userConfirmPasswordETv.setError("Your password doesn't match with confirm password.");
            return IS_VALIDATEFIELD;
        } else if (userPasswordETv.getText().toString().trim().length() < 5) {
            IS_VALIDATEFIELD = false;
            userPasswordETv.setError("Your password's length should be of 5 digits atleast.");
            return IS_VALIDATEFIELD;
        } /*else if (imagePath == null || imagePath.equals("")) {
            IS_VALIDATEFIELD = false;
            alertDialogGlobal.openDialog(this, "Please select a profile image.");
            return IS_VALIDATEFIELD;
        }*/
        return IS_VALIDATEFIELD;
    }

    @Override
    public void onClick(View v) {
        if (v == tvSignup) {
            IS_VALIDATEFIELD = true;
            if (validateField()) {
                DatabaseHandler databaseHandler = new DatabaseHandler(SignUpActivity.this);
                int new_email = databaseHandler.getEmailValidation(emailETv.getText().toString().trim());
                if (new_email == 1) {
                    Toast.makeText(SignUpActivity.this, "This email address already exists.", Toast.LENGTH_SHORT).show();
                } else if (new_email == 0) {
                    databaseHandler.insertNewUser(firstNameETv.getText().toString().trim().toLowerCase(),
                            lastNameETv.getText().toString().trim().toLowerCase(),
                            emailETv.getText().toString().trim().toLowerCase(),
                            userPasswordETv.getText().toString().trim(),
                            imagePath);
                    Toast.makeText(SignUpActivity.this, "Your account has been created successfully.", Toast.LENGTH_SHORT).show();
                    AppPreferences.setIsLogin(SignUpActivity.this, AppUtils.ISLOGIN, true);
                    AppPreferences.setUserFirstName(SignUpActivity.this, AppUtils.FIRST_NAME, firstNameETv.getText().toString().trim());
                    AppPreferences.setUserLastName(SignUpActivity.this, AppUtils.LAST_NAME, lastNameETv.getText().toString().trim());
                    AppPreferences.setUserEmail(SignUpActivity.this, AppUtils.EMAIL, emailETv.getText().toString().trim().toLowerCase());
                    AppPreferences.setPassword(SignUpActivity.this, AppUtils.PASSWORD, userPasswordETv.getText().toString().trim());
                    AppPreferences.setUserProfilePic(SignUpActivity.this, AppUtils.PROFILE_IMAGE, imagePath);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SignUpActivity.this.startActivity(intent);
                    ((Activity) SignUpActivity.this).finish();
                }
            }
        } else if (v == tv_add_photo) {
            AlertDialog alertDialog;
            AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(
                    this);
            // set dialog message
            alertDialogBuilderr
                    .setTitle(R.string.choose_profile_pic)
                    .setCancelable(true)
                    .setPositiveButton(R.string.open_camera,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                        } else {

                                            final String dir = Environment
                                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                    + "/picFolder/";
                                            File newdir = new File(dir);
                                            if (!newdir.exists()) {
                                                newdir.mkdir();
                                            }
                                            File[] files = newdir.listFiles();

                                            int count = 0;
                                            if (files == null) {
                                                count = 0;
                                            } else {
                                                count = files.length;
                                            }
                                            count++;
                                            String file_str = dir + count + ".jpg";
                                            File newfile = new File(file_str);
                                            try {
                                                newfile.createNewFile();
                                            } catch (IOException e) {
                                            }

                                            Uri fileUri = Uri.fromFile(newfile);

                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                                imagePath = fileUri.getPath();
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                            } else {
                                                imagePath = fileUri.getPath();
                                                File file = new File(fileUri.getPath());
                                                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                            }
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                            }
                                        }
                                    } else {

                                        final String dir = Environment
                                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                + "/picFolder/";
                                        File newdir = new File(dir);
                                        if (!newdir.exists()) {
                                            newdir.mkdir();
                                        }
                                        File[] files = newdir.listFiles();

                                        int count = 0;
                                        if (files == null) {
                                            count = 0;
                                        } else {
                                            count = files.length;
                                        }
                                        count++;
                                        String file_str = dir + count + ".jpg";
                                        File newfile = new File(file_str);
                                        try {
                                            newfile.createNewFile();
                                        } catch (IOException e) {
                                        }

                                        Uri fileUri = Uri.fromFile(newfile);

                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                            imagePath = fileUri.getPath();
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                        } else {
                                            imagePath = fileUri.getPath();
                                            File file = new File(fileUri.getPath());
                                            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                        }
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                        }
                                    }
                                }
                            })
                    .setNegativeButton(R.string.choose_from_gallery,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                        } else {
                                            Log.d("Home", "Already granted access");
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            startActivityForResult(intent, REQUEST_CODE);
                                            //initializeView();
                                        }
                                    } else {
                                        // initializeView();
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }
                                }
                            });
            // create alert dialog
            alertDialog = alertDialogBuilderr.create();
            // show it
            alertDialog.show();
        } else if (v == circularImageView) {
            AlertDialog alertDialog;
            AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(
                    this);
            // set dialog message
            alertDialogBuilderr
                    .setTitle(R.string.choose_profile_pic)
                    .setCancelable(true)
                    .setPositiveButton(R.string.open_camera,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                        } else {

                                            final String dir = Environment
                                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                    + "/picFolder/";
                                            File newdir = new File(dir);
                                            if (!newdir.exists()) {
                                                newdir.mkdir();
                                            }
                                            File[] files = newdir.listFiles();

                                            int count = 0;
                                            if (files == null) {
                                                count = 0;
                                            } else {
                                                count = files.length;
                                            }
                                            count++;
                                            String file_str = dir + count + ".jpg";
                                            File newfile = new File(file_str);
                                            try {
                                                newfile.createNewFile();
                                            } catch (IOException e) {
                                            }

                                            Uri fileUri = Uri.fromFile(newfile);

                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                                imagePath = fileUri.getPath();
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                            } else {
                                                imagePath = fileUri.getPath();
                                                File file = new File(fileUri.getPath());
                                                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                            }
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                            }
                                        }
                                    } else {

                                        final String dir = Environment
                                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                + "/picFolder/";
                                        File newdir = new File(dir);
                                        if (!newdir.exists()) {
                                            newdir.mkdir();
                                        }
                                        File[] files = newdir.listFiles();

                                        int count = 0;
                                        if (files == null) {
                                            count = 0;
                                        } else {
                                            count = files.length;
                                        }
                                        count++;
                                        String file_str = dir + count + ".jpg";
                                        File newfile = new File(file_str);
                                        try {
                                            newfile.createNewFile();
                                        } catch (IOException e) {
                                        }

                                        Uri fileUri = Uri.fromFile(newfile);

                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                            imagePath = fileUri.getPath();
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                        } else {
                                            imagePath = fileUri.getPath();
                                            File file = new File(fileUri.getPath());
                                            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                                            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                                        }
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                        }
                                    }
                                }
                            })
                    .setNegativeButton(R.string.choose_from_gallery,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                        } else {
                                            Log.d("Home", "Already granted access");
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            startActivityForResult(intent, REQUEST_CODE);
                                            //initializeView();
                                        }
                                    } else {
                                        // initializeView();
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }
                                }
                            });
            // create alert dialog
            alertDialog = alertDialogBuilderr.create();
            // show it
            alertDialog.show();
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (photoURI != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = "file:" + image.getAbsolutePath();
        return image;
    }
}
