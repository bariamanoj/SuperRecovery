package com.slbdeveloper.superrecovery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.SkuDetails;
import com.slbdeveloper.superrecovery.R;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>{

    private List<SkuDetails> subscription_demos;
    private Context mContext;

    onSubClick onSubClick;


    public void setOnSubClick(onSubClick onSubClick){
        this.onSubClick = onSubClick;
    }

    public interface onSubClick{
        void onsubClick(SkuDetails skuDetails , String sku);
    }

    public SubscriptionAdapter(Context context,  List<SkuDetails> subscription_demos) {
        this.subscription_demos = subscription_demos;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_iab_info, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final SkuDetails product = subscription_demos.get(position);

        String title = product.getTitle().substring(0,7);
        holder.tv_period.setText(title);
        holder.tv_price_title.setText(product.getPrice());
        holder.tv_price_msg.setText(product.getDescription());

        if (title.equalsIgnoreCase("3 Month")){
            holder.subscription.setBackgroundResource(R.drawable.shape_item_iab_filled);
            holder.tv_period.setTextColor(Color.WHITE);
            holder.tv_price_title.setTextColor(Color.WHITE);
            holder.tv_price_msg.setTextColor(Color.WHITE);
        }

        holder.subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSubClick.onsubClick(subscription_demos.get(position) , product.getSku());

            }
        });

    }

    @Override
    public int getItemCount() {
        return subscription_demos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout subscription;
        private TextView tv_period, tv_price_title, tv_price_msg;
        //private ImageView ivProduct;
        //private ImageButton ivMinus , ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            subscription = itemView.findViewById(R.id.subscription);
            tv_period = (TextView) itemView.findViewById(R.id.tv_period);
            tv_price_title = (TextView) itemView.findViewById(R.id.tv_price_title);
            tv_price_msg = (TextView) itemView.findViewById(R.id.tv_price_msg);

        }
    }

}
