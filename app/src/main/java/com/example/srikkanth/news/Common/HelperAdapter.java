package com.example.srikkanth.news.Common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.srikkanth.news.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by srikkanth on 1/6/18.
 */

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.MyViewHolder> {
    private Context context;
    private List<HelperClass> headlist;
    RequestOptions requestOptions;
    private static ClickListener clickListener;
    public HelperAdapter(Context context, List<HelperClass> headlist) {
        this.context = context;
        this.headlist = headlist;
        requestOptions=new RequestOptions().error(R.drawable.no_image);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.headline_list_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.textView.setText(headlist.get(position).getHeadline());
        setDate(holder,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToView(holder.getAdapterPosition());
            }
        });
        Glide.with(context).load(headlist.get(position).getUrlImage()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.placeHolder.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.placeHolder.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.imageView);
    }

    private void GoToView(int adapterPosition) {
        Intent intent=new Intent(context,DisplayActivity.class);
        HelperClass helperClass =headlist.get(adapterPosition);
        intent.putExtra("HeadlineObject", helperClass);
        Log.e("Hi","hiview");
        context.startActivity(intent);
    }

    private void setDate(MyViewHolder holder, int position) {
        try
        {
            String date=headlist.get(position).getPublishedDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date past = format.parse(date);
            if(position==0){
                Log.e("DatePast",String.valueOf(past.getHours()+"\t"+past.getMinutes()));
            }
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if(seconds<60){
                holder.dateview.setText(String.valueOf(seconds)+" seconds ago");
            }
            else if(minutes<60) {
                holder.dateview.setText(String.valueOf(minutes)+" minutes ago");
            }
            else if(hours<24) {
                holder.dateview.setText(String.valueOf(hours)+" hours ago");
            }
            else {
                holder.dateview.setText(String.valueOf(days)+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return headlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView textView,dateview;
        ImageView imageView;
        ImageView placeHolder;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            textView=(TextView)itemView.findViewById(R.id.headline_text_view);
            dateview=(TextView) itemView.findViewById(R.id.time_text_view);
            imageView=(ImageView)itemView.findViewById(R.id.headline_image_view);
            placeHolder=(ImageView) itemView.findViewById(R.id.progressbar);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        HelperAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
