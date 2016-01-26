package ca.kdounas.flickrphoto.persistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

@Table(name = "photo")
public class PhotoDb extends Model implements Photo {

    @Column(name = "uid")
    protected String uid;


    @Column(name = "name")
    protected String name;

    @Column(name = "owner")
    protected String owner;


    @Column(name = "url")
    protected String url;

    public PhotoDb() {
        super();
    }

    public PhotoDb(JSONObject object) {
        super();
        try {
            this.uid = object.getString("id");
            this.name = object.getString("title");
            this.owner = object.getString("owner");
            // http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
            this.url = "http://farm" + object.getInt("farm") + ".staticflickr.com/" + object.getInt("server") +
                    "/" + uid + "_" + object.getString("secret") + ".jpg";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static PhotoDb byPhotoUid(String uid) {
        return new Select().from(PhotoDb.class).where("uid = ?", uid).executeSingle();
    }

    public static List<PhotoDb> recentItems() {
        return new Select().from(PhotoDb.class).orderBy("id DESC").limit("100").execute();
    }


    public static void deleteByUid(String uid) {
        new Delete().from(PhotoDb.class).where("uid = ?", uid).execute();
    }


    public static void deleteAll() {
        new Delete().from(PhotoDb.class).execute();
    }

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
}
