package ca.kdounas.flickrphoto.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.view.ZoomOutPagerTransformer;
import ca.kdounas.flickrphoto.view.ZoomableView;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String PARAM_PHOTO_INDEX = "photo.index";
    int photoIndex;
    private ViewPager mPager;
    private ImagePagerAdapter mAdapter;
    private TextView mTxtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("....");
        mTxtCount = (TextView) findViewById(R.id.txt_count);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(false, new ZoomOutPagerTransformer());
        mPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        showPage(position);
                    }
                });
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);


        if (savedInstanceState != null)
            photoIndex = savedInstanceState.getInt(PARAM_PHOTO_INDEX, 0);
        else
            photoIndex = this.getIntent().getIntExtra(PARAM_PHOTO_INDEX, 0);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_PHOTO_INDEX, this.photoIndex);
    }


    private void showPage(final int currentPage) {
        final int total = mAdapter.photos.size();
        mTxtCount.setText((currentPage + 1 + " / " + total));

        photoIndex = currentPage;
        getSupportActionBar().setTitle(mAdapter.photos.get(currentPage).getName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.photos.clear();
        mAdapter.notifyDataSetChanged();


        (new AsyncTask<Void, Void, List<PhotoDb>>() {
            @Override
            protected List<PhotoDb> doInBackground(Void... params) {
                return PhotoDb.recentItems();
            }

            @Override
            protected void onPostExecute(List<PhotoDb> photos) {
                mPager.setVisibility(View.INVISIBLE);
                mAdapter.photos.addAll(photos);
                mAdapter.notifyDataSetChanged();

                mPager.setCurrentItem(photoIndex);
                showPage(photoIndex);
                mPager.setVisibility(View.VISIBLE);
            }
        }).execute();
    }


    public void onPrevious(View view) {
        if (mPager.getCurrentItem() > 0)
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void onNext(View view) {
        final int next = mPager.getCurrentItem() + 1;
        if (next <= mAdapter.photos.size())
            mPager.setCurrentItem(next);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ImageFragment extends Fragment {
        private static final String KEY_PHOTO = "key.photo";
        PhotoDb photo;
        ZoomableView zoomableView;

        public ImageFragment() {

            super();
        }

        ImageFragment(PhotoDb foto) {
            super();
            photo = foto;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable(KEY_PHOTO, this.photo);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (savedInstanceState != null)
                this.photo = (PhotoDb) savedInstanceState.getSerializable(KEY_PHOTO);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            zoomableView = new ZoomableView(getContext());
            return zoomableView;
        }

        @Override
        public void onResume() {
            super.onResume();
            ImageLoader.getInstance().displayImage(photo.getUrl(), zoomableView);
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        final ArrayList<PhotoDb> photos = new ArrayList<>();


        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageFragment(photos.get(position));
        }

        @Override
        public int getCount() {
            return photos.size();
        }
    }
}
