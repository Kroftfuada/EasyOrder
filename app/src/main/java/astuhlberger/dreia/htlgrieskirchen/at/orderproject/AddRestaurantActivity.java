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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class AddRestaurantActivity extends Activity
{

    Button addRestaurant;
    ListView new_restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurant);

        new_restaurant = (ListView) findViewById(R.id.listView_new_Restaurant);

        addRestaurant= (Button) findViewById(R.id.btn_newRestaurant);
        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs();
            }
        });
    }

    private void showDialogs() {
        //TODO: Datenbankzugriff
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pick Restaurant");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_new_restaurant, null);
        alert.setView(dialog);
        new_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRestaurantDialog(null);
            }
        });

    }
    private void showRestaurantDialog(final String restaurantname){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Make a new group with: "+restaurantname);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewGroup(restaurantname);
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
        //TODO: Datenbankzugriff mit den gruppen!

    }

    private void showBills() {
        Intent i = new Intent(this,BillActivity.class);
        startActivity(i);
    }
}
