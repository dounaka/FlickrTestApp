package ca.kdounas.flickrphoto.persistance;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONObject;

import java.util.ArrayList;

@Table(name = "favorite")
public class FavoriteDb extends PhotoDb {

    public FavoriteDb() {
        super();
    }

    public FavoriteDb(JSONObject object) {
        super(object);
    }

    public static FavoriteDb findByUid(String uid) {
        return new Select().from(FavoriteDb.class).where("uid = ?", uid).executeSingle();
    }

    public static ArrayList<FavoriteDb> getAll() {
        return new Select().from(FavoriteDb.class).orderBy("id DESC").limit("300").execute();
    }

    public static void deleteAll() {
        new Delete().from(FavoriteDb.class).execute();
    }


    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}
