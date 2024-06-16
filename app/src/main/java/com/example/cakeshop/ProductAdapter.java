package com.example.cakeshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Cake> products;
    private int layout;

    public ProductAdapter(Context context,int layout, List<Cake> products ) {
        this.context = context;
        this.products = products;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main,null);
        }
        ImageView imgHeader = convertView.findViewById(R.id.img_header);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvDesc = convertView.findViewById(R.id.tv_desc);
        TextView tvPrice = convertView.findViewById(R.id.tv_price);

        Cake product = products.get(position);
        String img = product.getImg();
        Glide.with(convertView).load(img).into(imgHeader);
        tvName.setText(product.getName());
        tvDesc.setText(product.getDesc());
        tvPrice.setText(product.getPrice()+"");
        return convertView;
    }
}

