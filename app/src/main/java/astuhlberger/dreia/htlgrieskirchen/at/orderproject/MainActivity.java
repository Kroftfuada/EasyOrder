package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Firebase dataBase;
    DataSnapshot snapshot;
    String username, password;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        dataBase = new Firebase("https://easyorder.firebaseIO.com");
        //Aufruf der LoginActivity
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Log.d("Firebase", "ok");
            username = data.getStringExtra("username");
            password = data.getStringExtra("password");

            pw = md5(password);

                dataBase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Eingeloggt", "Überprüfung der Daten");
                        Log.d("Eingeloggt",dataSnapshot.child(username).child("password").getValue().toString() + " _ " +pw);
                        if (dataSnapshot.child(username).exists() && dataSnapshot.child(username).child("password").getValue().toString().equals(pw)) {
                            if(dataSnapshot.child(username).child("registered").getValue().toString().equals("true")) {
                                Log.d("Eingeloggt", "Erfolgreich eingeloggt");

                                Intent intent = new Intent(getApplicationContext(), AddRestaurantActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent i = new Intent(getApplicationContext(),VerificationActivity.class);
                                i.putExtra("VerificationCode",dataSnapshot.child(username).child("verification").getValue().toString());
                                i.putExtra("Username",username);
                                startActivity(i);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                Firebase ref = dataBase.getRoot();
                ref.child("LogOn").setValue("seas");


        }
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
}