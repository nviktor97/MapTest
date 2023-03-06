package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseActivity extends AppCompatActivity {

    private Spinner list;
    private Button openMapBtn;
    private String currentCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getSupportActionBar().hide();

        list = (Spinner) findViewById(R.id.spinner);
        openMapBtn = (Button) findViewById(R.id.button);

        //A lenyíló lista feltöltése az öt várossal
        String[] items = new String[] { "Debrecen", "Nyíregyháza", "Győr", "Szeged", "Pécs" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        list.setAdapter(adapter);

        openMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCity = list.getSelectedItem().toString();

                //A hálózati műveleteket, mint a http request egy új Thread-en hajtom végre
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("https://wft-geo-db.p.rapidapi.com/v1/geo/adminDivisions?limit=1&offset=0&namePrefix=" + currentCity)
                                    .get()
                                    .addHeader("X-RapidAPI-Key", "79df976a90mshfbff79ff489d37dp1d8de4jsna448458a2093")
                                    .addHeader("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                                    .build();

                            //A visszakapott Json objektumot a Jackson parser-el kezelem és iterálok végig rajta és tárolom le a koordinátákat
                            try {
                                Response response = client.newCall(request).execute();

                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode responseJsonNode = mapper.readTree(response.body().string());

                                JsonNode items = responseJsonNode.get("data");

                                for (JsonNode item : items) {

                                    float lat = Float.parseFloat(item.get("latitude").asText());
                                    float lng = Float.parseFloat(item.get("longitude").asText());

                                    /*Beépített Google Maps api használata nélkül a következőképpen is megoldható a térkép betöltése,
                                    ezzel megnyílik a google maps app a megfelelő koordinátákkal
                                     */

                                    /*

                                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    intent.setPackage("com.google.android.apps.maps");
                                    startActivity(intent);

                                     */

                                    //Az eltárolt koordinátákat átadom az intentnek, hogy a következő activityben lekérdezhetőek legyenek
                                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putFloat("longitude", lng);
                                    extras.putFloat("latitude", lat);
                                    extras.putString("location", currentCity);
                                    intent.putExtras(extras);
                                    startActivity(intent);

                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });

    }

}