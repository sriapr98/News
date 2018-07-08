package com.example.srikkanth.news.Common;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srikkanth.news.Everything.MultiSelectionSpinner;
import com.example.srikkanth.news.R;
import com.example.srikkanth.news.TopHeadline.Headlines;
import com.example.srikkanth.news.TopHeadline.SourceActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener{
    private Spinner spinner1,spinner2;
    private Button button1,button2;
    private HashMap<String,String> countryMap=new HashMap<String, String>();
    private MultiSelectionSpinner spinnerSourceHeadline,spinner;
    private EditText editTextFrom,editTextTo;
    private Spinner spinnerLanguage,spinnerSortBy;
    private Button bt;
    private HashMap<String,String> hashMapLanguage;
    private DatePickerDialog datePickerDialogFrom,datePickerDialogTo;
    private SimpleDateFormat dateFormatter;
    private List<String> items=new ArrayList<String>();
    private String selectedSources="",fromDate,toDate,Language,sortBy,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();
        spinner1=(Spinner)findViewById(R.id.country_spinner);
        Bundle bundle = new Bundle();
        button1=(Button)findViewById(R.id.country_button);
        button2=(Button)findViewById(R.id.source_button);
        findViewsById();
        setDateTimeField();
        populate(hashMapLanguage);
        items=additems(items);
        spinner.setItems(items);
        spinnerSourceHeadline.setItems(items);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()) {
                    List<String> sources = spinner.getSelectedStrings();
                    if (sources.size() == 1 && sources.get(0).equals("All Sources")) {
                        sources = additems(sources);
                    }
                    if (sources.size() == 0) {
                        sources = additems(sources);
                    }
                    for (int i = 0; i < sources.size(); i++) {
                        if (!sources.get(i).equals("All Sources")) {
                            selectedSources += sources.get(i) + ",";
                        }
                    }
                    selectedSources = selectedSources.substring(0, selectedSources.length() - 1);
                    if (selectedSources.equals("")) {
                        Toast.makeText(MainActivity.this, "Please Select Atleast One of the Sources", Toast.LENGTH_LONG).show();
                        Log.e("Selected", selectedSources);
                    } else {
                        fromDate = editTextFrom.getText().toString();
                        toDate = editTextTo.getText().toString();
                        Language = spinnerLanguage.getSelectedItem().toString();
                        Language = hashMapLanguage.get(Language);
                        sortBy = spinnerSortBy.getSelectedItem().toString();
                        url = "https://newsapi.org/v2/everything?sources=" + selectedSources + "&from=" + fromDate + "&to=" + toDate + "&language=" + Language + "&sortBy=" + sortBy + "&page=1&apiKey=2443647854264fa88a4908eb5daa8edc";
                        Intent intent = new Intent(MainActivity.this, SourceActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("source", "");
                        startActivity(intent);
                    }
                }
                else {
                    showSnack(false);
                }
            }
        });
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d12133")));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    String country = spinner1.getSelectedItem().toString();
                    countryMap = populateCountry(countryMap);
                    //url = "http://newsapi.org/v2/top-headlines?country=" + countryMap.get(country) + "&apiKey=2443647854264fa88a4908eb5daa8edc";
                    Intent intent = new Intent(MainActivity.this, Headlines.class);
                    country = countryMap.get(country).toString();
                    Log.e("1", country);
                    intent.putExtra("country", country);
                    intent.putExtra("source", "null");
                    startActivity(intent);
                }
                else {
                    showSnack(false);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()){
                List<String> sources = spinnerSourceHeadline.getSelectedStrings();
                if(sources.size()==1&&sources.get(0).equals("All Sources")){
                    sources=additems(sources);
                }
                if(sources.size()==0){
                    sources=additems(sources);
                }
                String source="";
                Log.e("sizehead",String.valueOf(sources.size()));
                for(int i=0;i<sources.size();i++){
                    if(!sources.get(i).equals("All Sources")) {
                        source += sources.get(i) + ",";
                    }
                }
                source=source.substring(0,source.length()-1);
                if(source.equals("")){
                    Toast.makeText(MainActivity.this,"Please Select Atleast One of the Sources",Toast.LENGTH_LONG).show();
                    Log.e("Selected",selectedSources);
                }
                else {
                    url="http://newsapi.org/v2/top-headlines?sources="+source+"&page=1&apiKey=2443647854264fa88a4908eb5daa8edc";
                    Intent intent=new Intent(MainActivity.this,SourceActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("country","null");
                    intent.putExtra("source","");
                    startActivity(intent);
                }
            }
            else {
                    showSnack(false);
                }
        }});
    }

    private HashMap<String,String> populateCountry(HashMap<String,String> countryMap) {
        countryMap.put("All","All");
        countryMap.put("India","in");
        countryMap.put("United Arab Emirates","ae");
        countryMap.put("Australia","au");
        countryMap.put("Canada","ca");
        countryMap.put("Colombia","co");
        countryMap.put("China","cn");
        countryMap.put("France","fr");
        countryMap.put("Germany","de");
        countryMap.put("United Kingdom","gb");
        countryMap.put("Indonesia","id");
        countryMap.put("Israel","il");
        countryMap.put("Japan","jp");
        countryMap.put("South Korea","kr");
        countryMap.put("Mexico","mx");
        countryMap.put("Malaysia","my");
        countryMap.put("New Zealand","nz");
        countryMap.put("Russia","ru");
        countryMap.put("Saudi Arabia","sa");
        countryMap.put("Singapore","sg");
        countryMap.put("United States","us");
        countryMap.put("South Africa","za");
        countryMap.put("All","All");
        return countryMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    public HashMap<String,String> getCountryMap() {
        return countryMap;
    }
    private void populate(HashMap<String, String> hashMapLanguage) {
        hashMapLanguage.put("English","en");
        hashMapLanguage.put("Arabic","ar");
        hashMapLanguage.put("German","de");
        hashMapLanguage.put("Spanish","es");
        hashMapLanguage.put("French","fr");
        hashMapLanguage.put("Italian","it");
        hashMapLanguage.put("Portuguese","pt");
        hashMapLanguage.put("Russian","ru");
        hashMapLanguage.put("Chinese","zh");
    }

    private void findViewsById() {
        editTextFrom = (EditText) findViewById(R.id.from);
        editTextFrom.setInputType(InputType.TYPE_NULL);
        editTextFrom.requestFocus();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        editTextTo = (EditText) findViewById(R.id.to);
        editTextTo.setInputType(InputType.TYPE_NULL);
        spinner = (MultiSelectionSpinner) findViewById(R.id.input1);
        spinnerSourceHeadline=(MultiSelectionSpinner)findViewById(R.id.input2);
        spinnerLanguage=findViewById(R.id.language_spinner);
        spinnerSortBy=findViewById(R.id.sort_by_spinner);
        bt =findViewById(R.id.getSelected);
        hashMapLanguage=new HashMap<String, String>();
    }
    private void setDateTimeField() {
        editTextFrom.setOnClickListener(this);
        editTextTo.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialogFrom = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextFrom.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialogTo = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editTextTo.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private List<String> additems(List<String> items) {
        items.add("All Sources");
        items.add("bbc-news");
        items.add("bbc-sport");
        items.add("cnbc");
        items.add("cnn");
        items.add("espn");
        items.add("espn-cric-info");
        items.add("fox-news");
        items.add("fox-sports");
        items.add("google-news");
        items.add("google-news-in");
        items.add("hacker-news");
        items.add("national-geographic");
        items.add("new-york-magazine");
        items.add("the-hindu");
        items.add("the-times-of-india");
        items.add("the-new-york-times");
        items.add("time");
        items.add("the-wall-street-journal");
        items.add("the-washington-times");
        items.add("the-economist");
        return items;
    }

    @Override
    public void onClick(View view) {
        if(view == editTextFrom) {
            datePickerDialogFrom.show();
        } else if(view == editTextTo) {
            datePickerDialogTo.show();
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
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

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
