package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class GroupActivity extends Activity {

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
        //TODO: showOrder
        //TODO: leave

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

    private void showProductActivity() {
        Intent i = new Intent(this, ProductActivity.class);
        startActivityForResult(i,1);
    }

    private void showMap() {
        Intent i = new Intent(this, MapFragmentActivity.class);
        i.putExtra("name",restaurantname);
        startActivity(i);
    }
}
