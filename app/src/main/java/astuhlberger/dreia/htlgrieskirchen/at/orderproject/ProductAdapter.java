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
public class ProductAdapter extends BaseAdapter{
    public final String SIZE ="w500";

    ArrayList<String> arrayListImages = null;
    LayoutInflater layoutInflater;
    Context context;
    String productname;
    String prices;
    String amounts;

    public ProductAdapter(Context context, ArrayList<String> URLList, String productname, String price, String amount) {
        this.arrayListImages = URLList;
        this.context = context;
        this.productname =productname;
        this.prices=price;
        this.amounts=amount;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayListImages.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.product_adapter,null);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        Picasso.with(context).load(SIZE+arrayListImages.get(position)).into(imageView);
        TextView name = (TextView) rowView.findViewById(R.id.textView_ProductName);
        TextView amount = (TextView) rowView.findViewById(R.id.textView_Amount);
        TextView price = (TextView) rowView.findViewById(R.id.textView_Price);
        name.setText(productname);
        amount.setText(amounts);
        price.setText(prices);
        return rowView;

    }
}

