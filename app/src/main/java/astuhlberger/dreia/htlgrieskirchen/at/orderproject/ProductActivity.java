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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Marcus on 16.06.2016.
 */
public class ProductActivity extends Activity {
    ListView productList;
    HashMap<String, Integer> product;

    ArrayList<String> items;

    ProductAdapter pa;

    ArrayList<String> productname;
    ArrayList<String> price;
    ArrayList<String> amount;

    Button order, cancel;
    String restaurantname;
    Firebase productBase;
    Firebase billBase;
    String itemBase;
    String productnameBase;
    String priceBase;
    String amountBase;
    SharedPreferences prefs = null;
    String globalUsername = null;

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
            restaurantname = params.getString("name");
        }

        productList = (ListView) findViewById(R.id.listView_productlist);
        productBase = new Firebase("https://easyorderproducts.firebaseio.com");
        billBase = new Firebase("https://easyorderbills.firebaseio.com");
        product = new HashMap<>();
        productname=new ArrayList<String>();
        price=new ArrayList<String>();
        amount=new ArrayList<String>();
        items = new ArrayList<String>();
        fillLists();

        pa = new ProductAdapter(this,items,productname,price,amount);
        productList.setAdapter(pa);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogs(productname.get(position),position);
            }
        });

        order = (Button) findViewById(R.id.btn_orderProducts);
        cancel = (Button) findViewById(R.id.btn_cancel);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBillsFirebase();
                Intent i = new Intent();
                i.putExtra("List",product);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Activity schließen und wieder auf group activity zurückkehren
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    private void setBillsFirebase() {

        billBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int anz = (int) dataSnapshot.child(globalUsername).getChildrenCount();
                int anztrue = anz+1;

                billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Restaurant").setValue(restaurantname);
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Date").setValue(date);
                int billprice =0;
                for (int i = 0; i < amount.size();i++){
                    billprice+=Integer.parseInt(amount.get(i))*Integer.parseInt(price.get(i));
                }
                billBase.child(globalUsername).child(String.valueOf(anztrue)).child("Price").setValue(billprice);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void fillLists() {
        productBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int anz = (int) dataSnapshot.child(restaurantname).getChildrenCount();
                for (int i = 0; i<anz; i++){
                    itemBase = (String) dataSnapshot.child(restaurantname).child(String.valueOf((i+1))).child("Image").getValue();
                    priceBase = (String) dataSnapshot.child(restaurantname).child(String.valueOf((i+1))).child("Price").getValue();
                    productnameBase = (String) dataSnapshot.child(restaurantname).child(String.valueOf((i+1))).child("Product").getValue();
                    amountBase = String.valueOf(0);
                    items.add(itemBase);
                    price.add(priceBase);
                    productname.add(productnameBase);
                    amount.add(amountBase);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void showDialogs(final String s, final int position) {
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
                amount.set(position,String.valueOf(number));
                pa.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


}
