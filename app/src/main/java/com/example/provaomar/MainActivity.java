package com.example.provaomar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.meuBotao);

        ListView listaPhone = findViewById(R.id.mylistview);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://jsonplaceholder.typicode.com/users";
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                List<String> city = new ArrayList<String>();
                                try {
                                    JSONArray jsonObject = new JSONArray(response);
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        city.add(jsonObject.getJSONObject(i).get("phone").toString());
                                    }
                                    listaPhone.setAdapter(new ArrayAdapter<String>(
                                            getApplicationContext(),
                                            android.R.layout.simple_list_item_1,
                                            android.R.id.text1,
                                            city
                                    ));
                                    listaPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String cidade = (String) listaPhone.getItemAtPosition(position);
                                            String lat = "";
                                            String lng = "";
                                            Toast.makeText(MainActivity.this, "Voce escolheu : " + cidade, Toast.LENGTH_SHORT).show();
                                            try {
                                                JSONArray jsonObject = new JSONArray(response);

                                                for (int i = 0; i < jsonObject.length(); i++) {

                                                    String phonesPar = jsonObject.getJSONObject(i).get("phone").toString();

                                                    if (cidade.contains(phonesPar)) {
                                                        lat = jsonObject.getJSONObject(i).getJSONObject("address").getJSONObject("geo").get("lat").toString();
                                                        lng = jsonObject.getJSONObject(i).getJSONObject("address").getJSONObject("geo").get("lng").toString();
                                                    }

                                                }
                                                Intent maps = new Intent(MainActivity.this, MapsActivity.class);

                                                maps.putExtra("lat", lat);
                                                maps.putExtra("lng", lng);
                                                startActivity(maps);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                System.err.println(error.toString());
                            }
                        }
                );

                MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
            }
        });
    }

}