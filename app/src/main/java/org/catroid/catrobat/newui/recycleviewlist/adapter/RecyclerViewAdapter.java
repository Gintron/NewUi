package org.catroid.catrobat.newui.recycleviewlist.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.catroid.catrobat.newui.R;
import org.catroid.catrobat.newui.data.ListItem;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnLongClickListener {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mDetailsView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mNameView = (TextView) itemView.findViewById(R.id.name_view);
            mDetailsView = (TextView) itemView.findViewById(R.id.details_view);
        }
    }

    private List<ListItem> mListItems;
    private int mItemLayoutId;
    private RecyclerViewMultiSelectionManager<ListItem> mMultiSelectionManager;
    private RecyclerViewAdapterDelegate delegate = null;

    private static int SELECTED_ITEM_BACKGROUND_COLOR = 0xFFDDDDDD;

    public RecyclerViewAdapter(List<ListItem> listItems, int itemLayout) {
        mListItems = listItems;
        mItemLayoutId = itemLayout;
        mMultiSelectionManager = new RecyclerViewMultiSelectionManager<ListItem>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = mListItems.get(position);

        holder.itemView.setOnLongClickListener(this);
        holder.mNameView.setText(item.getName());
        holder.mDetailsView.setText(item.getDetails());

        if (mMultiSelectionManager.getSelected(item)) {
            holder.mItemView.setBackgroundColor(SELECTED_ITEM_BACKGROUND_COLOR);
            holder.mImageView.setImageResource(R.drawable.ic_check_circle_black_24dp);
        } else {
            holder.mItemView.setBackgroundColor(0x00000000);
            holder.mImageView.setImageResource(item.getImageRes());
        }
    }

    public void setDelegate(RecyclerViewAdapterDelegate del) {
        delegate = del;
    }

    @Override
    public boolean onLongClick(View child) {
        RecyclerView recyclerView = (RecyclerView) child.getParent();

        int position = recyclerView.getChildAdapterPosition(child);

        if (position == RecyclerView.NO_POSITION) {
            return false;
        }

        ListItem item = mListItems.get(position);

        if (mMultiSelectionManager.isSelectable(item)) {
            mMultiSelectionManager.toggleSelected(item);

            notifyDataSetChanged();
            notifySelectionChanged();

            return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public List<ListItem> getSelectedItems() {
        return mMultiSelectionManager.getSelectedItems();
    }

    public void clearSelection() {
        mMultiSelectionManager.clearSelection();
        notifyDataSetChanged();
        notifySelectionChanged();
    }

    private void notifySelectionChanged() {
        if (delegate != null) {
            delegate.onSelectionChanged(this);
        }
    }
}
