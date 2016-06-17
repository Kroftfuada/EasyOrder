package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class GroupActivity extends Activity {

    //al für menüpunkt
    ArrayList<Integer> groupid;

    ListView groupList;
    EditText textAddUser;
    Button btnAddUser;
    Button btnStopOrders;
    Button btnAddProducts;
    Button btnShowMap;
    Button btnLeave;
    Button btnShowOrders;
    String restaurantname = "Mc Donalds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupactivity_layout);

        groupid = new ArrayList();
        //TODO: arraylist mit gruppen befüllen

        groupList = (ListView) findViewById(R.id.listView_groups);
        textAddUser = (EditText) findViewById(R.id.input_addUser);
        btnAddProducts = (Button) findViewById(R.id.btn_addProducts);
        btnAddUser = (Button) findViewById(R.id.btn_addUser);
        btnStopOrders = (Button) findViewById(R.id.btn_StopOrders);
        btnShowMap = (Button) findViewById(R.id.btn_showMap);
        btnLeave = (Button) findViewById(R.id.btn_leaveGroup);
        btnShowOrders = (Button) findViewById(R.id.btn_showOrder);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if (params!=null){
            restaurantname = params.getString("name");
        }

        //TODO: Listview mit usern befüllen
        //TODO: addproducts
        //TODO: addUser
        //TODO: leave

        btnShowOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog();
            }
        });

        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductActivity();

            }
        });

        btnStopOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: bei abfrage ob der user gruppenadmin ist
                if(true) {
                    if(btnAddProducts.isClickable()){
                        btnAddProducts.setClickable(false);
                    }else{
                        btnAddProducts.setClickable(true);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Only the admin can stop orders", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();

            }
        });

    }

    private void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //TODO: Listview einfügen und mit daten aus bestellung füllen
        builder.setNegativeButton("OK", null);
        builder.show();

    }

    private void showProductActivity() {
        Intent i = new Intent(this, ProductActivity.class);
        startActivityForResult(i,1);
    }

    private void showMap() {
        Intent i = new Intent(this, MapFragmentActivity.class);
        i.putExtra("name",restaurantname);
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
