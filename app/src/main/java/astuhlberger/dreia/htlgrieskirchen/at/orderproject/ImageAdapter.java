package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Marcus on 19.06.2016.
 */
public class ImageAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> productList;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, ArrayList<String> productList) {
        this.context = context;
        productList = productList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.imageview,null);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        Picasso.with(context).load(productList.get(position)).into(imageView);

        return rowView;
    }
}
