package pl.cubesoft.smagabakery.adapter;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;

import pl.cubesoft.smagabakery.ImageLoader;
import pl.cubesoft.smagabakery.R;
import pl.cubesoft.smagabakery.model.Person;


public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

    private static final String STATE_EXPANDED_ITEMS = "state_expanded_items";
    private static final String STATE_ITEM_COLORS = "state_item_colors";
    private final List<Person> items = new ArrayList<>();
    private final OnPersonListAdapterInteractionListener listener;
    private final ImageLoader imageLoader;
    private final Object imageLoadTag;
    private HashSet<Long> expandedItems = new HashSet<>();
    private HashMap<Long, Integer> itemsColors = new HashMap<>();

    


    public interface OnPersonListAdapterInteractionListener {
        void onPersonListItemClick(int position);

        void onPersonListItemLongClick(int position);

        void onPersonListItemDeleteClick(int position);
    }


    public PersonListAdapter(OnPersonListAdapterInteractionListener listener, ImageLoader imageLoader, Object imageLoadTag) {
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.imageLoadTag = imageLoadTag;

        setHasStableIds(true);

    }

    public void loadState(Bundle bundle) {
        expandedItems = (HashSet<Long>) bundle.getSerializable(STATE_EXPANDED_ITEMS);
        itemsColors = (HashMap<Long, Integer>) bundle.getSerializable(STATE_ITEM_COLORS);
    }

    public void saveState(Bundle bundle) {
        bundle.putSerializable(STATE_EXPANDED_ITEMS, expandedItems);
        bundle.putSerializable(STATE_ITEM_COLORS, itemsColors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_person_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Person item = getItem(position);
        holder.name.setText(getName(item));
        holder.dateBday.setText(item.getDateBDay());


        holder.description.setText(item.getDescription() != null ? Html.fromHtml(item.getDescription()) : null);
        Linkify.addLinks(holder.description, Linkify.ALL);

        if (!isItemExpanded(position)) {
            holder.description.setMaxLines(2);
        } else {
            holder.description.setMaxLines(Integer.MAX_VALUE);
        }

        if (item.getAvatar() != null) {
            imageLoader.load(Uri.parse(item.getAvatar()), holder.photo, ImageLoader.Transform.CIRCLE, imageLoadTag);
        } else {
            holder.photo.setImageBitmap(null);
        }


        Integer color = itemsColors.get(getItemId(position));
        if (color != null) {
            holder.itemView.setBackgroundColor(color);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }


        holder.itemView.setOnClickListener(v -> {
            if (null != listener) {
                listener.onPersonListItemClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (null != listener) {

                listener.onPersonListItemLongClick(position);
                return true;
            }
            return false;
        });

        holder.delete.setOnClickListener(view -> {
                    if (null != listener) {

                        listener.onPersonListItemDeleteClick(position);
                    }
                }
        );
    }

    private String getName(Person item) {
        return item.getFirstName() + " " + item.getLastName();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return getName(getItem(position)).hashCode();
    }

    public Person getItem(int position) {
        return items.get(position);
    }

    public void setItems(Collection<Person> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setItemExpanded(int position, boolean expanded) {
        if (expanded) {
            expandedItems.add(getItemId(position));
        } else {
            expandedItems.remove(getItemId(position));
        }
        notifyItemChanged(position);
    }

    public boolean isItemExpanded(int position) {
        return expandedItems.contains(getItemId(position));

    }

    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void setItemBackgroundColor(int position, int color) {
        itemsColors.put(getItemId(position), color);
        notifyItemChanged(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.date_bday)
        TextView dateBday;

        @BindView(R.id.delete)
        View delete;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.photo)
        ImageView photo;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }



    }
}
