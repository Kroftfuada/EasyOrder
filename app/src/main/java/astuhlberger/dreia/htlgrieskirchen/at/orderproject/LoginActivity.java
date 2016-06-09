package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by nprechtl on 09.06.2016.
 */
public class LoginActivity extends Activity{

    EditText username, password;
    String login_username,login_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.input_username);
        password = (EditText)findViewById(R.id.input_password);


    }
    public void login()
    {
        login_username = username.getText().toString();
        login_password = password.getText().toString();


    }
}
