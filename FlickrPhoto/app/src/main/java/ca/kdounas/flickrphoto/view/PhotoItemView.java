package ca.kdounas.flickrphoto.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.Photo;
import ca.kdounas.flickrphoto.persistance.PhotoDb;

/**
 * Created by dounaka on 1/24/16.
 */
public class PhotoItemView <T extends Photo> extends EntityView<T> {

    public ImageView imageView;
    private TextView mTxtCaption, mTxtUsername;

    private View panelPhoto;

    public PhotoItemView(Context ctx) {
        super(ctx);
    }

    @Override
    public int getViewResourceId() {
        return R.layout.view_item_photo;
    }

    @Override
    public void bindControls(Context ctx) {
        imageView = (ImageView) findViewById(R.id.img_photo);
        mTxtCaption = (TextView) findViewById(R.id.txt_image_caption);
        mTxtUsername = (TextView) findViewById(R.id.txt_user_name);

        panelPhoto = findViewById(R.id.panel_photo_item);
    }

    @Override
    protected void showEntity(T photo) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        mTxtCaption.setText(photo.getName());
        mTxtUsername.setText(photo.getOwner());
        imageLoader.displayImage(photo.getUrl(), imageView);
    }

    @Override
    public void select() {
        panelPhoto.setBackgroundColor(0x50FF4081);
    }

    @Override
    public void unselect() {
        panelPhoto.setBackgroundColor(0xFFFFFFFF);
    }

}
