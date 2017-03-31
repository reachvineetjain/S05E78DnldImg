package com.nehvin.s05e78dnldimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView = null;
    Button downloadButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        downloadButton = (Button) findViewById(R.id.downloadButton);
        Log.i("info","On Create Method done");
    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Log.i("info","Inside download task");
            URL url = null;
            HttpURLConnection connection = null;
            InputStream stream = null;
            Bitmap bmpMap = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                Log.i("info","Connection completed");
                stream = connection.getInputStream();
                Log.i("info","stream obtained");
                bmpMap = BitmapFactory.decodeStream(stream);
                Log.i("info","Stream decoded and converted to bitmap");
                return bmpMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(stream != null)
                        stream.close();
                    if(connection != null)
                        connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    public void getImage(View view)
    {
        Log.i("info","inside button clicked");
        DownloadTask task = new DownloadTask();
        Bitmap img;
        try {
            img = task.execute("https://upload.wikimedia.org/wikipedia/en/5/5f/TomandJerryTitleCardc.jpg").get();
            Log.i("info","called execute task");
            if (img!=null)
                imageView.setImageBitmap(img);
            else
                Log.i("info","download failed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}