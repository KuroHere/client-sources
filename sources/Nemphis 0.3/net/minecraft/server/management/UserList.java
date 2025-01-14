/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.io.Files
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.management.UserListEntry;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList {
    protected static final Logger logger = LogManager.getLogger();
    protected final Gson gson;
    private final File saveFile;
    private final Map values = Maps.newHashMap();
    private boolean lanServer = true;
    private static final ParameterizedType saveFileFormat = new ParameterizedType(){
        private static final String __OBFID = "CL_00001875";

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{UserListEntry.class};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };
    private static final String __OBFID = "CL_00001876";

    public UserList(File saveFile) {
        this.saveFile = saveFile;
        GsonBuilder var2 = new GsonBuilder().setPrettyPrinting();
        var2.registerTypeHierarchyAdapter(UserListEntry.class, (Object)new Serializer(null));
        this.gson = var2.create();
    }

    public boolean isLanServer() {
        return this.lanServer;
    }

    public void setLanServer(boolean state) {
        this.lanServer = state;
    }

    public void addEntry(UserListEntry entry) {
        this.values.put(this.getObjectKey(entry.getValue()), entry);
        try {
            this.writeChanges();
        }
        catch (IOException var3) {
            logger.warn("Could not save the list after adding a user.", (Throwable)var3);
        }
    }

    public UserListEntry getEntry(Object obj) {
        this.removeExpired();
        return (UserListEntry)this.values.get(this.getObjectKey(obj));
    }

    public void removeEntry(Object p_152684_1_) {
        this.values.remove(this.getObjectKey(p_152684_1_));
        try {
            this.writeChanges();
        }
        catch (IOException var3) {
            logger.warn("Could not save the list after removing a user.", (Throwable)var3);
        }
    }

    public String[] getKeys() {
        return this.values.keySet().toArray(new String[this.values.size()]);
    }

    protected String getObjectKey(Object obj) {
        return obj.toString();
    }

    protected boolean hasEntry(Object entry) {
        return this.values.containsKey(this.getObjectKey(entry));
    }

    private void removeExpired() {
        ArrayList var1 = Lists.newArrayList();
        for (UserListEntry var3 : this.values.values()) {
            if (!var3.hasBanExpired()) continue;
            var1.add(var3.getValue());
        }
        for (Object var4 : var1) {
            this.values.remove(var4);
        }
    }

    protected UserListEntry createEntry(JsonObject entryData) {
        return new UserListEntry(null, entryData);
    }

    protected Map getValues() {
        return this.values;
    }

    public void writeChanges() throws IOException {
        Collection var1 = this.values.values();
        String var2 = this.gson.toJson(var1);
        BufferedWriter var3 = null;
        try {
            var3 = Files.newWriter((File)this.saveFile, (Charset)Charsets.UTF_8);
            var3.write(var2);
        }
        finally {
            IOUtils.closeQuietly((Writer)var3);
        }
    }

    class Serializer
    implements JsonDeserializer,
    JsonSerializer {
        private static final String __OBFID = "CL_00001874";

        private Serializer() {
        }

        public JsonElement serializeEntry(UserListEntry p_152751_1_, Type p_152751_2_, JsonSerializationContext p_152751_3_) {
            JsonObject var4 = new JsonObject();
            p_152751_1_.onSerialization(var4);
            return var4;
        }

        public UserListEntry deserializeEntry(JsonElement p_152750_1_, Type p_152750_2_, JsonDeserializationContext p_152750_3_) {
            if (p_152750_1_.isJsonObject()) {
                JsonObject var4 = p_152750_1_.getAsJsonObject();
                UserListEntry var5 = UserList.this.createEntry(var4);
                return var5;
            }
            return null;
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.serializeEntry((UserListEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            return this.deserializeEntry(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }

        Serializer(Object p_i1141_2_) {
            this();
        }
    }

}

