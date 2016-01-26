package ca.kdounas.flickrphoto.persistance;

import java.io.Serializable;

/**
 * Created by dounaka on 1/26/16.
 */
public interface Photo extends Serializable {
    String getUrl();

    String getOwner();

    String getUid();

    String getName();
}
