package com.example.bingspeedlimit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}


public class MainActivity extends AppCompatActivity {

    Button btnHit;
    TextView txtJson,speed;
    ProgressDialog pd;
    String aaspeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);
        speed = (TextView) findViewById(R.id.speedlimit);



        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://dev.virtualearth.net/REST/v1/Routes/SnapToRoad?points=19.2108,72.8747;19.2095,72.8641&includeTruckSpeedLimit=true&IncludeSpeedLimit=true&speedUnit=MPH&travelMode=driving&output=json&key=AtEZXSXwdHgb-TnLu1iqJUEmc4Jwy6u33NRb1f2ItXgx7TxkqMNCNsOm33IZUDhP");
            }
        });


    }


    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                    try {
                        // get JSONObject from JSON file
                        JSONObject obj = new JSONObject(line);

                        JSONArray userArray = obj.getJSONArray("resourceSets");
                        // fetch JSONObject named employee
                        JSONObject employee = userArray.getJSONObject(0);
                        aaspeed=employee.getJSONArray("resources").getJSONObject(0).getJSONArray("snappedPoints").getJSONObject(0).get("speedLimit").toString();
                        // get employee name and salary
//                        aaspeed = employee.getString("speedLimit");
                        Log.d("aaspeed","hgfdfgh");
//                salary = employee.getString("salary");
                        // set employee name and salary in TextView's

//                employeeSalary.setText("Salary: "+salary);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
//            txtJson.setText(result);
            speed.setText(aaspeed);

        }
    }
}