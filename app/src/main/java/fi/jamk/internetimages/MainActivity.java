package fi.jamk.internetimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //start showing images
        imageIndex = 0;
        showImage();
    }

    public void showImage(){
        //create a new AsyncTask object
        task = new DownloadImageTask();

        //start AsyncTask
        task.execute(PATH + images[imageIndex]);
    }


    //imageview object
    private ImageView imageView;

    //textview object
    private TextView textView;

    //progressbar object
    private ProgressBar progressBar;

    //example image path
    private final String PATH = "http://ptm.fi/android/";

    //example image names
    private String[] images = {"image1.jpg","image2.jpg","image3.jpg"};

    //which image is now visible
    private int imageIndex;

    //asynctask to load a new image
    private DownloadImageTask task;

    //swipe down und up values
    private float x1,x2;


    //method gets called when user performs any touch event on screen
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 < x2) { //left to right -> previous
                    imageIndex--;
                    if (imageIndex < 0) imageIndex = images.length-1;
                } else { //right to left -> next
                    imageIndex++;
                    if (imageIndex > (images.length-1)) imageIndex = 0;
                }
                showImage();
                break;
        }
        return false;
    }


    //asynctask class
    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

        //this is done in UI thread, nothing this time
        @Override
        protected void onPreExecute() {
            // show loading progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        //this is background thread, load image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            Bitmap bitmap = null;
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        //this is done in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
