package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class AddRestaurantActivity extends Activity
{

    Button addRestaurant;
    ArrayList<String> restaurants;

    //al für menüpunkt
    ArrayList<Integer> groupid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurant);

        restaurants = new ArrayList<>();
        //TODO:Arraylist füllen aus Datenbank

        groupid = new ArrayList();
        //TODO: arraylist mit gruppen befüllen

        addRestaurant= (Button) findViewById(R.id.btn_newRestaurant);
        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs();
            }
        });
    }

    private void showDialogs() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pick Restaurant");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_new_restaurant, null);
        alert.setView(dialog);
        ListView new_restaurant = (ListView) dialog.findViewById(R.id.listView_new_Restaurant);
        new_restaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRestaurantDialog(restaurants.get(position));
            }
        });
        alert.show();
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
        i.putExtra("name", restaruantname);
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
        System.exit(0);
    }

    private void showGroups() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pick a Group");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_groups, null);
        alert.setView(dialog);
        ListView group = (ListView) dialog.findViewById(R.id.listView_groups);
        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openGroupActivity(position);
            }
        });
        alert.show();
    }

    private void openGroupActivity(int position) {
        groupid.get(position);
        //TODO: restaurantname etc holen aus db und intent an group activity machen!
    }

    private void showBills() {
        Intent i = new Intent(this,BillActivity.class);
        startActivity(i);
    }
}
