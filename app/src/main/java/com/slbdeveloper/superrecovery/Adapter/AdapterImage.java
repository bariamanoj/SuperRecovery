package com.slbdeveloper.superrecovery.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.slbdeveloper.superrecovery.Model.ImageData;
import com.slbdeveloper.superrecovery.R;

import java.util.ArrayList;

import static com.slbdeveloper.superrecovery.Activity.ScanActivity.toolbar;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ViewHolder> {

    private static final String TAG = "AdapterImage";

    ArrayList<ImageData> alImageData;
    Context context;
    int itemCount = 0;

    public AdapterImage(Context context, ArrayList<ImageData> alImageData) {
        this.alImageData = alImageData;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ImageData imageData = this.alImageData.get(position);

        if (imageData.getSelection()) {
            holder.cbPic.setVisibility(View.VISIBLE);
        } else {
            holder.cbPic.setVisibility(View.GONE);
        }
        //view.setTag(viewGroup);
        try {

            itemCount = itemCount + 1;

            //Log.d(TAG, "onBindViewHolder: " + itemCount);

            Log.d(TAG, "onBindViewHolder: " + alImageData.size());

            if (alImageData.get(position).getFilePath().endsWith(".mp3")){

                Glide.with(context)
                        .load(R.drawable.ic_music)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_music)
                                .centerCrop())
                        .into(holder.ivPic);

            }else{
                Glide.with(context)
                        .load(alImageData.get(position).getFilePath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .centerCrop())
                        .into(holder.ivPic);
            }



//            if(!AppConsts.isUserSubcribed){
//
//                Glide.with(context)
//                        .load(alImageData.get(position).getFilePath())
//                        //.override(30, 30)
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.no_image)
//                                .centerCrop())
//                        .into(holder.ivPic);
//
//            }else{
//                Glide.with(context)
//                        .load(alImageData.get(position).getFilePath())
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.no_image)
//                                .centerCrop())
//                        .into(holder.ivPic);
//            }






//            GlideApp.with(context)
//                    .load(alImageData.get(position)
//                    .getFilePath())
//                    .placeholder(R.drawable.no_image)
//                    .centerCrop()
//                    .into(ivPic);


        } catch (Exception e) {
            //do nothing
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageData.getSelection()) {
                    imageData.setSelction(false);
                    notifyDataSetChanged();
                    toolbar.setTitle(getSelectedItem().size() + " items selected");
                } else {
                    imageData.setSelction(true);
                    notifyDataSetChanged();
                    toolbar.setTitle(getSelectedItem().size() + " items selected");
                }

                if (getSelectedItem().size() > 0) {
                    toolbar.setVisibility(View.VISIBLE);
                    //restore_button.setBackgroundResource(R.drawable.recover_btn);

                } else {
                    toolbar.setVisibility(View.GONE);
                    //restore_button.setBackground(null);
                }

            }
        });

    }


//    @Override
//    public int getCount() {
//        return alImageData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return alImageData.get(position);
////        return null;
//    }



    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return alImageData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbPic;
        ImageView ivPic;

        public ViewHolder(View itemView) {
            super(itemView);
            cbPic = itemView.findViewById(R.id.cbPic);
            ivPic = itemView.findViewById(R.id.ivPic);
        }
    }

    public ArrayList<ImageData> getSelectedItem() {
        ArrayList<ImageData> arrayList = new ArrayList();
        if (this.alImageData != null) {
            for (int i = 0; i < this.alImageData.size(); i++) {
                if ((this.alImageData.get(i)).getSelection()) {
                    arrayList.add(this.alImageData.get(i));
                }
            }
        }
        return arrayList;
    }

    public void setAllImagesUnseleted() {
        if (this.alImageData != null) {
            for (int i = 0; i < this.alImageData.size(); i++) {
                if ((this.alImageData.get(i)).getSelection()) {
                    (this.alImageData.get(i)).setSelction(false);
                }
            }
        }
    }
}
