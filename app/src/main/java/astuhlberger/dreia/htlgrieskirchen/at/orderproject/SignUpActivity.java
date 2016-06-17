package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

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
    boolean usernameANDemailCheckup = false;
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
        setResult(Activity.RESULT_OK, returnIntent);
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
                        if(usernameANDemailCheckup == true) {
                            signUp(username, email, password);
                        }
                    }
                }
            }
        }
    }

    public void signUp(String username,String email,String password)
    {

            String pw = md5(password);
            String verificationCode = random();
            Firebase referal = dataBase.getRoot();
            referal.child(username);
            referal.child(username).child("password").setValue(pw.toString());
            referal.child(username).child("email").setValue(email);
            referal.child(username).child("verification").setValue(verificationCode);
            referal.child(username).child("registered").setValue("false");

            Log.d("Verification", verificationCode + " " + username);

            Intent i = new Intent(this,VerificationActivity.class);
            i.putExtra("VerificationCode",verificationCode);
            i.putExtra("Username",username);
            startActivity(i);

    }

    public static String random() {

        return String.valueOf(Math.random()*200);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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

    public boolean checkDatabase(final String username, final String email) {

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                    String snapShotUsername = dataSnapshot.getValue().toString();
                    if (snapShotUsername.contains(email)) {
                        usernameANDemailCheckup = false;
                        Toast.makeText(getApplicationContext(), "Email already exists.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        usernameANDemailCheckup = true;
                    }



                if(dataSnapshot.child(username).exists())
                {
                    usernameANDemailCheckup = false;
                    Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    usernameANDemailCheckup = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return true;
    }
}
