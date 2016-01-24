package ca.kdounas.flickrphoto.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.view.EntityRecyclerAdapter;
import ca.kdounas.flickrphoto.view.EntityView;
import ca.kdounas.flickrphoto.view.PhotoItemView;

public class PhotoListFragment extends Fragment {

    private OnPhotoClickListener mListener;
    private EntityRecyclerAdapter<PhotoDb> photoAdater;
    private RecyclerView mRecyclerViewPhoto;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_photo_list, container, false);
        mRecyclerViewPhoto = (RecyclerView) mainView.findViewById(R.id.recyclerviewphoto);
        mRecyclerViewPhoto.setHasFixedSize(true);
        mRecyclerViewPhoto.setLayoutManager(new LinearLayoutManager(getContext()));
        photoAdater = new EntityRecyclerAdapter<PhotoDb>(new ArrayList<PhotoDb>()) {
            @Override
            public EntityView<PhotoDb> getEntityItemView(ViewGroup parent) {
                return  new PhotoItemView(PhotoListFragment.this.getContext());
            }
        };

        photoAdater.itemClickListener = new EntityRecyclerAdapter.ItemClickListener() {
            @Override
            public void onClick(View v) {
                PhotoItemView photoView = (PhotoItemView)v;
                mListener.onPhotoClick(photoView.entity.getUid());
            }
        };
        mRecyclerViewPhoto.setAdapter(photoAdater);
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoClickListener) {
            mListener = (OnPhotoClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        for (PhotoDb photo : PhotoDb.recentItems()) {
            photoAdater.entities.add(photo);
        }
        photoAdater.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPhotoClickListener {
        // TODO: Update argument type and name
        void onPhotoClick(final String photouid);
    }
}
