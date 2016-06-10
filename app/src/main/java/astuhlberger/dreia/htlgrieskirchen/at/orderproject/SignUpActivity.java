package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

/**
 * Created by nprechtl on 09.06.2016.
 */
public class SignUpActivity extends Activity {

    EditText input_name;
    EditText input_email;
    EditText input_password;
    EditText input_verify_password;
    TextView goToLogin;
    Button btn_signup;
    Firebase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        input_verify_password = (EditText) findViewById(R.id.input_verify_password);
        goToLogin = (TextView) findViewById(R.id.link_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        dataBase = new Firebase("https://easyorder.firebaseIO.com");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctChecks(v);
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin(v);
            }
        });
    }

    public void goToLogin(View view)
    {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    public void correctChecks(View view){
        String username = input_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();
        String verifyPassword = input_verify_password.getText().toString();

        boolean allConditionsOkay = false;

        allConditionsOkay = checkUsername(username);
        if (allConditionsOkay) {
            allConditionsOkay = checkEmail(email);
            if (allConditionsOkay){
                allConditionsOkay = checkPassword(password, verifyPassword);
                if (allConditionsOkay){
                    allConditionsOkay = checkDatabase(username, email);
                    if (allConditionsOkay){
                        signUp(username,email,password);
                    }
                }
            }
        }
    }

    public void signUp(String username,String email,String password)
    {
        byte[] bytesOfMessage = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                bytesOfMessage = password.getBytes("UTF-8");
            }catch (UnsupportedEncodingException c)
            {
                Log.d("Firebase",c.toString());
            }
            byte[] pw = md.digest(bytesOfMessage);


            Firebase referal = dataBase.getRoot();
            referal.child(username);
            referal.child(username).child("password").setValue(pw.toString());
            referal.child(username).child("email").setValue(email);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public boolean checkUsername(String username){
        if (username.isEmpty() || username.length()<5 || !username.matches("[A-Za-z0-9_]+")){
            input_name.setError("At least 5 characters from A-Z, a-z, 0-9, _");
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email){
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("That is not a correct email-address");
            return false;
        }
        return true;
    }

    public boolean checkPassword(String password, String verifyPassword){
        if (password.isEmpty() || password.length()<5 || !password.matches("[A-Za-z0-9]+")){

            input_password.setError("At least 5 characters from A-Z, a-z, 0-9");
            return false;

        }
        if (!password.equals(verifyPassword)){
            input_verify_password.setError("Password is not the same");
            return false;
        }
        return true;
    }

    public boolean checkDatabase(String username, String email) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("username", username);
        returnIntent.putExtra("email", email);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        return true;
    }
}