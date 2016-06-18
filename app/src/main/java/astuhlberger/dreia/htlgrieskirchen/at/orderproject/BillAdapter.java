package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nico Prechtl on 18.06.2016.
 */
public class BillAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<String> dates;
    ArrayList<String> restaurantname;
    ArrayList<String> price;

    public BillAdapter(Context context, ArrayList<String> date, ArrayList<String> restaurantname, ArrayList<String> price) {
        this.context = context;
        this.dates = date;
        this.restaurantname = restaurantname;
        this.price = price;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.bill_adapter, null);

        TextView name = (TextView) rowView.findViewById(R.id.text_restname);
        TextView date = (TextView) rowView.findViewById(R.id.textView_date);
        TextView pric = (TextView) rowView.findViewById(R.id.textView_billprice);
        name.setText(restaurantname.get(position));
        date.setText(dates.get(position));
        pric.setText(price.get(position));
        return rowView;

    }
}
