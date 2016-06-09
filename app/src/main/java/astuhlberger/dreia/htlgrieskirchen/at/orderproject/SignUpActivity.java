package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    public void goToLogin(View view){

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
                        signUp();
                    }
                }
            }
        }
    }

    public void signUp() {
    }

    public boolean checkUsername(String username){
        if (username.isEmpty() || username.length()<5 || !username.matches("[A-Za-z0-9_]+")){
            //colorizeUserInput
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email){
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //colorizeEmailInput
            return false;
        }
        return true;
    }

    public boolean checkPassword(String password, String verifyPassword){
        if (password.isEmpty() || password.length()<5 || !password.matches("[A-Za-z0-9]+") ||
                !password.equals(verifyPassword)){
            //colorizePasswordInput
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
