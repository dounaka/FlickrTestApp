package ca.kdounas.flickrphoto.app;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ca.kdounas.flickrphoto.client.FlickrClient;


// reference : com.codepath.apps.restclienttemplate.FlickrClientApp


public class FlickrApplication extends com.activeandroid.app.Application {
	private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        FlickrApplication.context = this;
        
        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);

    }
    
    public static FlickrClient getRestClient() {
    	return (FlickrClient) FlickrClient.getInstance(FlickrClient.class, FlickrApplication.context);
    }
}