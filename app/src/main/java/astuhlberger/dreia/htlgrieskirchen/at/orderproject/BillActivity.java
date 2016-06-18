package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class BillActivity extends Activity {

    //al für menüpunkt
    ArrayList<Integer> groupid;
    ArrayList<String> dates;
    ArrayList<String> restnames;
    ArrayList<String> prices;
    BillAdapter ba;
    ListView billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_layout);

        billList = (ListView) findViewById(R.id.listView_bill);

        groupid = new ArrayList();
        //TODO: menupoint groupid befüllen mit gruppen

        dates = new ArrayList<>();
        restnames = new ArrayList<>();
        prices = new ArrayList<>();
        //TODO: arraylisten mit gruppendatenbank befüllen

        ba = new BillAdapter(this,dates,restnames,prices);
        billList.setAdapter(ba);
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
