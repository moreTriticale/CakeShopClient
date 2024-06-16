package com.example.cakeshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;
    private int layout;

    public OrderAdapter(Context context,int layout, List<Order> orders ) {
        this.context = context;
        this.orders = orders;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item_layout,null);
        }
        TextView tvOrderName = convertView.findViewById(R.id.tv_order_name);
        TextView tvOrderCount = convertView.findViewById(R.id.tv_order_count);
        TextView tvOrderSumPrice = convertView.findViewById(R.id.tv_order_sum_price);
        TextView tvOrderStatus = convertView.findViewById(R.id.tv_order_status);
        TextView tvOrderTime = convertView.findViewById(R.id.tv_order_time);
        Order order = orders.get(position);
        tvOrderName.setText("产品名称:" + order.getName());
        tvOrderCount.setText("产品数量:" + order.getCount() + "");
        tvOrderSumPrice.setText("总价格" + (order.getCount()  * Integer.parseInt(order.getPrice())) + "");
        if (order.getStatus() == 1){
            tvOrderStatus.setText("已下单");
        }
        tvOrderTime.setText("下单时间:" + order.getCreateTime());
        return convertView;
    }
}
