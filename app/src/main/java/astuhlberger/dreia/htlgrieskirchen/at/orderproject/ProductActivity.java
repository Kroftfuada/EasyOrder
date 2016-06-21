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
import android.widget.Button;
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
    ListView productList;
    HashMap<String, Integer> product;
    ArrayList<String> items = new ArrayList<String>();
    ProductAdapter pa;
    ArrayList<String> productname;
    ArrayList<String> price;
    ArrayList<String> amount;
    Button order, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        productList = (ListView) findViewById(R.id.listView_productlist);

        //TODO: werte aus datenbank holen für productname und price und mit amount dann noch weiterverarbeiten durch anderen dialog

        pa = new ProductAdapter(this,items,productname,price,amount);
        productList.setAdapter(pa);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogs(items.get(position));
            }
        });

        order = (Button) findViewById(R.id.btn_orderProducts);
        cancel = (Button) findViewById(R.id.btn_cancel);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: hashmap zurückliefern an intentforresult
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Activity schließen und wieder auf group activity zurückkehren
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
}
