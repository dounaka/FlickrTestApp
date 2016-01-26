package ca.kdounas.flickrphoto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by dounaka on 1/24/16.
 */
public abstract class EntityView<T> extends FrameLayout {

    public int position;
    public T entity;

    public EntityView(Context ctx) {
        super(ctx);
        initView(ctx, null);
    }

    public EntityView(Context ctx, ViewGroup parent) {
        super(ctx);
        initView(ctx, parent);
    }

    public EntityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, null);
    }

    public EntityView(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        initView(context, null);
    }

    public abstract int getViewResourceId();

    public abstract void bindControls(Context ctx);

    private void initView(final Context ctx, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(getViewResourceId(), this, true);
        bindControls(ctx);
    }

    public final void show(final T entt) {
        this.entity = entt;
        showEntity(this.entity);
    }


    public void select() {
        setBackgroundColor(0x80FFFF99);
    }
    public void unselect() {
        setBackgroundColor(0xFFFFFFFF);
    }
     protected abstract void showEntity(T entity);
}
