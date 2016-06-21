package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

    Firebase groupBase;

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
    Firebase dataBaseUsers,dataBaseGroups, groupOrder;
    ArrayList<String>usersInGroup;
    ListView groupUsers;
    ArrayAdapter<String> restaurantAdapter;
    String adminname;
    int anz,anztrue;
    int counterForGroup = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupactivity_layout);
        Firebase.setAndroidContext(this);
        groupid = new ArrayList();

        groupBase = new Firebase("https://easyordergroups.firebaseio.com");
        usersInGroup = new ArrayList<>();
        groupList = (ListView) findViewById(R.id.listView_groups);
        textAddUser = (EditText) findViewById(R.id.input_addUser);
        btnAddProducts = (Button) findViewById(R.id.btn_addProducts);
        btnAddUser = (Button) findViewById(R.id.btn_addUser);
        btnStopOrders = (Button) findViewById(R.id.btn_StopOrders);
        btnShowMap = (Button) findViewById(R.id.btn_showMap);
        btnLeave = (Button) findViewById(R.id.btn_leaveGroup);
        btnShowOrders = (Button) findViewById(R.id.btn_showOrder);
        usernameToAdd = (EditText)findViewById(R.id.input_addUser);
        dataBaseUsers = new Firebase("https://easyorder.firebaseIO.com");
        groupUsers = (ListView) findViewById(R.id.listViewGroupActivity);
        dataBaseGroups = new Firebase("https://easyordergroups.firebaseio.com/");
        groupOrder = new Firebase("https://easyordergrouporder.firebaseio.com");
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if (params!=null){
            restaurantname = params.getString("name");
            adminname = params.getString("username").toString();
        }

        //TODO: addproducts
        //TODO: addUser
        //TODO: leave
        //TODO: Intent behandlung wenn man auf den Menüpunkt show Groups drückt

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

    private void usersToFirebase()
    {
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

                    dataBaseGroups.child(String.valueOf(anztrue)).child("Member").setValue(members);
                    dataBaseGroups.child(String.valueOf(anztrue)).child("Restaurant").setValue(restaurantname);
                    counterForGroup++;
                    Toast.makeText(getApplicationContext(),"The Group has been made. Please go to insert your wished products now.",Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });






    }

    private void addUserToListView()
    {
        dataBaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                String snapShotUsername = dataSnapshot.getValue().toString();
                if(usernameToAdd.getText().toString()!=null)
                {
                    if(dataSnapshot.child(usernameToAdd.getText().toString()).exists()&& !usersInGroup.contains(usernameToAdd.getText().toString()))
                    {
                        usersInGroup.add(usernameToAdd.getText().toString());
                        restaurantAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,usersInGroup);
                        groupUsers.setAdapter(restaurantAdapter);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Pls insert a valid username",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
        startActivityForResult(i, 1);
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
        i.putExtra("MenuPrice",menuprice);
        startActivity(i);
    }

    private void showBills() {
        Intent i = new Intent(this,BillActivity.class);
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
                    //TODO: Aus konstanten usernamen hollen und statt diesen usernamen ersetzen
                    if (admin.equals("Username")) {
                        userInGroup = true;
                    }
                    if (userInGroup == false) {
                        String members = (String) dataSnapshot.child(String.valueOf((i + 1))).child("Member").getValue();
                        String member[] = members.split(",");
                        int count = member.length;
                        for (int j = 0; j < count; j++) {
                            if (userInGroup == false && member[j].equals("Username")) {
                                userInGroup = true;
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
