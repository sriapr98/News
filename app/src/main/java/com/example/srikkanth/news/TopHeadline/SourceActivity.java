package com.example.srikkanth.news.TopHeadline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.srikkanth.news.Common.ConnectivityReceiver;
import com.example.srikkanth.news.Common.HelperAdapter;
import com.example.srikkanth.news.Common.HelperClass;
import com.example.srikkanth.news.Common.MyApplication;
import com.example.srikkanth.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class SourceActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private String url;
    private List<HelperClass> helperClassList =new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textView,textViewPageNo,textViewGone;
    private ImageButton imageButton;
    private EditText editText;
    private int page_no=1;
    private Button buttonNext,buttonPrev;
    private String query="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_no=1;
        setContentView(R.layout.activity_source);
        textView=findViewById(R.id.title_text_view);
        textViewPageNo=findViewById(R.id.page_number_text_view);
        imageButton=findViewById(R.id.search_image_btn);
        editText=findViewById(R.id.searchbtn_edit_text);
        buttonNext=findViewById(R.id.next);
        buttonPrev=findViewById(R.id.previous);
        buttonPrev.setVisibility(View.INVISIBLE);
        textViewGone=findViewById(R.id.gone_text_view);
        if(!checkConnection()){
            showSnack(false);
        }
            imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    query = editText.getText().toString();
                    if (query.equals("")) {
                        Toast.makeText(SourceActivity.this, "Please enter a Source Word", Toast.LENGTH_LONG).show();
                    } else {
                        url = "https://newsapi.org/v2/everything?q=" + "\"" + query + "\"" + "&sortBy=relevancy&page=" + page_no + "&apiKey=2443647854264fa88a4908eb5daa8edc";
                        Log.e("searchurl", url);
                        loadHeadlineList(url);
                    }
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    buttonPrev.setVisibility(View.VISIBLE);
                    page_no++;
                    int pos = url.indexOf("page=");
                    int andpos = url.lastIndexOf('&');
                    url = url.substring(0, pos + 5) + page_no + url.substring(andpos, url.length());
                    Log.e("nexturl", url);
                    textViewPageNo.setText(String.valueOf(page_no));
                    loadHeadlineList(url);
                }
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()) {
                    if (page_no > 1) {
                        page_no--;
                        int pos = url.indexOf("page=");
                        int andpos = url.lastIndexOf('&');
                        url = url.substring(0, pos + 5) + page_no + url.substring(andpos, url.length());
                        Log.e("nexturl", url);
                        textViewPageNo.setText(String.valueOf(page_no));
                        loadHeadlineList(url);
                    } else {
                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        Log.e("intent url",url);
        String source=intent.getStringExtra("source");
        textView.setText(source);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d12133")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.headlines_recycler_view);
        loadHeadlineList(url);

    }
     private void loadHeadlineList(String url1) {
        Log.e("URLLLLLLL",url1);
        helperClassList.clear();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url1,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String source="",authour="",description="",url="",date="",check="",headline="",imgurl="";
                    int totalResults=response.getInt("totalResults");
                    Log.e("totalResults",String.valueOf(totalResults));
                    JSONArray articles=response.getJSONArray("articles");
                    Log.e("Articles Length",String.valueOf(articles.length()));
                    for(int i=0;i<articles.length();i++){
                        JSONObject obj=articles.getJSONObject(i);
                        JSONObject sourceObj=obj.getJSONObject("source");
                        source=sourceObj.getString("name");
                        authour=obj.getString("author");
                        description=obj.getString("description");
                        url=obj.getString("url");
                        date=obj.getString("publishedAt");
                        check=headline;
                        headline=obj.getString("title");
                        imgurl=obj.getString("urlToImage");
                        Log.v("url",imgurl);
                        if(!headline.equals(check)) {
                            HelperClass headlineobj = new HelperClass(source, authour, headline, description, imgurl, date, url);
                            helperClassList.add(headlineobj);
                        }

                    }
                    setuprecyclerview(helperClassList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSonerror","error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SourceActivity.this);

        //adding the string request to request queue
        requestQueue.add(jsonObjectRequest);
    }

    private void setuprecyclerview(List<HelperClass> helperClassList) {
        if(helperClassList.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewGone.setVisibility(View.GONE);
            Log.e("HIIIIIIIIIIII","1");
            HelperAdapter myAdapter = new HelperAdapter(this, helperClassList);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myAdapter);
            alphaAdapter.setDuration(3000);
            recyclerView.setAdapter(alphaAdapter);
            myAdapter.notifyDataSetChanged();
        }
        else {
            Log.e("Elseeeeeeee","2");
            textViewGone.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return true;
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
                .make(findViewById(R.id.source), message, Snackbar.LENGTH_LONG);

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
