package ca.kdounas.flickrphoto.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.app.fragment.PhotoListFragment;
import ca.kdounas.flickrphoto.app.fragment.SearchByTagFragment;
import ca.kdounas.flickrphoto.persistance.TagDb;
import ca.kdounas.flickrphoto.view.PhotoItemView;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchByTagFragment.OnNewResultListener, PhotoListFragment.OnPhotoClickListener {
    private static final int CODE_RETURN_PHOTO_DETAIL = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchByTagFragment()).commit();
        }

        getSupportActionBar().setTitle("Search ...");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchByTagFragment()).commit();
        }
    }

    @Override
    public void onNewResults(final String tagname) {
        getSupportActionBar().setTitle(tagname);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PhotoListFragment()).commit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TagDb tag = new TagDb(tagname);
                tag.save();
            }
        }).start();
    }

    @Override
    public void onPhotoClick(final PhotoItemView photoView) {
        final Intent intentPhotoDetail = new Intent(this, PhotoDetailActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putInt(PhotoDetailActivity.PARAM_PHOTO_INDEX, photoView.position);
        intentPhotoDetail.putExtras(bundle);
        startActivity(intentPhotoDetail);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

    }



}
