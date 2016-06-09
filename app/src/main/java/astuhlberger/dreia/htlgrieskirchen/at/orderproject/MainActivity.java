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

public class MainActivity extends AppCompatActivity {
    Firebase dataBase;
    DataSnapshot snapshot;
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        dataBase = new Firebase("https://easyorder.firebaseIO.com");



        //Aufruf der LoginActivity
        Intent i = new Intent(this,LoginActivity.class);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1)
        {
            username = data.getStringExtra("username");
            password = data.getStringExtra("password");
            Log.d("Firebase",username + password);

            dataBase.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {

                    if(snapshot.child("id").child("username").getValue().toString().contains(username) && snapshot.child("id").child("password").getValue().toString().contains(password))
                    {
                        Toast.makeText(getApplicationContext(),"LOGIN COMPLETE",Toast.LENGTH_LONG).show();;
                    }
                }

                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
