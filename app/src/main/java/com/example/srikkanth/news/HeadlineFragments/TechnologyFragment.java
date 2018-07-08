package com.example.srikkanth.news.HeadlineFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.srikkanth.news.Common.HelperClass;
import com.example.srikkanth.news.Common.HelperAdapter;
import com.example.srikkanth.news.TopHeadline.Headlines;
import com.example.srikkanth.news.Common.MainActivity;
import com.example.srikkanth.news.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TechnologyFragment extends Fragment {


    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private HelperAdapter helperAdapter;
    private String url;
    private List<HelperClass> helperClassList =new ArrayList<>();
    public TechnologyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Business Fragment","fragemnt");
        View rootView= inflater.inflate(R.layout.fragment_entertainment, container, false);
        //helperClassList=headlines.getHelperClassList();
        recyclerView=(RecyclerView)rootView.findViewById(R.id.headlines_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String country="";
        Headlines headlines=(Headlines)getActivity();
        country =headlines.getCountry();
        if(!country.equals("null")) {
            url = "http://newsapi.org/v2/top-headlines?country=" + country + "&category=technology&apiKey=2443647854264fa88a4908eb5daa8edc";
        }
        else {
            Log.e("HI","poi");
            url = "http://newsapi.org/v2/top-headlines?category=technology&apiKey=2443647854264fa88a4908eb5daa8edc";
        }
        loadHeadlineList(url);
        return rootView;
    }
    private void loadHeadlineList(String url) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String source="",authour="",description="",url="",date="",check="",headline="",imgurl="";
                    JSONArray articles=response.getJSONArray("articles");
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        requestQueue.add(jsonObjectRequest);
    }
    private void setuprecyclerview(List<HelperClass> helperClassList) {
        HelperAdapter myAdapter=new HelperAdapter(getContext(), helperClassList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myAdapter);
        alphaAdapter.setDuration(2000);
        recyclerView.setAdapter(alphaAdapter);
        myAdapter.notifyDataSetChanged();
    }

}
