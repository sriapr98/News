package com.example.srikkanth.news.Common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.srikkanth.news.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DisplayActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private TextView source,authour,title,description,url,date;
    RequestOptions requestOptions;
    ImageView imageView;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        if(!checkConnection()){
            showSnack(false);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d12133")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestOptions=new RequestOptions().centerCrop().error(R.drawable.no_image);
        source=findViewById(R.id.source_text_view);
        imageView=findViewById(R.id.head_image_view);
        authour=findViewById(R.id.authour_text_view);
        title=findViewById(R.id.headtitle_text_view);
        description=findViewById(R.id.headdesc_text_view);
        url=findViewById(R.id.url_text_view);
        date=findViewById(R.id.date_text_view);
        Intent intent=getIntent();
        HelperClass helperClass =(HelperClass)intent.getSerializableExtra("HeadlineObject");
        Log.e("Des", helperClass.getHeadline()+"\n"+ helperClass.getDescription());
        Glide.with(this).load(helperClass.getUrlImage()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imageView.setImageResource(R.drawable.no_image);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
        if(!helperClass.getSource().equals("null")) {
            source.setText("--"+ helperClass.getSource());
        }
        if(!helperClass.getAuthour().equals("null")){
            authour.setText(helperClass.getAuthour());
        }
        if(!helperClass.getDescription().equals("null")) {
            description.setText(helperClass.getDescription());
        }
        if(!helperClass.getHeadline().equals("null")) {
            title.setText(helperClass.getHeadline());
        }
        if(!helperClass.getPublishedDate().equals("null")) {
            formatdate(helperClass);
        }
        if(!helperClass.getUrl().equals("null")) {
            url.setText(helperClass.getUrl());
        }
    }
    private void formatdate(HelperClass helperClass) {
        String strDate= helperClass.getPublishedDate();
        Log.e("eor",strDate);
        Log.e("1",strDate);
        String pres="";
        SimpleDateFormat format=null;
        strDate=strDate.substring(0,19);
        Log.e("hi",strDate);
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = format.parse(strDate);
                SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm");
                formatter.setTimeZone(TimeZone.getDefault());
                pres=formatter.format(date);
            } catch (ParseException e) {
                Log.e("2", "2");
                e.printStackTrace();
            }
            date.setText(pres);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.source_rel), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
