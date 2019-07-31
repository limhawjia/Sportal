package wjhj.orbital.sportsmatchfindingapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import wjhj.orbital.sportsmatchfindingapp.BR;

public abstract class DataBindingListAdapter<T> extends ListAdapter<T, DataBindingListAdapter.DataBindingViewHolder<T>> {

    private ItemClickListener<T> listener;
    private LifecycleOwner lifecycleOwner;

    /**
     * The variable T in your databinding layout must be called "item", and the listener must be
     * called "listener"
     *
     * @param diffCallback diffCallback for T
     */
    public DataBindingListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public void setItemClickListener(ItemClickListener<T> listener) {
        this.listener = listener;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * Since viewType is the layoutId used by the adapter to inflate the layout, any child Adapter
     * needs to override getItemViewType(int position) to return the appropriate layout ID.
     *
     * @param parent   parent
     * @param viewType layoutId used by the adapter
     * @return viewHolder
     */

    @NonNull
    @Override
    public DataBindingViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new DataBindingViewHolder<>(binding, listener, lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public abstract int getItemViewType(int position);


    protected static class DataBindingViewHolder<T> extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        private ItemClickListener<T> listener;
        private LifecycleOwner lifecycleOwner;

        DataBindingViewHolder(@NonNull ViewDataBinding binding, @Nullable ItemClickListener<T> listener,
                              @Nullable LifecycleOwner lifecycleOwner) {
            super(binding.getRoot());
            this.binding = binding;

            if (listener != null) {
                this.listener = listener;
            }

            if (lifecycleOwner != null) {
                this.lifecycleOwner = lifecycleOwner;
            }
        }

        public void bind(T item) {
            binding.setVariable(BR.item, item);

            if (listener != null) {
                binding.setVariable(BR.listener, listener);
            }

            if (lifecycleOwner != null) {
                binding.setLifecycleOwner(lifecycleOwner);
            }

            binding.executePendingBindings();
        }
    }

    public interface ItemClickListener<T> {
        void onItemClick(View view, T item);
    }
}


