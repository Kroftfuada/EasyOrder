package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nprechtl on 16.06.2016.
 */
public class GroupActivity extends Activity {

    //al für menüpunkt
    ArrayList<String> groupid;
    String menurestaurant;
    String menuadmin;
    String menumember;
    String menuproducts;
    String menuprice;
    int id;

    String intentProducts;
    String intentPrice;

    Firebase groupBase;

    HashMap<String, Integer> prodctmap;
    ListView groupList;
    EditText textAddUser;
    Button btnAddUser;
    Button btnStopOrders;
    Button btnAddProducts;
    Button btnShowMap;
    Button btnLeave;
    Button btnShowOrders;
    String restaurantname;
    EditText usernameToAdd;
    Firebase dataBaseUsers, dataBaseGroups, groupOrder;
    ArrayList<String> usersInGroup, itemsToOrder;
    ListView groupUsers;
    ArrayAdapter<String> restaurantAdapter;
    String adminname;
    int anz, anztrue;
    int counterForGroup = 0;
    int groupID = 0;
    boolean showOrders = false;
    boolean seas = true;
    SharedPreferences prefs = null;
    String globalUsername = null;
    ArrayAdapter<String> groupad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupactivity_layout);
        Firebase.setAndroidContext(this);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        globalUsername = prefs.getString("username", "");
        groupid = new ArrayList();
        itemsToOrder = new ArrayList();
        prodctmap = new HashMap<>();
        groupBase = new Firebase("https://easyordergroups.firebaseio.com");
        usersInGroup = new ArrayList<>();
        intentMethod();
        groupList = (ListView) findViewById(R.id.listView_groups);
        textAddUser = (EditText) findViewById(R.id.input_addUser);
        btnAddProducts = (Button) findViewById(R.id.btn_addProducts);
        btnAddUser = (Button) findViewById(R.id.btn_addUser);
        btnStopOrders = (Button) findViewById(R.id.btn_StopOrders);
        btnShowMap = (Button) findViewById(R.id.btn_showMap);
        btnLeave = (Button) findViewById(R.id.btn_leaveGroup);
        btnShowOrders = (Button) findViewById(R.id.btn_showOrder);
        usernameToAdd = (EditText) findViewById(R.id.input_addUser);
        dataBaseUsers = new Firebase("https://easyorder.firebaseIO.com");
        groupUsers = (ListView) findViewById(R.id.listViewGroupActivity);
        dataBaseGroups = new Firebase("https://easyordergroups.firebaseio.com/");
        groupOrder = new Firebase("https://easyordergrouporder.firebaseio.com");
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if (params != null) {
            restaurantname = params.getString("name");
            adminname = params.getString("username").toString();
        }


        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveGroup();
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToListView();
            }
        });

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

        btnStopOrders.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usersToFirebase();
                    }
                });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();

            }
        });

    }

    private void LeaveGroup() {

        Intent i = new Intent(GroupActivity.this, AddRestaurantActivity.class);
        startActivity(i);

    }

    private void intentMethod() {

        Intent i = getIntent();
        Bundle params = i.getExtras();
        if (params != null) {
            if (params.containsKey("MenuAdmin")) {
                restaurantname = params.getString("MenuRestaurant");
                counterForGroup = params.getInt("MenuId");
                adminname = params.getString("MenuAdmin");
                String member = params.getString("MenuMember");
                String[] members = member.split(",");
                for (int k = 0; k < members.length; k++) {
                    usersInGroup.add(members[k]);
                }

                intentProducts = params.getString("MenuProducts");
                intentPrice = params.getString("MenuPrice");
                setLists(intentProducts, intentPrice);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Bundle params = data.getExtras();
            if (params != null) {
                prodctmap = (HashMap<String, Integer>) params.get("order");
                //TODO: prodctmap hinzufügen zu den bisherigen gruppenbestellungen mit preis, welcher noch ausgerechnet werden muss.
                String prodPnumbers = "";
                int sum = 0;

                int anzFromHash = prodctmap.size();
                String[] products = new String[anzFromHash];
                int anz = 0;
                Log.d("Products", String.valueOf(anzFromHash));
                for (String key : prodctmap.keySet()) {
                    products[anz] = key;
                }

                for (int i = 0; i < anzFromHash; i++) {
                    if (i == (anzFromHash - 1)) {
                        prodPnumbers += products[0] + "-" + prodctmap.get(products);
                    } else {
                        prodPnumbers += products[0] + "-," + prodctmap.get(products);
                    }

                    sum += prodctmap.get(products[i]);
                }

                final String productsandnumbers = prodPnumbers;
                final int finalsum = sum;

                groupOrder.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(String.valueOf(groupID)).exists() && seas == true) {
                            String product = dataSnapshot.child(String.valueOf(groupID)).child("Products+Numbers").getValue().toString().concat("," + productsandnumbers);
                            int sum = finalsum + Integer.parseInt((String) dataSnapshot.child(String.valueOf(groupID)).child("SumPrice").getValue());
                            groupOrder.child(String.valueOf(groupID)).child("Products+Numbers").setValue(product);
                            groupOrder.child(String.valueOf(groupID)).child("SumPrice").setValue(sum);

                        } else {
                            groupOrder.child(String.valueOf(groupID)).child("Products+Numbers").setValue(productsandnumbers);
                            groupOrder.child(String.valueOf(groupID)).child("SumPrice").setValue(finalsum);
                            seas = false;
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    }

    private void usersToFirebase() {

        dataBaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int anz = (int) dataSnapshot.getChildrenCount();
                int anztrue = anz + 1;

                if (counterForGroup == 0) {
                    dataBaseGroups.child(String.valueOf(anztrue)).child("Admin").setValue(adminname);

                    String members = "";
                    for (int i = 0; i < usersInGroup.size(); i++) {
                        if (i < (usersInGroup.size() - 1)) {
                            members += (usersInGroup.get(i).toString() + ",");
                        } else {
                            members += (usersInGroup.get(i).toString());
                        }
                    }

                    groupID = anztrue;

                    dataBaseGroups.child(String.valueOf(anztrue)).child("Member").setValue(members);
                    dataBaseGroups.child(String.valueOf(anztrue)).child("Restaurant").setValue(restaurantname);
                    counterForGroup++;
                    Toast.makeText(getApplicationContext(), "The Group has been made. Please go to insert your wished products now.", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void addUserToListView() {
        dataBaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String snapShotUsername = dataSnapshot.getValue().toString();
                if (usernameToAdd.getText().toString() != null) {
                    if (dataSnapshot.child(usernameToAdd.getText().toString()).exists() && !usersInGroup.contains(usernameToAdd.getText().toString())) {
                        usersInGroup.add(usernameToAdd.getText().toString());
                        restaurantAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, usersInGroup);
                        groupUsers.setAdapter(restaurantAdapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Pls insert a valid username", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void showOrderDialog() {
        showOrders = true;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Items to order.");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.allproducts, null);
        alert.setView(dialog);
        final ListView order = (ListView) dialog.findViewById(R.id.listViewProducts);
        if (showOrders == true) {
            groupOrder.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String products = (String) dataSnapshot.child(String.valueOf(groupID)).child("Products+Numbers").getValue();
                    String sum = dataSnapshot.child(String.valueOf(groupID)).child("SumPrice").getValue().toString();

                    setLists(products, sum);
                    ArrayAdapter<String> groupad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, itemsToOrder);
                    order.setAdapter(groupad);
                    showOrders = false;
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            alert.show();

        }


    }

    private void setLists(String products, String sum) {

        String[] allProducts = products.split(",");
        for (int k = 0; k < allProducts.length; k++) {
            itemsToOrder.add("Product:" + allProducts[k].toString());
        }
        itemsToOrder.add("SUM: " + sum);
        Log.d("seas", products);
        Log.d("seas", sum);
    }

    private void showProductActivity() {
        Intent i = new Intent(this, ProductActivity.class);
        i.putExtra("name", restaurantname);
        startActivityForResult(i, 1);
    }

    private void showMap() {
        Intent i = new Intent(this, MapFragmentActivity.class);
        i.putExtra("name", restaurantname);
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
            case R.id.action_bills:
                showBills();
                break;
            case R.id.action_groups:
                fillMenuGroup();
                showGroups();
                break;
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void logout() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void showGroups() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pick a Group");
        final LinearLayout dialog = (LinearLayout) getLayoutInflater().inflate(R.layout.listview_groups, null);
        alert.setView(dialog);
        ListView group = (ListView) dialog.findViewById(R.id.listView_groups);
        groupad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, groupid);
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
        id = Integer.parseInt(idString[1]);
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
        i.putExtra("MenuRestaurant", menurestaurant);
        i.putExtra("MenuAdmin", menuadmin);
        i.putExtra("MenuMember", menumember);
        i.putExtra("MenuProducts", menuproducts);
        i.putExtra("MenuPrice", menuprice);
        i.putExtra("MenuId", id);
        startActivity(i);
    }

    private void showBills() {
        Intent i = new Intent(this, BillActivity.class);
        startActivity(i);
    }

    private void fillMenuGroup() {

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

                        if (members.contains(",")) {
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
