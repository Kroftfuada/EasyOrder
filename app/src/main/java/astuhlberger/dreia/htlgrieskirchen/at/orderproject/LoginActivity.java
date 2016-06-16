package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nprechtl on 09.06.2016.
 */
public class LoginActivity extends Activity{

    EditText username, password;
    String login_username,login_password;
    Button login;
    TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.input_username);
        password = (EditText)findViewById(R.id.input_password);
        login = (Button)findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_username = username.getText().toString();
                login_password = password.getText().toString();
                Log.d("Firebase",login_username);

                if(login_username != null && login_password != null)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("username",login_username);
                    returnIntent.putExtra("password",login_password);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                    //TODO: ob schon verificated
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Passwort oder Username falsch",Toast.LENGTH_LONG).show();
                }
            }
        });

        signup = (TextView)findViewById(R.id.link_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(goToSignUp);
            }
        });

    }



}
