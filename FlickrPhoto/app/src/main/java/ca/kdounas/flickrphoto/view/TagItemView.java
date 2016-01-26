package ca.kdounas.flickrphoto.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

import ca.kdounas.flickrphoto.R;
import ca.kdounas.flickrphoto.persistance.PhotoDb;
import ca.kdounas.flickrphoto.persistance.TagDb;

/**
 * Created by dounaka on 1/24/16.
 */
public class TagItemView extends EntityView<TagDb> {
    @Override
    protected void showEntity(TagDb entity) {
        mTxtTag.setText(entity.getTag());
        mTxtDate.setText(new Date(entity.getSearchOn()).toString());
    }

    private TextView mTxtTag, mTxtDate;

    public TagItemView(Context ctx) {
        super(ctx);
    }

    @Override
    public int getViewResourceId() {
        return R.layout.view_item_tag;
    }

    @Override
    public void bindControls(Context ctx) {
        mTxtTag = (TextView) findViewById(R.id.txt_image_caption);
        mTxtDate = (TextView) findViewById(R.id.txt_user_name);
    }




}
