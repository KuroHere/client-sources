package io.github.liticane.clients.feature.event.api;

import io.github.liticane.clients.Client;
import io.github.liticane.clients.feature.event.Event;
import io.github.liticane.clients.feature.event.api.annotations.SubscribeEvent;
import io.github.liticane.clients.util.misc.ChatUtil;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.*;

public final class EventManager {

    private final Map<Type, List<CallSite<Event>>> callSiteMap;
    private final Map<Type, List<EventListener<Event>>> listenerCache;

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public EventManager() {
        callSiteMap = new HashMap<>();
        listenerCache = new HashMap<>();
    }

    public void register(final Object subscriber) {
        try {
            for (final Field field : subscriber.getClass().getDeclaredFields()) {
                final SubscribeEvent annotation = field.getAnnotation(SubscribeEvent.class);
                if (annotation != null) {
                    final Type eventType = ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0];

                    if (!field.isAccessible())
                        field.setAccessible(true);
                    try {
                        final EventListener<Event> listener =
                                (EventListener<Event>) LOOKUP.unreflectGetter(field)
                                        .invokeWithArguments(subscriber);

                        final byte priority = annotation.value();

                        final List<CallSite<Event>> callSites;
                        final CallSite<Event> callSite = new CallSite<>(subscriber, listener, priority);

                        if (this.callSiteMap.containsKey(eventType)) {
                            callSites = this.callSiteMap.get(eventType);
                            callSites.add(callSite);
                            callSites.sort((o1, o2) -> o2.priority - o1.priority);
                        } else {
                            callSites = new ArrayList<>(1);
                            callSites.add(callSite);
                            this.callSiteMap.put(eventType, callSites);
                        }
                    } catch (Throwable exception) {
                        if (!Client.DEVELOPMENT_SWITCH) return;
                        ChatUtil.display("Exception in console");
                        exception.printStackTrace();
                    }
                }
            }

            this.populateListenerCache();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void populateListenerCache() {
        final Map<Type, List<CallSite<Event>>> callSiteMap = this.callSiteMap;
        final Map<Type, List<EventListener<Event>>> listenerCache = this.listenerCache;

        for (final Type type : callSiteMap.keySet()) {
            final List<CallSite<Event>> callSites = callSiteMap.get(type);
            final int size = callSites.size();
            final List<EventListener<Event>> listeners = new ArrayList<>(size);

            for (int i = 0; i < size; i++)
                listeners.add(callSites.get(i).listener);

            listenerCache.put(type, listeners);
        }
    }

    public void unregister(final Object subscriber) {
        for (List<CallSite<Event>> callSites : this.callSiteMap.values()) {
            callSites.removeIf(eventCallSite -> eventCallSite.owner == subscriber);
        }

        this.populateListenerCache();
    }

    public void handle(final Event event) {
        final List<EventListener<Event>> listeners = listenerCache.getOrDefault(event.getClass(), Collections.emptyList());

        int i = 0;
        final int listenersSize = listeners.size();

        while (i < listenersSize) {
            listeners.get(i++).call(event);
        }
    }

    public void handle(final Event event, final Object... listeners) {
        int i = 0;
        final int listenersSize = listeners.length;
        final List<Object> list = Arrays.asList(listeners);
        while (i < listenersSize) {
            ((EventListener)list.get(i++)).call(event);
        }
    }

    private static class CallSite<Event> {
        private final Object owner;
        private final EventListener<Event> listener;
        private final byte priority;

        public CallSite(Object owner, EventListener<Event> listener, byte priority) {
            this.owner = owner;
            this.listener = listener;
            this.priority = priority;
        }
    }
}