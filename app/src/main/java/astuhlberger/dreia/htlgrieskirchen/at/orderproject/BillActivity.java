package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 *  Created by nprechtl on 16.06.2016.
 */

//TODO: Layout anzeigen lassen

public class BillActivity extends Activity {

    //al für menüpunkt
    ArrayList<String> groupid;
    String menurestaurant;
    String menuadmin;
    String menumember;
    String menuproducts;
    String menuprice;
    int id;

    Firebase dataBase;
    Firebase groupBase;
    Firebase groupOrder;

    ArrayList<String> dates;
    ArrayList<String> restnames;
    ArrayList<String> prices;
    BillAdapter ba;
    ListView billList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_layout);

        Firebase.setAndroidContext(this);
        dataBase = new Firebase("https://easyorderbills.firebaseio.com");
        groupBase = new Firebase("https://easyordergroups.firebaseio.com");
        groupOrder = new Firebase("https://easyordergrouporder.firebaseio.com");
        billList = (ListView) findViewById(R.id.listView_bill);

        groupid = new ArrayList();

        dates = new ArrayList<>();
        restnames = new ArrayList<>();
        prices = new ArrayList<>();

        fillBillLists();

        ba = new BillAdapter(this,dates,restnames,prices);
        billList.setAdapter(ba);
        ba.notifyDataSetChanged();


    }

    private void fillMenuGroup() {

        groupBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long anz = dataSnapshot.getChildrenCount();

                for (int i = 0; i<anz; i++){

                    boolean userInGroup = false;

                    String admin = (String) dataSnapshot.child(String.valueOf((i+1))).child("Admin").getValue();
                    //TODO: Aus konstanten usernamen holen und statt diesen usernamen ersetzen
                    if (admin.equals("Username")){
                        userInGroup = true;
                    }
                    if (userInGroup==false){
                        String members = (String) dataSnapshot.child(String.valueOf((i+1))).child("Member").getValue();
                        String member[] = members.split(",");
                        int count = member.length;
                        for (int j = 0; j<count; j++){
                            if (userInGroup==false && member[j].equals("Username")){
                                userInGroup = true;
                            }
                        }
                    }
                    if (userInGroup){
                        String restaurant = (String) dataSnapshot.child(String.valueOf((i+1))).child("Restaurant").getValue();
                        String group = "Group -"+(i+1)+"-, "+restaurant+", Admin: "+ admin;
                        groupid.add(group);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void fillBillLists() {

        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //TODO: Aus Konstanten den usernamen holen und statt diesen usernamen einsetzen
                if (dataSnapshot.child("Username").exists()){
                    int anz = (int) dataSnapshot.child("Username").getChildrenCount();
                    for (int i = 0; i<anz; i++) {
                        String r1 = "";
                        String d1 = "";
                        String p1 = "";

                        r1 = (String) dataSnapshot.child("Username").child(String.valueOf((i+1))).child("Restaurant").getValue();
                        p1 = (String) dataSnapshot.child("Username").child(String.valueOf((i+1))).child("Price").getValue();
                        d1 = (String) dataSnapshot.child("Username").child(String.valueOf((i+1))).child("Date").getValue();
                        Log.d("Daten geholt", "Daten geholt");
                        dates.add(d1);
                        restnames.add(r1);
                        prices.add(p1);
                        Log.d("In Liste", "In Liste");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
        i.putExtra("MenuId",id);
        i.putExtra("MenuRestaurant",menurestaurant);
        i.putExtra("MenuAdmin",menuadmin);
        i.putExtra("MenuMember",menumember);
        i.putExtra("MenuProducts",menuproducts);
        i.putExtra("MenuPrice",menuprice);
        startActivity(i);
    }
}
