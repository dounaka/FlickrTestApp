package ca.kdounas.flickrphoto.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic adapter
 */
public abstract class EntityRecyclerAdapter<ET> extends RecyclerView.Adapter<EntityRecyclerAdapter.EntityViewHolder> implements View.OnClickListener {

    public final List<ET> entities;
    public int positionCurrent = -1;
    public ItemClickListener itemClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EntityRecyclerAdapter(List<ET> enttes) {
        entities = enttes;
    }

    public abstract EntityView<ET> getEntityItemView(ViewGroup parent);

    // Create new views (invoked by the layout manager)
    @Override
    public EntityRecyclerAdapter.EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EntityViewHolder vh = new EntityViewHolder(getEntityItemView(parent), this);
        return vh;
    }

    @Override
    public void onClick(final View v) {
        if (itemClickListener == null)
            return;
        itemClickListener.onClick(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EntityViewHolder holder, int pos) {
        holder.entityView.show(entities.get(pos));
        holder.entityView.position = (pos);
        if (positionCurrent != -1) {
            if (holder.entityView.position == positionCurrent)
                holder.entityView.select();
            else
                holder.entityView.unselect();
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public interface ItemClickListener {
        void onClick(View v);
    }

    // Provide a reference to the views for each data item Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        public EntityView entityView;

        public EntityViewHolder(EntityView ev, View.OnClickListener clicklistener) {
            super(ev);
            entityView = ev;
            entityView.setOnClickListener(clicklistener);
        }
    }
}