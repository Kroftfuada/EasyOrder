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

/**
 * Created by nprechtl on 16.06.2016.
 */
public class VerificationActivity extends Activity{
    Firebase dataBase;
    EditText verifycode;
    TextView code;
    Button verify;
    String verificationCode,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);
        dataBase = new Firebase("https://easyorder.firebaseIO.com");

        verifycode = (EditText) findViewById(R.id.input_verification);
        verify = (Button) findViewById(R.id.btn_verify);
        code = (TextView)findViewById(R.id.textView_VerificationCode);

        Intent i = getIntent();
        Bundle params = i.getExtras();

        verificationCode = params.getString("VerificationCode").toString();
        username = params.getString("Username").toString();

        code.setText(verificationCode);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCode();
            }
        });
    }

    private void checkCode()
    {

        Log.d("Verification", verificationCode + " " + username);

        Firebase referal = dataBase.getRoot();
        referal.child(username).child("registered").setValue("true");
        if(verificationCode.equals(verifycode.getText().toString()))
        {
            Intent i = new Intent(this, AddRestaurantActivity.class);
            startActivity(i);
        }

    }
}
