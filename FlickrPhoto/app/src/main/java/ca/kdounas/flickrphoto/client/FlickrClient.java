package ca.kdounas.flickrphoto.client;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

/**
 * reference : com.codepath.apps.restclienttemplate.FlickrClient
 */


public class FlickrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = FlickrApi.class;
    public static final String REST_URL = "http://www.flickr.com/services";
    public static final String REST_CALLBACK_URL = "oauth://flickrrest";
    public static final String api_key = "a74f6e30173916d605029e05f4cf895f";
    public static final String app_secret = "052b65a27c816a34";
    private static String API_SEARCH_BY_TAG_URL = "?&format=json&nojsoncallback=1&method=flickr.photos.search&tag_mode=all&api_key=a74f6e30173916d605029e05f4cf895f&tags=";


    public FlickrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, api_key, app_secret, REST_CALLBACK_URL);
        setBaseUrl("https://api.flickr.com/services/rest");
    }

    public void getPhotoByTagname(final String tagname, final AsyncHttpResponseHandler handler) {
        final String searchUrl = API_SEARCH_BY_TAG_URL + tagname;
        this.client.get(getApiUrl(searchUrl), null, handler);
    }
}
