package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
    private void showRestaurantDialog(){
        
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
            case R.id.action_orders: showOrders();
                break;
            case R.id.action_logout: logout();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void logout() {
    }

    private void showOrders() {
    }

    private void showGroups() {
    }

    private void showBills() {
    }
}
