package wjhj.orbital.sportsmatchfindingapp.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import wjhj.orbital.sportsmatchfindingapp.BR;

public abstract class DataBindingAdapter<T> extends ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>> {

    private Object controller;

    /**
     * The variable in your databinding layout must be called "item".
     * @param diffCallback diffCallback for T
     */
    public DataBindingAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public DataBindingAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback, Object controller) {
        super(diffCallback);
        this.controller = controller;
    }

    /**
     * Since viewType is the layoutId used by the adapter to inflate the layout, any child Adapter
     * needs to override getItemViewType(int position) to return the appropriate layout ID.
     * @param parent parent
     * @param viewType layoutId used by the adapter
     * @return viewHolder
     */

    @NonNull
    @Override
    public DataBindingViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new DataBindingViewHolder<>(binding, controller);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public abstract int getItemViewType(int position);


    static class DataBindingViewHolder<T> extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        private Object controller;

        DataBindingViewHolder(@NonNull ViewDataBinding binding, @Nullable Object controller) {
            super(binding.getRoot());
            this.binding = binding;
            if (controller != null) {
                this.controller = controller;
            }
        }

        void bind(T item) {
            binding.setVariable(BR.item, item);
        if (controller != null) {
            binding.setVariable(BR.controller, controller);
        }
            binding.executePendingBindings();
        }
    }
}


