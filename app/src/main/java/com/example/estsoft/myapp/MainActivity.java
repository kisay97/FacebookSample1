package com.example.estsoft.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView textLabel;
    private LoginButton loginButton;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            textLabel.setText("User ID: "
                            + loginResult.getAccessToken().getPermissions());
            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            JSONObject postsObject = response.getJSONObject();

                            try {
                                JSONArray postsArray = postsObject.getJSONArray("data");

                                Object post = postsArray.get(0);
                                textLabel.setText(post.toString());
                            } catch (JSONException e) {
                                textLabel.setText(e.toString());
                            }
                        }
                    }).executeAsync();
        }

        @Override
        public void onCancel() {
            textLabel.setText("Login attempt canceled");
        }

        @Override
        public void onError(FacebookException error) {
            textLabel.setText("Login attempt failed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext()); //페이스북sdk(API) 초기화
        callbackManager = CallbackManager.Factory.create(); //콜백 메니저 초기화

        setContentView(R.layout.activity_main);

        textLabel = (TextView) findViewById(R.id.textLabel);
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