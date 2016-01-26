package ca.kdounas.flickrphoto.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.app.PhotoDetailActivity;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.view.EntityRecyclerAdapter;
import ca.kdounas.flickrphoto.view.EntityView;
import ca.kdounas.flickrphoto.view.PhotoItemView;

public class PhotoListFragment extends Fragment {

    private OnPhotoClickListener mListener;
    private EntityRecyclerAdapter<PhotoDb> mPhotoAdater;
    private RecyclerView mRecyclerViewPhoto;
    private int itemToScroll = -1;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View mainView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerViewPhoto = (RecyclerView) mainView.findViewById(R.id.recyclerview);
        mRecyclerViewPhoto.setHasFixedSize(true);
        mRecyclerViewPhoto.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPhotoAdater != null) {
            if (itemToScroll != -1) {
                mPhotoAdater.notifyDataSetChanged();
                mPhotoAdater.positionCurrent = itemToScroll;
                mRecyclerViewPhoto.smoothScrollToPosition(itemToScroll);
                itemToScroll = -1;
            }
            return;
        }

        mPhotoAdater = new EntityRecyclerAdapter<PhotoDb>(new ArrayList<PhotoDb>()) {
            @Override
            public EntityView<PhotoDb> getEntityItemView(ViewGroup parent) {
                return new PhotoItemView(PhotoListFragment.this.getActivity());
            }
        };
        mPhotoAdater.itemClickListener = new EntityRecyclerAdapter.ItemClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoClick((PhotoItemView) v);
            }
        };
        mRecyclerViewPhoto.setAdapter(mPhotoAdater);
        if (getActivity() instanceof OnPhotoClickListener) {
            mListener = (OnPhotoClickListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
        new AsyncTask<Void, Void, List<PhotoDb>>() {
            @Override
            protected List<PhotoDb> doInBackground(Void... params) {
                return PhotoDb.recentItems();
            }

            @Override
            protected void onPostExecute(final List<PhotoDb> fotos) {
                mPhotoAdater.entities.addAll(fotos);
                mPhotoAdater.notifyDataSetChanged();
            }
        }.execute();

        initBroadcast();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initBroadcast() {
        LocalBroadcastManager broadcastMgr = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PhotoDetailActivity.BROADCAST_ACTION_PHOTO_CURRENT);
        intentFilter.addAction(PhotoDetailActivity.BROADCAST_ACTION_PHOTO_DELETE);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int index = intent.getIntExtra(PhotoDetailActivity.BROADCAST_PARAM_PHOTO_INDEX, -1);
                if (index == -1) return;
                if (intent.getAction().equals(PhotoDetailActivity.BROADCAST_ACTION_PHOTO_CURRENT))
                    itemToScroll = index;
                else if (intent.getAction().equals(PhotoDetailActivity.BROADCAST_ACTION_PHOTO_DELETE)) {
                    mPhotoAdater.entities.remove(index);
                    mPhotoAdater.notifyDataSetChanged();
                }


            }
        };
        broadcastMgr.registerReceiver(broadcastReceiver, intentFilter);
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(PhotoItemView photoView);
    }
}
