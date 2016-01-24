package ca.kdounas.flickrphoto.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.view.ZoomableView;

public class PhotoDetailActivity extends AppCompatActivity {
private ZoomableView mZoomableView;

    public static final String PARAM_PHOTO_UID = "photo.uid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("....");

        mZoomableView = (ZoomableView) findViewById(R.id.image_zoomable);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        final String photoUid = this.getIntent().getStringExtra(PARAM_PHOTO_UID);
        Toast.makeText(this, photoUid, Toast.LENGTH_SHORT).show();
        PhotoDb currentPhoto = PhotoDb.byPhotoUid(photoUid);
        getSupportActionBar().setTitle(currentPhoto.getName());
        ImageLoader.getInstance().displayImage(currentPhoto.getUrl(), mZoomableView);
    }
}
