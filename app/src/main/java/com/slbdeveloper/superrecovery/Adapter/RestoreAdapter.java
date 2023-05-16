package com.slbdeveloper.superrecovery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;

public class RestoreAdapter extends RecyclerView.Adapter<RestoreAdapter.ViewHolder> {

    private static final String TAG = "AdapterImage";

    ArrayList<ImageData> alImageData;
    Context context;
    int itemCount = 0;

    public RestoreAdapter(Context context, ArrayList<ImageData> alImageData) {
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


        } catch (Exception e) {
            //do nothing
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (alImageData.get(position).getFilePath().endsWith(".jpg") || alImageData.get(position).getFilePath().endsWith(".jpeg") || alImageData.get(position).getFilePath().endsWith(".png") || alImageData.get(position).getFilePath().endsWith(".gif")){

//                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((alImageData.get(position)).getFilePath()));
//                    intent.setDataAndType(Uri.parse((alImageData.get(position)).getFilePath()), "video/mp4");
//                    startActivity(intent);

                }else if(alImageData.get(position).getFilePath().endsWith(".3gp") || alImageData.get(position).getFilePath().endsWith(".mp4") || alImageData.get(position).getFilePath().endsWith(".mkv") || alImageData.get(position).getFilePath().endsWith(".flv")){

                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((alImageData.get(position)).getFilePath()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.parse((alImageData.get(position)).getFilePath()), "video/mp4");
                    context.startActivity(intent);

                }else if(alImageData.get(position).getFilePath().endsWith(".mp3")){



                }

            }
        });

    }

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


}
