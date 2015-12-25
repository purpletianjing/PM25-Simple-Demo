package com.thoughtworks.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.thoughtworks.myapplication.domain.PM25;
import com.thoughtworks.myapplication.service.AirServiceClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText;
    private TextView pm25TextView;
    private ProgressDialog loadingDialog;
    private ListView cityList;
    private SimpleAdapter simple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = (EditText) findViewById(R.id.edit_view_input);
        pm25TextView = (TextView) findViewById(R.id.text_view_pm25);
        cityList = (ListView) findViewById(R.id.city_list);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getString(R.string.loading_message));

        findViewById(R.id.button_query_pm25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQueryPM25Click();
            }
        });
        cityList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),                 simple.getItem(position).toString()
                + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onQueryPM25Click() {
        final String city = cityEditText.getText().toString();
        if (!TextUtils.isEmpty(city)) {
            showLoading();
            AirServiceClient.getInstance().requestPM25(city, new Callback<List<PM25>>() {
                @Override
                public void onResponse(Response<List<PM25>> response, Retrofit retrofit) {
                    showSuccessScreen(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    showErrorScreen();
                }
            });
        }
    }

    private void showSuccessScreen(Response<List<PM25>> response) {
        hideLoading();
        if (response != null) {
            populate(response.body());
        }
    }

    private void showErrorScreen() {
        hideLoading();
        pm25TextView.setText(R.string.error_message_query_pm25);
    }

    private void showLoading() {
        loadingDialog.show();
    }

    private void hideLoading() {
        loadingDialog.dismiss();
    }

    private void populate(List<PM25> data) {
        List<Map<String,String>> specificData=new ArrayList<Map<String,String>>();
        List<String> dataList = new ArrayList<String>();
        if (data != null && !data.isEmpty()) {
            for(int i = 0; i < data.size();i++) {
                PM25 pm25 = data.get(i);

                if(pm25.getPositionName() != null && pm25.getQuality()!=null) {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("quality",pm25.getQuality());
                    map.put("positionName",pm25.getPositionName());
                    map.put("pm2_5",pm25.getPm2_5());
                    dataList.add(pm25.getPositionName());
                    specificData.add(map);
                }
            }

        }
         simple=new SimpleAdapter(getApplicationContext(),
                specificData,
                R.layout.city_item,
                new String[]{"positionName"},
                new int[]{R.id.city_item});
        cityList.setAdapter(simple);
    }
}
