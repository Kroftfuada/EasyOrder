package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
                    if (dataSnapshot.child(username).exists() && dataSnapshot.child(username).child("password").getValue().toString().equals(pw)) {
                        if (dataSnapshot.child(username).child("registered").getValue().toString().equals("true")) {
                            Log.d("Eingeloggt", "Erfolgreich eingeloggt");

                            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = SP.edit();
                            editor.putString("username", username);
                            editor.commit();


                            Intent intent = new Intent(getApplicationContext(), AddRestaurantActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                            i.putExtra("VerificationCode", dataSnapshot.child(username).child("verification").getValue().toString());
                            i.putExtra("Username", username);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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