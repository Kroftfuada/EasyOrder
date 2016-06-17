package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marcus on 16.06.2016.
 */
public class ProductActivity extends Activity {
    ListView billList;
    HashMap<String, Integer> product;
    ArrayList<String> items = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
                displayItems();
        billList = (ListView) findViewById(R.id.listView_productlist);

        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogs(items.get(position));
            }
        });
    }

    private void showDialogs(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout vDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.product_selection, null);
        builder.setView(vDialog);
        builder.setTitle(s + ":");
        final NumberPicker numberpicker = (NumberPicker) vDialog.findViewById(R.id.numberPicker);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int number = numberpicker.getValue();
                product.put(s, number);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void displayItems(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, items);
        //TODO: Datenbank
    }


}
