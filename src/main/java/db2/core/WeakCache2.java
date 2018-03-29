package db2.core;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.ibatis.cache.Cache;

import db2.entity.BaseEntity;

public class WeakCache2 implements Cache {

    private final Deque<Object> hardLinksToAvoidGarbageCollection = new LinkedList<Object>();
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries = new ReferenceQueue<Object>();
    private int numberOfHardLinks = 256;

    private Map<Object, Object> cache = new HashMap<Object, Object>();

    private String id;

    public WeakCache2(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getSize() {
        removeGarbageCollectedItems();
        return cache.size();
    }

    public void setSize(int size) {
        this.numberOfHardLinks = size;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value == null)
            return;

        removeGarbageCollectedItems();
        inited(value);
        cache.put(key, new WeakEntry(key, value, queueOfGarbageCollectedEntries));
    }

    private void inited(Object value) {
        if (value instanceof List) {
            List<BaseEntity> entities = (List<BaseEntity>) value;
            for (BaseEntity baseEntity : entities) {
                baseEntity.setInited(true);
            }
        } else {
            BaseEntity entity = (BaseEntity) value;
            entity.setInited(true);
        }
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        @SuppressWarnings("unchecked") // assumed delegate cache is totally
                // managed by this cache
                WeakReference<Object> weakReference = (WeakReference<Object>) cache.get(key);
        if (weakReference != null) {
            result = weakReference.get();
            if (result == null) {
                cache.remove(key);
                // delegate.removeObject(key);
            } else {
                hardLinksToAvoidGarbageCollection.addFirst(result);
                if (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
                    hardLinksToAvoidGarbageCollection.removeLast();
                }
            }
        }
        return result;
    }

    @Override
    public Object removeObject(Object key) {
        removeGarbageCollectedItems();
        return cache.remove(key);
    }

    @Override
    public void clear() {
        hardLinksToAvoidGarbageCollection.clear();
        removeGarbageCollectedItems();
        cache.clear();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private void removeGarbageCollectedItems() {
        WeakEntry sv;
        while ((sv = (WeakEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            cache.remove(sv.key);
        }
    }

    private static class WeakEntry extends WeakReference<Object> {
        private final Object key;

        private WeakEntry(Object key, Object value, ReferenceQueue<Object> garbageCollectionQueue) {
            super(value, garbageCollectionQueue);
            this.key = key;
        }
    }
}
