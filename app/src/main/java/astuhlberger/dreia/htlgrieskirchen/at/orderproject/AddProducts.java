package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Marcus on 19.06.2016.
 */
public class AddProducts extends Activity{
    private ArrayList<String> productList;
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproducts_layout);
        gridView = (GridView) findViewById(R.id.gridview);

        productList = new ArrayList<String>();
        addProducts();
    }

    private void addProducts() {
        imageAdapter = new ImageAdapter(getBaseContext(), productList);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
