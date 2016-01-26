package ca.kdounas.flickrphoto.persistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ne supporte pas l'heritage, c'est bien dommage...
 * a creuser
 * https://github.com/pardom/ActiveAndroid/issues/14
 */
@Table(name = "favorite")
public class FavoriteDb extends Model implements Serializable, Photo {

    @Column(name = "uid")
    protected String uid;


    @Column(name = "name")
    protected String name;

    @Column(name = "owner")
    protected String owner;


    @Column(name = "url")
    protected String url;

    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getName() {
        return name;
    }

    public static FavoriteDb findByUid(String uid) {
        return new Select().from(FavoriteDb.class).where("uid = ?", uid).executeSingle();
    }

    public static ArrayList<FavoriteDb> recents() {
        return new Select().from(FavoriteDb.class).orderBy("id DESC").limit("50").execute();
    }

    public static void deleteAll() {
        new Delete().from(FavoriteDb.class).execute();
    }

    public void map(PhotoDb photo) {
        this.name = photo.name;
        this.owner = photo.owner;
        this.url = photo.url;
    }


}
