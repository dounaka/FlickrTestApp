package ca.kdounas.flickrphoto.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codepath.oauth.OAuthLoginActivity;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.client.FlickrClient;

public class LoginActivity extends OAuthLoginActivity<FlickrClient> {
    private Button mBtnLoginLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLoginLogout = (Button) findViewById(R.id.btn_login_logout);

    }



    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }



    public void flickrLogin(View view) {
            getClient().connect();
    }
}
