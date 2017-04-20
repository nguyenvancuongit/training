package com.example.nguyen.volleyexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "MyTag";
    String url = "http://hoikkuma-help.livepasstest.com/api/get_genre";
    String urlImage = "http://i.imgur.com/7spzG.png";

    // UI
    TextView responseText;
    ProgressDialog pDialog;
    ImageView mImageView1, mImageView2;
    NetworkImageView mNetworkImageView;

    // store
    CustomRequest postRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = (TextView) findViewById(R.id.txt1);
        responseText.setMovementMethod(new ScrollingMovementMethod());
        mImageView1 = (ImageView) findViewById(R.id.myImage1);
        mImageView2 = (ImageView) findViewById(R.id.myImage2);
        pDialog = new ProgressDialog(this);

        // show loading
        pDialog.setMessage("Loading...");
        pDialog.show();

        //================================Load image Use ImageRequest ==============================
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(urlImage,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        pDialog.dismiss();
                        mImageView1.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView1.setImageResource(R.mipmap.ic_launcher);
                        pDialog.dismiss();
                    }
                });
        // Access the RequestQueue through your singleton class.
        MyApplication.getInstance().getRequestQueue().add(request);

        //=======================Load image Use ImageLoader  =========================
        //                              (Recommend)
        //=======================Load image Use ImageLoader with imageView =========================
        // Get the ImageLoader through your singleton class.
        MyApplication.getInstance().getImageLoader().get(urlImage, ImageLoader.getImageListener(mImageView2,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        //=============Load image Use ImageLoader with NetworkImageView (for listView ) ===========
        // Get the NetworkImageView that will display the image.
        mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        // Set the URL of the image that should be loaded into this view, and
        // specify the ImageLoader that will be used to make the request.
        mNetworkImageView.setImageUrl(urlImage, MyApplication.getInstance().getImageLoader());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_string:
                pDialog.show();
                getRequestStringResponse();
                break;
            case R.id.btn_post_string:
                pDialog.show();
                postRequestStringResponse();
                break;
            case R.id.btn_post_JSONObject_custom:
                pDialog.show();
                postRequestJSONObjectCustomRequest();
                break;
        }
    }

    private void getRequestStringResponse() {
        // Making a GET Request use StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://httpbin.org/html",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        // Result handling
                        responseText.setText(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        });

        // Add the request to the queue
        MyApplication.getInstance().getRequestQueue().add(stringRequest);
    }

    private void postRequestStringResponse() {
        // Make a POST request use StringRequest
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJ = new JSONObject(response);
                            responseText.setText("" + responseJ.getJSONArray("data").getJSONObject(0).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseText.setText("error");
                        pDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("id", "hoikkuma-help");
                params.put("secret", "0ff584cebe6512892392a5c05437d72b");
                return params;
            }
        };
        MyApplication.getInstance().getRequestQueue().add(postRequest);
    }

    private void postRequestJSONObjectCustomRequest() {
        // Make a POST request use CustomRequest, JSONObject response
        Map<String, String> params = new HashMap<>();

        // the POST parameters:
        params.put("id", "hoikkuma-help");
        params.put("secret", "0ff584cebe6512892392a5c05437d72b");

        postRequest = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responseText.setText("" + response.getJSONArray("data").getJSONObject(0).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        pDialog.hide();
                    }
                }, Request.Priority.HIGH
        );

        // set Priofity for request (if you want to change it)
//        Priority.LOW // images, thumbnails, ...
//        Priority.NORMAL // residual
//        Priority.HIGH // descriptions, lists, ...
//        Priority.IMMEDIATE // login, logout, ...
        postRequest.setPriority(Request.Priority.HIGH);

        // set TAG for this request, use to cancel it by TAG
        postRequest.setTag(TAG);

//        DO NOT SAVE CACHE
//        postRequest.setShouldCache(false);

        MyApplication.getInstance().getRequestQueue().add(postRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // if you want just cancel one Request
        postRequest.cancel();

        // if you want cancel Requests by TAG
        if (MyApplication.getInstance().getRequestQueue() != null) {
            MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
        }

        // if you want cancel all request
        MyApplication.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
