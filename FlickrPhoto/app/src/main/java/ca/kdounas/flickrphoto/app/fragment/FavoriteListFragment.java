package ca.kdounas.flickrphoto.app.fragment;

import java.util.List;

import ca.kdounas.flickrphoto.persistance.FavoriteDb;
import ca.kdounas.flickrphoto.view.EntityView;
import ca.kdounas.flickrphoto.view.FavoriteItemView;
import ca.kdounas.flickrphoto.view.PhotoItemView;

/**
 * Created by dounaka on 1/26/16.
 */
public class FavoriteListFragment extends EntityListFragment<FavoriteDb>{
    @Override
    protected EntityView<FavoriteDb> getItemView() {
        return new FavoriteItemView(getActivity());
    }
    @Override
    protected List<FavoriteDb> getEntities() {
        return FavoriteDb.recents();
    }
}
