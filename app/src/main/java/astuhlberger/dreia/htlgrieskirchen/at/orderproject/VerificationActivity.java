package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class VerificationActivity extends Activity{

    EditText verifycode;
    Button verify;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);
        
        verifycode = (EditText) findViewById(R.id.input_verification);
        verify = (Button) findViewById(R.id.btn_verify);
        
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCode();
            }
        });
    }

    private void checkCode() {
    }
}
