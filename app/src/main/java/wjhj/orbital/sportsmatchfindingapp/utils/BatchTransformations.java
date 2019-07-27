package wjhj.orbital.sportsmatchfindingapp.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java9.util.function.Function;

public class BatchTransformations {

    public static <S, T> LiveData<List<T>> switchMapList(LiveData<List<S>> source, Function<S, LiveData<T>> loader,
                                                   Function<T, String> uniqueMapper) {
        return Transformations.switchMap(source, items -> {
            MediatorLiveData<List<T>> mediatorLiveData = new MediatorLiveData<>();
            HashMap<String, T> uniqueMap = new HashMap<>();
            for (S item : items) {
                mediatorLiveData.addSource(loader.apply(item), loadedItem -> {
                    uniqueMap.put(uniqueMapper.apply(loadedItem), loadedItem);
                    mediatorLiveData.postValue(new ArrayList<>(uniqueMap.values()));
                });
            }
            mediatorLiveData.postValue(new ArrayList<>(uniqueMap.values()));
            return mediatorLiveData;
        });
    }
}
