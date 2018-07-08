package com.example.srikkanth.news.TopHeadline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.master.glideimageview.GlideImageView;

import com.example.srikkanth.news.Common.ConnectivityReceiver;
import com.example.srikkanth.news.Common.HelperAdapter;
import com.example.srikkanth.news.Common.HelperClass;
import com.example.srikkanth.news.Common.MainActivity;
import com.example.srikkanth.news.Common.MyApplication;
import com.example.srikkanth.news.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.srikkanth.news.HeadlineFragments.BusinessFragment;
import com.example.srikkanth.news.HeadlineFragments.EntertainmentFragment;
import com.example.srikkanth.news.HeadlineFragments.GeneralFragment;
import com.example.srikkanth.news.HeadlineFragments.HealthFragment;
import com.example.srikkanth.news.HeadlineFragments.ScienceFragment;
import com.example.srikkanth.news.HeadlineFragments.SportsFragment;
import com.example.srikkanth.news.HeadlineFragments.TechnologyFragment;

public class Headlines extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<HelperClass> helperClassList = new ArrayList<>();
    private ImageButton imageButton;
    private static final String TAG = MainActivity.class.getSimpleName();
    //private RecyclerView recyclerView;
    private HelperAdapter helperAdapter;
    private TextView title;
    private String query="Hi";
    private HashMap<String,String> countryMap=new HashMap<String, String>();
    // Movies json url
    private String url,country,source;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);
        imageButton=findViewById(R.id.search_image_btn);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d12133")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //recyclerView=(RecyclerView)findViewById(R.id.headlines_recycler_view);
        title=(TextView)findViewById(R.id.title_text_view);
        viewPager =findViewById(R.id.viewpager);
        checkConnection();
        addTabs(viewPager);
        tabLayout =findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Intent intent=getIntent();
        country=intent.getStringExtra("country");
        countryMap=populate(countryMap);
        title.setText(countryMap.get(country).toString());
        Log.e("country",country);
        source=intent.getStringExtra("source");
        Log.e("Source",source+"\t"+country);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()) {
                    EditText editText = findViewById(R.id.searchbtn_edit_text);
                    query = editText.getText().toString();
                    if (!query.equals("")) {
                        url = "https://newsapi.org/v2/top-headlines?q=" + query + "&page=1&apiKey=2443647854264fa88a4908eb5daa8edc";
                        Intent intent = new Intent(Headlines.this, SourceActivity.class);
                        Log.e("url", url);
                        intent.putExtra("url", url);
                        Log.e("Hi", "hi");
                        Log.e("query", "1" + "\t" + query);
                        intent.putExtra("source", "News about :" + query);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a search query", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //loadHeadlineList(url);
    }

    public String getCountry() {
        if(!country.equals("All")){
            Log.e("cont",country);
            return country;
        }
            return "null";
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EntertainmentFragment(), "Entertainment");
        adapter.addFrag(new BusinessFragment(), "Business");
        adapter.addFrag(new GeneralFragment(), "General");
        adapter.addFrag(new HealthFragment(), "Health");
        adapter.addFrag(new ScienceFragment(),"Science");
        adapter.addFrag(new SportsFragment(),"Sports");
        adapter.addFrag(new TechnologyFragment(),"Technology");
        viewPager.setAdapter(adapter);
    }

    private HashMap<String,String> populate(HashMap<String, String> countryMap) {
        countryMap.put("All","All");
        countryMap.put("in","India");
        countryMap.put("ae","United Arab Emirates");
        countryMap.put("au","Australia");
        countryMap.put("ca","Canada");
        countryMap.put("co","Colombia");
        countryMap.put("cn","China");
        countryMap.put("fr","France");
        countryMap.put("de","Germany");
        countryMap.put("gb","United Kingdom");
        countryMap.put("id","Indonesia");
        countryMap.put("il","Israel");
        countryMap.put("jp","Japan");
        countryMap.put("kr","South Korea");
        countryMap.put("mx","Mexico");
        countryMap.put("my","Malaysia");
        countryMap.put("nz","New Zealand");
        countryMap.put("ru","Russia");
        countryMap.put("sa","Saudi Arabia");
        countryMap.put("sg","Singapore");
        countryMap.put("us","United States");
        countryMap.put("za","South Africa");
        return countryMap;
    }
    public List<HelperClass> getHelperClassList() {
        return helperClassList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                .make(findViewById(R.id.source_linear_layout), message, Snackbar.LENGTH_LONG);

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