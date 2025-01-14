package net.minecraft.util.registry;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {
	protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap(256);
	protected final Map<V, K> inverseObjectRegistry;

	public RegistryNamespaced() {
		this.inverseObjectRegistry = ((BiMap) this.registryObjects).inverse();
	}

	public void register(int id, K key, V value) {
		this.underlyingIntegerMap.put(value, id);
		this.putObject(key, value);
	}

	@Override
	protected Map<K, V> createUnderlyingMap() {
		return HashBiMap.<K, V> create();
	}

	@Override
	@Nullable
	public V getObject(@Nullable K name) {
		return super.getObject(name);
	}

	@Nullable

	/**
	 * Gets the name we use to identify the given object.
	 */
	public K getNameForObject(V value) {
		return this.inverseObjectRegistry.get(value);
	}

	/**
	 * Does this registry contain an entry for the given key?
	 */
	@Override
	public boolean containsKey(K key) {
		return super.containsKey(key);
	}

	/**
	 * Gets the integer ID we use to identify the given object.
	 */
	public int getIDForObject(V value) {
		return this.underlyingIntegerMap.getId(value);
	}

	@Nullable

	/**
	 * Gets the object identified by the given ID.
	 */
	public V getObjectById(int id) {
		return this.underlyingIntegerMap.get(id);
	}

	@Override
	public Iterator<V> iterator() {
		return this.underlyingIntegerMap.iterator();
	}
}
