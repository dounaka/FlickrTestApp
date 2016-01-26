package ca.kdounas.flickrphoto.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.app.FlickrApplication;
import ca.kdounas.flickrphoto.client.FlickrClient;
import ca.kdounas.flickrphoto.persistance.PhotoDb;

/**
 */
public class SearchByTagFragment extends Fragment {

    private OnNewResultListener mListener;
    private EditText mEditTagname;

    private ViewFlipper mViewFlipper;

    public SearchByTagFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View searchView = inflater.inflate(R.layout.fragment_search_by_tag, container, false);
        mEditTagname = (EditText) searchView.findViewById(R.id.edit_tagname);
        mViewFlipper = (ViewFlipper) searchView.findViewById(R.id.view_flipper_search);
        final Button btnSearch = (Button) searchView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.showNext();
                runSearch();
            }
        });
        return searchView;
    }

    private void runSearch() {
        final FlickrClient client = FlickrApplication.getRestClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                PhotoDb.deleteAll();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                final String tagValue = mEditTagname.getText().toString();
                client.getPhotoByTagname(tagValue, new JsonHttpResponseHandler() {
                    public void onSuccess(final JSONObject json) {
                        new AsyncTask<Void, Void,Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    PhotoDb photo = null;
                                    JSONArray photos = json.getJSONObject("photos").getJSONArray("photo");
                                    for (int x = 0; x < photos.length(); x++) {
                                        String uid = photos.getJSONObject(x).getString("id");
                                        photo = PhotoDb.byPhotoUid(uid);
                                        if (photo == null)
                                            photo = new PhotoDb(photos.getJSONObject(x));
                                        photo.save();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("debug", e.toString());
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void nada) {
                                mListener.onNewResults(tagValue);
                            }
                        }.execute();


                    }
                });
            }
        }.execute();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof OnNewResultListener) {
            mListener = (OnNewResultListener) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnNewResultListener {
        void onNewResults(String tagname);
    }
}
