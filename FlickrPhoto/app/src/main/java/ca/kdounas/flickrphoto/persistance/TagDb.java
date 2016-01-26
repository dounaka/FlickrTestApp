package ca.kdounas.flickrphoto.persistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dounaka on 1/24/16.
 */

@Table(name = "tag")
public class TagDb extends Model {


    @Column(name = "tag")
    private String tag;


    @Column(name = "searchon")
    private Long searchOn = System.currentTimeMillis();


    public TagDb() {
        super();
    }

    public TagDb(String tg) {
        super();
        this.tag = tg;
    }

    public static List<TagDb> getLastSearches() {
        return new Select().from(TagDb.class).orderBy("id DESC").limit("50").execute();
    }

    public String getTag() {
        return tag;
    }

    public Long getSearchOn() {
        return searchOn;
    }


}
