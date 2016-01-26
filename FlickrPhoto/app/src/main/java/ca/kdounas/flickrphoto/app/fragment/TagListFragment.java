package ca.kdounas.flickrphoto.app.fragment;

import java.util.List;

import ca.kdounas.flickrphoto.persistance.TagDb;
import ca.kdounas.flickrphoto.view.EntityView;
import ca.kdounas.flickrphoto.view.TagItemView;

public class TagListFragment extends EntityListFragment<TagDb> {
    @Override
    protected EntityView<TagDb> getItemView() {
        return new TagItemView(getActivity());
    }

    @Override
    protected List<TagDb> getEntities() {
        return TagDb.getLastSearches();
    }
}