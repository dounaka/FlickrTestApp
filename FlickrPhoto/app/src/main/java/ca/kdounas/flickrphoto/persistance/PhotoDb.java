package ca.kdounas.flickrphoto.persistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Table(name = "photo")
public class PhotoDb extends Model {

    @Column(name = "uid")
    private String uid;


    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private String owner;


    @Column(name = "url")
    private String url;

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

    public static ArrayList<PhotoDb> recentItems() {
        return new Select().from(PhotoDb.class).orderBy("id DESC").limit("300").execute();
    }


    public static void deleteAll() {
        new Delete().from(PhotoDb.class).execute();
    }

    public String getUrl() {
        return url;
    }


    public String getOwner() {
        return owner;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}
