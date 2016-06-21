package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class AddRestaurantActivity extends Activity
{

    Button addRestaurant;
    ArrayList<String> restaurants;
    Firebase dataBase;
    Firebase groupBase;
    Firebase groupOrder;

    //al für  menüpunkt
    ArrayList<String> groupid;
    String menurestaurant;
    String menuadmin;
    String menumember;
    String menuproducts;
    String menuprice;
    int id;

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String globalUsername = prefs.getString("username", "");

    String username;
    boolean showGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_restaurant);
        Firebase.setAndroidContext(this);


        dataBase = new Firebase("https://easyorderrestaurant.firebaseIO.com/");
        groupBase = new Firebase("https://easyordergroups.firebaseio.com");
        groupOrder = new Firebase("https://easyordergrouporder.firebaseio.com");
        restaurants = new ArrayList<>();
        dataBase.child("logOn").setValue("false");
        showGroups = false;
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null)
        {
            username = b.get("username").toString();
        }



        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int anz = (int) dataSnapshot.getChildrenCount();

                for (int i = 0; i < (anz - 1); i++) {
                    Log.d("Restaurant", "seas");
                    restaurants.add(dataSnapshot.child("Restaurant" + String.valueOf(i + 1)).getValue().toString());
                    Log.d("Restaurant", dataSnapshot.child("Restaurant1").getValue().toString());
                    addDataToRestaurants();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        dataBase.child("logOn").setValue("true");
        groupid = new ArrayList();

    }

    private void addDataToRestaurants()
    {
        addRestaurant = (Button) findViewById(R.id.btn_newRestaurant);
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
        ArrayAdapter<String>restaurantAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,restaurants);
        new_restaurant.setAdapter(restaurantAdapter);

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
        i.putExtra("username", username);
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
            case R.id.action_groups: fillMenuGroup();
                showGroups();
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void showGroups() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pick a Group");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_groups, null);
        alert.setView(dialog);
        ListView group = (ListView) dialog.findViewById(R.id.listView_groups);
        ArrayAdapter<String> groupad = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,groupid);
        group.setAdapter(groupad);
        group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openGroupActivity(position);
            }
        });
        alert.show();
    }

    private void openGroupActivity(int position) {
        String group = groupid.get(position);
        String[] idString = group.split("-");
        id =  Integer.parseInt(idString[1]);
        groupBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menurestaurant = (String) dataSnapshot.child(String.valueOf(id)).child("Restaurant").getValue();
                menuadmin = (String) dataSnapshot.child(String.valueOf(id)).child("Admin").getValue();
                menumember = (String) dataSnapshot.child(String.valueOf(id)).child("Member").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        groupOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuproducts = (String) dataSnapshot.child(String.valueOf(id)).child("Products+Numbers").getValue();
                menuprice = (String) dataSnapshot.child(String.valueOf(id)).child("SumPrice").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Intent i = new Intent(this, GroupActivity.class);
        i.putExtra("MenuRestaurant",menurestaurant);
        i.putExtra("MenuAdmin",menuadmin);
        i.putExtra("MenuMember",menumember);
        i.putExtra("MenuProducts",menuproducts);
        i.putExtra("MenuId",id);
        i.putExtra("MenuPrice",menuprice);
        startActivity(i);
    }

    private void showBills() {
        Intent i = new Intent(this,BillActivity.class);
        startActivity(i);
    }

    private void fillMenuGroup() {
        showGroups = true;
        if(showGroups == true) {
            groupBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long anz = dataSnapshot.getChildrenCount();

                    for (int i = 0; i < anz; i++) {

                        boolean userInGroup = false;

                        String admin = (String) dataSnapshot.child(String.valueOf((i + 1))).child("Admin").getValue();

                        if (admin.equals(globalUsername)) {
                            userInGroup = true;
                        }

                        if (userInGroup == false) {
                            String members = (String) dataSnapshot.child(String.valueOf((i + 1))).child("Member").getValue();
                            String member[];

                            if(members.contains(","))
                            {
                                member = members.split(",");
                                int count = member.length;
                                for (int j = 0; j < count; j++) {
                                    if (userInGroup == false && member[j].equals(globalUsername)) {
                                        userInGroup = true;
                                    }
                                }
                            }
                        }
                        if (userInGroup) {
                            String restaurant = (String) dataSnapshot.child(String.valueOf((i + 1))).child("Restaurant").getValue();
                            String group = "Group -" + (i + 1) + "-, " + restaurant + ", Admin: " + admin;
                            groupid.add(group);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}
