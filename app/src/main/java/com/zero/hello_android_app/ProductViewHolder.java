package com.zero.hello_android_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder  extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView nameTextView;
    public TextView priceTextView;

    public ProductViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.product_image);
        nameTextView = itemView.findViewById(R.id.product_name);
        priceTextView = itemView.findViewById(R.id.product_price);
    }
}
