package ca.kdounas.flickrphoto.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
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
import ca.kdounas.flickrphoto.persistance.FavoriteDb;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.view.ZoomOutPagerTransformer;
import ca.kdounas.flickrphoto.view.ZoomableView;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String PARAM_PHOTO_INDEX = "photo.index";
    public static final String BROADCAST_ACTION_PHOTO_DELETE = "photo.delete";
    public static final String BROADCAST_ACTION_PHOTO_CURRENT = "photo.current";
    public static final String BROADCAST_PARAM_PHOTO_INDEX = "photo.index";
    LocalBroadcastManager mLocalBroadcastManager;
    private int mCurrentPhotoIndex;
    private MenuItem mMenuAddFavorite, mMenuRemoveFavorite;
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
            mCurrentPhotoIndex = savedInstanceState.getInt(PARAM_PHOTO_INDEX, 0);
        else
            mCurrentPhotoIndex = this.getIntent().getIntExtra(PARAM_PHOTO_INDEX, 0);
    }

    private void notifyDelete(int index) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_PHOTO_DELETE);
        intent.putExtra(BROADCAST_PARAM_PHOTO_INDEX, index);
        mLocalBroadcastManager.sendBroadcast(intent);

    }

    private void notifyCurrent(int index) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_PHOTO_CURRENT);
        intent.putExtra(BROADCAST_PARAM_PHOTO_INDEX, index);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_PHOTO_INDEX, this.mCurrentPhotoIndex);
    }


    private void showPage(final int currentPage) {
        final PhotoDb photo = mAdapter.photos.get(currentPage);
        final int total = mAdapter.photos.size();
        mTxtCount.setText((currentPage + 1 + " / " + total));
        mCurrentPhotoIndex = currentPage;
        getSupportActionBar().setTitle(photo.getName());
        notifyCurrent(mCurrentPhotoIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocalBroadcastManager == null)
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
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
                mPager.setCurrentItem(mCurrentPhotoIndex);
                showPage(mCurrentPhotoIndex);
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
        if (next < mAdapter.photos.size())
            mPager.setCurrentItem(next);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo_detail, menu);
        mMenuAddFavorite = menu.findItem(R.id.action_add_favorite);
        mMenuRemoveFavorite = menu.findItem(R.id.action_remove_favorite);
        mMenuRemoveFavorite.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_favorite) {
            addToFavorite();
            return true;
        } else if (id == R.id.action_remove_favorite) {


            return true;
        } else if (id == R.id.action_delete) {
            deleteCurrentPhoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite() {
        final PhotoDb photo = mAdapter.photos.get(mCurrentPhotoIndex);
        if (FavoriteDb.findByUid(photo.getUid()) != null)

            return;

        mMenuAddFavorite.setVisible(false);
        mMenuRemoveFavorite.setVisible(true);
        FavoriteDb favorite = new FavoriteDb();
        favorite.map(photo);
        favorite.save();
    }

    private void deleteCurrentPhoto() {
        // mPager.removeAllViews();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final PhotoDb fotoToDelete = mAdapter.photos.get(mCurrentPhotoIndex);
                fotoToDelete.deleteByUid(fotoToDelete.getUid());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                final int nextIndex;
                if (mCurrentPhotoIndex == mAdapter.photos.size())
                    nextIndex = mCurrentPhotoIndex - 1;
                else
                    nextIndex = mCurrentPhotoIndex;
                mAdapter.photos.remove(mCurrentPhotoIndex);
                ArrayList<PhotoDb> backupPhotos = new ArrayList<>(); // save a data heavy read!
                backupPhotos.addAll(mAdapter.photos);
                // force a new adapter, this doesnt work with ViewPager -> mAdapter.notifyDataSetChanged();
                mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
                mAdapter.photos.addAll(backupPhotos);
                mPager.setAdapter(mAdapter);
                mPager.setCurrentItem(nextIndex);
                showPage(nextIndex);
                notifyDelete(mCurrentPhotoIndex);
            }
        }.execute();
    }

    public static class ImageFragment extends Fragment {
        private static final String KEY_PHOTO = "key.photo";
        PhotoDb photo;
        ZoomableView zoomableView;

        public ImageFragment() {
            super();
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
            zoomableView = new ZoomableView(getActivity());
            return zoomableView;
        }

        @Override
        public void onResume() {
            super.onResume();
            showPhoto();
        }

        public void showPhoto() {
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
            ImageFragment imgFragment = new ImageFragment();
            imgFragment.photo = photos.get(position);
            return imgFragment;
        }

        @Override
        public int getCount() {
            return photos.size();
        }


    }
}
