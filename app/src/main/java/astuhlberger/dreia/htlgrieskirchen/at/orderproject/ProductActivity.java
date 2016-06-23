package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

/**
 * Created by Marcus on 16.06.2016.
 */
public class ProductActivity extends Activity {

    HashMap<String, Integer> product;

    ArrayAdapter<String> itemsForList;
    ArrayList<String> productname;
    ArrayList<String> price;
    ArrayList<String> amount;
    Button order,cancel;
    String restaurantname;
    Firebase items,billBase;
    SharedPreferences prefs = null;
    String globalUsername = null;
    int globalprice = 0;
    boolean seas = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        Firebase.setAndroidContext(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        globalUsername = prefs.getString("username", "");
        Intent i = getIntent();
        Bundle params = i.getExtras();
        if (params!=null){
            restaurantname = params.getString("name").replace("s","");
        }

        productname = new ArrayList<>();
        price = new ArrayList<>();
        product = new HashMap<>();

        items = new Firebase("https://easyorderproducts.firebaseIO.com");
        billBase = new Firebase("https://easyorderbills.firebaseio.com/");
        items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Products", restaurantname);
                Log.d("Products", String.valueOf(dataSnapshot.child(restaurantname).getChildrenCount()));
                for (int i = 0; i < (dataSnapshot.child(restaurantname).getChildrenCount()); i++) {
                    productname.add(i, dataSnapshot.child(restaurantname).child(String.valueOf(i + 1)).child("Product").getValue().toString());
                    price.add(i, String.valueOf(dataSnapshot.child(restaurantname).child(String.valueOf(i + 1)).child("Price").getValue()));
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(ProductActivity.this);
                alert.setTitle("Select your item");
                final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_groups, null);
                alert.setView(dialog);
                ListView group = (ListView) dialog.findViewById(R.id.listView_groups);
                ArrayAdapter<String> groupad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, productname);
                group.setAdapter(groupad);
                group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pickAmount(position);
                    }
                });
                alert.show();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        order = (Button)findViewById(R.id.btn_orderProducts);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductActivity.this, "Thanks for your order", Toast.LENGTH_SHORT).show();
                setBillsFirebase();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("order", product);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        cancel = (Button)findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void pickAmount(final int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ProductActivity.this);
        alert.setTitle("How many do you want?");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.anzahl, null);
        final EditText swag = (EditText) dialog.findViewById(R.id.anzahl);
        alert.setView(dialog);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String amount = swag.getText().toString();
                product.put(productname.get(position).toString() + "-" + amount, (Integer.parseInt(price.get(position)) * Integer.parseInt(amount)));
                globalprice = (Integer.parseInt(price.get(position)) * Integer.parseInt(amount));
            }
        });
        alert.show();
    }


    private void setBillsFirebase() {


        billBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(seas) {
                    int anz = (int) dataSnapshot.child(globalUsername).getChildrenCount();
                    int anztrue = anz + 1;
                    billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Restaurant").setValue(restaurantname);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                    Calendar calendar = new GregorianCalendar(2016, 6, 22, 23, 48, 50);
                    Date date = calendar.getTime();
                    billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Date").setValue(date.toString());
                    billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Price").setValue(globalprice);
                    seas = false;
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
