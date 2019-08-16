package com.example.monappgrapimgservicewebv2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    String serviceWeb = "http://ziedzaier.com/wp-content/uploads/2018/11/imagegraph.txt";
    String str = "\n*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n";
    GraphView graph;
    ImageView image;
    Bitmap decodedImage;
    TextView txtValeurs;
    String message;

    LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(serviceWeb);
                System.out.println(str+"Sending 'GET' request to URL: " + url+str);

                HttpURLConnection client;
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");
                int responseCode = client.getResponseCode();
                System.out.println(str+"Response code: " + responseCode+str);

                InputStreamReader myInput = new InputStreamReader(client.getInputStream());
                BufferedReader in = new BufferedReader(myInput);
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject obj = new JSONObject(response.toString());
                String fname = obj.getString("fname");
                String lname = obj.getString("lname");
                String photo = obj.getString("photo");
                System.out.println(str+"fname : " + fname+"lname : " + lname+str);
                System.out.println(str+"photo : " + photo+str);

                JSONArray jsonarray = obj.getJSONArray("mesure");
                JSONObject jsonobject;

                //graphique
                //instancier votre graphview
                graph = (GraphView) findViewById (R.id.graph);
                DataPoint[] dataPoint = new DataPoint[jsonarray.length()];
                series = new LineGraphSeries<>();

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    int year = jsonobject.getInt("year");
                    double size = jsonobject.getDouble("size");
                    System.out.println(str+"year : " + year+" size : " + size+str);
                    dataPoint[i] = new DataPoint(year,size);
                }

                series.resetData(dataPoint);

                //image
                //encode une image a un string base64
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                image = (ImageView)findViewById(R.id.image);
                //decode un string base64 a une image
                imageBytes = Base64.decode(photo, Base64.DEFAULT);
                decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                //label
                message = "FirstName : " + fname+", LastName: " + lname;
                txtValeurs = (TextView) findViewById(R.id.LabelNom);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            graph.addSeries(series);
            image.setImageBitmap(decodedImage);
            txtValeurs.setText(message);
            super.onPostExecute(result);
        }
    }
}
