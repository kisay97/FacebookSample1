package com.example.estsoft.myapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ImageView imageView;
    private TextView messageText;
    private TextView dateText;
    private LoginButton loginButton;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            messageText.setText("User ID: "
                            + loginResult.getAccessToken().getPermissions());

//            new GraphRequest(AccessToken.getCurrentAccessToken(),
//                    "/me/feed",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        @Override
//                        public void onCompleted(GraphResponse response) {
//                            JSONObject postsObject = response.getJSONObject();
//
//                            try {
//                                JSONArray postsArray = postsObject.getJSONArray("data");
//
//                                JSONObject post = (JSONObject)postsArray.get(0);
//                                messageText.setText(post.toString());
//
//                                String message = (String)post.get("message");
//                                messageText.setText(message);
//                                dateText.setText(post.toString());
//
//                                dateText.setText((String)post.get("id"));
//                            } catch (JSONException e) {
//                                messageText.setText(e.toString());
//                            }
//                        }
//                    }).executeAsync();

            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/photos",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            JSONObject jsonObject = response.getJSONObject();
                            messageText.setText(jsonObject.toString());

                            try {
                                JSONArray jsonArray = (JSONArray)jsonObject.get("data");
                                messageText.setText(jsonArray.get(0).toString());
                            }catch (Exception e){
                                messageText.setText(e.toString());
                            }
                        }
                    }).executeAsync();

//            new GraphRequest(AccessToken.getCurrentAccessToken(),
//                    "/1006859559439463",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        @Override
//                        public void onCompleted(GraphResponse response) {
//                            JSONObject jsonObject = response.getJSONObject();
//
//
//                            messageText.setText(jsonObject.toString());
//                        }
//                    }).executeAsync();

//            new GraphRequest(AccessToken.getCurrentAccessToken(),
//                    "/me/feed",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        @Override
//                        public void onCompleted(GraphResponse response) {
//                            JSONObject jsonObject = response.getJSONObject();
//                            messageText.setText(jsonObject.toString());
//
//                            JSONArray data=null;
//                            JSONArray paging=null;
//                            try{
//                                data = jsonObject.getJSONArray("data");
//                                paging = jsonObject.getJSONArray("paging");
//                            }catch (Exception e){
//                                messageText.setText(e.toString());
//                                return;
//                            }
//
////                            String keyString = "";
////                            Iterator<String> keys = jsonObject.keys();
////                            while(keys.hasNext()){
////                                keyString += keys.next() + ", ";
////                            }
////                            messageText.setText(keyString);
//
////                            String keyString = "";
////                            try {
////                                Iterator<String> keys = ((JSONObject) data.get(0)).keys();
////                                while(keys.hasNext()){
////                                    keyString += keys.next() + ", ";
////                                }
////                                messageText.setText(keyString);
////                            }catch (Exception e){
////                                messageText.setText(e.toString());
////                            }
//
////                            if(data != null) messageText.setText(data.toString());
////                            else messageText.setText("데이터 없음");
//
//                            if(paging != null) dateText.setText(paging.toString());
//                            else dateText.setText("패깅 없음");
//                        }
//                    }).executeAsync();

//            Bundle params = new Bundle();
//            params.putBoolean("redirect", false);
//            new GraphRequest(AccessToken.getCurrentAccessToken(),
//                    "/me/picture",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        @Override
//                        public void onCompleted(GraphResponse response) {
//                            JSONObject jsonObject = response.getJSONObject();
//
////                            try {
////                                JSONObject pictureData = jsonObject.getJSONObject("data");
////                                String pictureURLString = pictureData.getString("url");
////                                URL pictureURL = new URL(pictureURLString);
////
////                                messageText.setText(pictureURLString);
////                                imageView = (ImageView)findViewById(R.id.imageView);
////                                URLConnection conn = pictureURL.openConnection();
////                                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
////                                Bitmap bm = BitmapFactory.decodeStream(bis);
////                                bis.close();
////                                imageView.setImageBitmap(bm);
////                            }catch (Exception e){
////                                messageText.setText(e.toString());
////                            }
//
////                            if(jsonObject != null) {
////                                messageText.setText(jsonObject.toString());
////                                Log.d("user-id/picture", jsonObject.toString());
////                            } else{
////                                messageText.setText("자료없음");
////                            }
//                        }
//                    }).executeAsync();
        }

        @Override
        public void onCancel() {
            messageText.setText("Login attempt canceled");
        }

        @Override
        public void onError(FacebookException error) {
            messageText.setText("Login attempt failed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext()); //페이스북sdk(API) 초기화
        callbackManager = CallbackManager.Factory.create(); //콜백 메니저 초기화

        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        messageText = (TextView) findViewById(R.id.messageText);
        dateText = (TextView) findViewById(R.id.dateText);
        loginButton = (LoginButton)findViewById(R.id.fb_login_button);

        //로그인 버튼으로 로그인 할 때 받아올 권한들 설정
        //https://developers.facebook.com/docs/facebook-login/permissions 문서 참고
        loginButton.setReadPermissions(Arrays.asList("user_posts", "user_photos"));

        //로그인 행위에 대한 콜백(대답?)을 설정해줌
        loginButton.registerCallback(callbackManager, callback);
    }

    //페이스북 로그인으로 인한 로그인창(웹뷰)에서 메인 액티비티로 돌아왔을 떄 값을 받아오는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}