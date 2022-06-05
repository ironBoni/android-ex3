package com.ex3.androidchat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ex3.androidchat.databinding.ActivityRegisterBinding;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;


public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    IUserService userService;
    EditText txtUserIdR, txtPasswordR, txtNickname, txtConfirm;
    private ImageView imageView;

    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Register");
        dialog.setMessage("Creating new account for you.");
        Button button = (Button) findViewById(R.id.btnRegister);
        Button uploadPic = (Button) findViewById(R.id.btnUploadProfile);
        imageView = (ImageView) findViewById(R.id.imageView);
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                if (!hasEnteredAllFields()) return;
                if (!isValidUser(txtUserIdR.getText().toString())) return;
                if (!isValidPassword(txtPasswordR.getText().toString())) return;
                if (!isValidConfirmPassword(txtPasswordR.getText().toString(),
                        txtConfirm.getText().toString())) return;
                createUser();
            }



            private void createUser() {
                dialog.show();
                userService = new UserService();
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                User user = new User(txtUserIdR.getText().toString(),
                        txtNickname.getText().toString(), txtPasswordR.getText().toString(), Client.getMyServer());
                boolean isRegisterOk = userService.create(user);
                if (isRegisterOk) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            }

            private boolean isValidConfirmPassword(String password, String confirmPassword) {
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            private boolean isValidPassword(String password) {
                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                Pattern pattern = Pattern.compile("^.*(?=.*\\d)(?=.*[A-Z])(?=.*[1-9]).*$");
                Matcher matcher = pattern.matcher(password);
                boolean isOk = matcher.find();
                if (!isOk) {
                    Toast.makeText(RegisterActivity.this, "Password must contain 6 characters: both" +
                            " digits and letters and at least one capital letter", Toast.LENGTH_LONG).show();
                }
                return isOk;
            }

            private boolean isValidUser(String userId) {
                if (userService.getById(userId) != null) {
                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (userId.indexOf(' ') >= 0) {
                    Toast.makeText(RegisterActivity.this, "User cannot have spaces!", Toast.LENGTH_LONG).show();
                    return false;
                }

                return true;
            }

            private boolean hasEnteredAllFields() {
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                if (txtUserIdR.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter User Id!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtNickname.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Nickname!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtPasswordR.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtConfirm.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Confirm Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }

    private void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String encodedImage;
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();



                if (null != selectedImageUri) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                    byte[] byteArray = outputStream.toByteArray();
//
//                    String encodedString = android.util.Base64.encodeToString(byteArray , Base64.DEFAULT);
//                    Log.d("image", encodedString);
                }
            }
        }
    }

}
