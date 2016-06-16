package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class AddRestaurantActivity extends Activity
{

    Button addRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurant);

        addRestaurant= (Button) findViewById(R.id.btn_newRestaurant);
        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs();
            }
        });
    }

    private void showDialogs() {

    }
    private void showRestaurantDialog(final String restaruantname){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Make a new group with: "+restaruantname);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewGroup(restaruantname);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void createNewGroup(String restaruantname) {
        Intent i = new Intent(this, GroupActivity.class);
        i.putExtra("name",restaruantname);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_bills: showBills();
                break;
            case R.id.action_groups: showGroups();
                break;
            case R.id.action_logout: logout();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void logout() {
        //TODO: username aus den konstanten werfen
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    private void showGroups() {
    }

    private void showBills() {
    }
}
