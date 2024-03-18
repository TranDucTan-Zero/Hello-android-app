package com.zero.hello_android_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zero.hello_android_app.R;
import com.zero.hello_android_app.enity.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private int mResource;
    private OnEditButtonClickListener mEditButtonClickListener;
    private OnDeleteButtonClickListener mDeleteButtonClickListener;

    public ProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.product_id);
            holder.nameTextView = convertView.findViewById(R.id.product_name);
            holder.priceTextView = convertView.findViewById(R.id.product_price);
            holder.imageView = convertView.findViewById(R.id.product_image);

            holder.deleteButton = convertView.findViewById(R.id.btn_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);
        holder.idTextView.setText(String.valueOf(product.getId()));
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));

        Glide.with(mContext)
                .load(product.getImage())
                .into(holder.imageView);



        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteButtonClickListener != null) {
                    mDeleteButtonClickListener.onDeleteButtonClick(position);
                }
            }
        });

        return convertView;
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.mEditButtonClickListener = listener;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.mDeleteButtonClickListener = listener;
    }

    static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

        Button deleteButton;
    }
}