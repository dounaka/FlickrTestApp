package ca.kdounas.flickrphoto.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.TagDb;
import ca.kdounas.flickrphoto.view.EntityRecyclerAdapter;
import ca.kdounas.flickrphoto.view.EntityView;
import ca.kdounas.flickrphoto.view.TagItemView;

public abstract  class EntityListFragment<T> extends Fragment {

    private RecyclerView mRecyclerViewTag;
    private EntityRecyclerAdapter<T> mTagRecyclerAdapter;

    public EntityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerViewTag = (RecyclerView) mainView.findViewById(R.id.recyclerview);
        mRecyclerViewTag.setHasFixedSize(true);
        mRecyclerViewTag.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mainView;
    }

    protected abstract EntityView<T> getItemView();

    protected abstract List<T> getEntities();


    @Override
    public void onResume() {
        super.onResume();
        if (mTagRecyclerAdapter != null) return;
        mTagRecyclerAdapter = new EntityRecyclerAdapter<T>(new ArrayList<T>()) {
            @Override
            public EntityView<T> getEntityItemView(ViewGroup parent) {
                return getItemView();
            }
        };
        mTagRecyclerAdapter.entities.addAll(getEntities());
        mRecyclerViewTag.setAdapter(mTagRecyclerAdapter);
    }



}
