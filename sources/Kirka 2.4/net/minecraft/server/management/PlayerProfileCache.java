/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
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
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;

public class PlayerProfileCache {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final Map field_152661_c = Maps.newHashMap();
    private final Map field_152662_d = Maps.newHashMap();
    private final LinkedList field_152663_e = Lists.newLinkedList();
    private final MinecraftServer field_152664_f;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType field_152666_h = new ParameterizedType(){
        private static final String __OBFID = "CL_00001886";

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{ProfileEntry.class};
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
    private static final String __OBFID = "CL_00001888";

    public PlayerProfileCache(MinecraftServer p_i1171_1_, File p_i1171_2_) {
        this.field_152664_f = p_i1171_1_;
        this.usercacheFile = p_i1171_2_;
        GsonBuilder var3 = new GsonBuilder();
        var3.registerTypeHierarchyAdapter(ProfileEntry.class, (Object)new Serializer(null));
        this.gson = var3.create();
        this.func_152657_b();
    }

    private static GameProfile func_152650_a(MinecraftServer p_152650_0_, String p_152650_1_) {
        final GameProfile[] var2 = new GameProfile[1];
        ProfileLookupCallback var3 = new ProfileLookupCallback(){
            private static final String __OBFID = "CL_00001887";

            public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                var2[0] = p_onProfileLookupSucceeded_1_;
            }

            public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                var2[0] = null;
            }
        };
        p_152650_0_.getGameProfileRepository().findProfilesByNames(new String[]{p_152650_1_}, Agent.MINECRAFT, var3);
        if (!p_152650_0_.isServerInOnlineMode() && var2[0] == null) {
            UUID var4 = EntityPlayer.getUUID(new GameProfile(null, p_152650_1_));
            GameProfile var5 = new GameProfile(var4, p_152650_1_);
            var3.onProfileLookupSucceeded(var5);
        }
        return var2[0];
    }

    public void func_152649_a(GameProfile p_152649_1_) {
        this.func_152651_a(p_152649_1_, null);
    }

    private void func_152651_a(GameProfile p_152651_1_, Date p_152651_2_) {
        UUID var3 = p_152651_1_.getId();
        if (p_152651_2_ == null) {
            Calendar var4 = Calendar.getInstance();
            var4.setTime(new Date());
            var4.add(2, 1);
            p_152651_2_ = var4.getTime();
        }
        String var7 = p_152651_1_.getName().toLowerCase(Locale.ROOT);
        ProfileEntry var5 = new ProfileEntry(p_152651_1_, p_152651_2_, null);
        if (this.field_152662_d.containsKey(var3)) {
            ProfileEntry var6 = (ProfileEntry)this.field_152662_d.get(var3);
            this.field_152661_c.remove(var6.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152661_c.put(p_152651_1_.getName().toLowerCase(Locale.ROOT), var5);
            this.field_152663_e.remove((Object)p_152651_1_);
        } else {
            this.field_152662_d.put(var3, var5);
            this.field_152661_c.put(var7, var5);
        }
        this.field_152663_e.addFirst(p_152651_1_);
    }

    public GameProfile getGameProfileForUsername(String p_152655_1_) {
        String var2 = p_152655_1_.toLowerCase(Locale.ROOT);
        ProfileEntry var3 = (ProfileEntry)this.field_152661_c.get(var2);
        if (var3 != null && new Date().getTime() >= var3.field_152673_c.getTime()) {
            this.field_152662_d.remove(var3.func_152668_a().getId());
            this.field_152661_c.remove(var3.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152663_e.remove((Object)var3.func_152668_a());
            var3 = null;
        }
        if (var3 != null) {
            GameProfile var4 = var3.func_152668_a();
            this.field_152663_e.remove((Object)var4);
            this.field_152663_e.addFirst(var4);
        } else {
            GameProfile var4 = PlayerProfileCache.func_152650_a(this.field_152664_f, var2);
            if (var4 != null) {
                this.func_152649_a(var4);
                var3 = (ProfileEntry)this.field_152661_c.get(var2);
            }
        }
        this.func_152658_c();
        return var3 == null ? null : var3.func_152668_a();
    }

    public String[] func_152654_a() {
        ArrayList var1 = Lists.newArrayList(this.field_152661_c.keySet());
        return var1.toArray(new String[var1.size()]);
    }

    public GameProfile func_152652_a(UUID p_152652_1_) {
        ProfileEntry var2 = (ProfileEntry)this.field_152662_d.get(p_152652_1_);
        return var2 == null ? null : var2.func_152668_a();
    }

    private ProfileEntry func_152653_b(UUID p_152653_1_) {
        ProfileEntry var2 = (ProfileEntry)this.field_152662_d.get(p_152653_1_);
        if (var2 != null) {
            GameProfile var3 = var2.func_152668_a();
            this.field_152663_e.remove((Object)var3);
            this.field_152663_e.addFirst(var3);
        }
        return var2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void func_152657_b() {
        var1 = null;
        var2 = null;
        try {
            var2 = Files.newReader((File)this.usercacheFile, (Charset)Charsets.UTF_8);
            var1 = (List)this.gson.fromJson((Reader)var2, (Type)PlayerProfileCache.field_152666_h);
        }
        catch (FileNotFoundException fileNotFoundException) {
            IOUtils.closeQuietly((Reader)var2);
            return;
            catch (Throwable throwable) {
                IOUtils.closeQuietly(var2);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((Reader)var2);
        if (var1 == null) return;
        this.field_152661_c.clear();
        this.field_152662_d.clear();
        this.field_152663_e.clear();
        var1 = Lists.reverse((List)var1);
        var3 = var1.iterator();
        ** GOTO lbl23
lbl-1000: // 1 sources:
        {
            var4 = (ProfileEntry)var3.next();
            if (var4 == null) continue;
            this.func_152651_a(var4.func_152668_a(), var4.func_152670_b());
lbl23: // 3 sources:
            ** while (var3.hasNext())
        }
lbl24: // 1 sources:
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void func_152658_c() {
        String var1 = this.gson.toJson((Object)this.func_152656_a(1000));
        BufferedWriter var2 = null;
        try {
            var2 = Files.newWriter((File)this.usercacheFile, (Charset)Charsets.UTF_8);
            var2.write(var1);
        }
        catch (FileNotFoundException var82222) {
            IOUtils.closeQuietly((Writer)var2);
            return;
            catch (IOException var82222) {
                IOUtils.closeQuietly((Writer)var2);
                return;
                catch (Throwable throwable) {
                    IOUtils.closeQuietly(var2);
                    throw throwable;
                }
            }
        }
        IOUtils.closeQuietly((Writer)var2);
        return;
    }

    private List func_152656_a(int p_152656_1_) {
        ArrayList var2 = Lists.newArrayList();
        ArrayList var3 = Lists.newArrayList((Iterator)Iterators.limit(this.field_152663_e.iterator(), (int)p_152656_1_));
        for (GameProfile var5 : var3) {
            ProfileEntry var6 = this.func_152653_b(var5.getId());
            if (var6 == null) continue;
            var2.add(var6);
        }
        return var2;
    }

    class ProfileEntry {
        private final GameProfile field_152672_b;
        private final Date field_152673_c;
        private static final String __OBFID = "CL_00001885";

        private ProfileEntry(GameProfile p_i46333_2_, Date p_i46333_3_) {
            this.field_152672_b = p_i46333_2_;
            this.field_152673_c = p_i46333_3_;
        }

        public GameProfile func_152668_a() {
            return this.field_152672_b;
        }

        public Date func_152670_b() {
            return this.field_152673_c;
        }

        ProfileEntry(GameProfile p_i1166_2_, Date p_i1166_3_, Object p_i1166_4_) {
            this(p_i1166_2_, p_i1166_3_);
        }
    }

    class Serializer
    implements JsonDeserializer,
    JsonSerializer {
        private static final String __OBFID = "CL_00001884";

        private Serializer() {
        }

        public JsonElement func_152676_a(ProfileEntry p_152676_1_, Type p_152676_2_, JsonSerializationContext p_152676_3_) {
            JsonObject var4 = new JsonObject();
            var4.addProperty("name", p_152676_1_.func_152668_a().getName());
            UUID var5 = p_152676_1_.func_152668_a().getId();
            var4.addProperty("uuid", var5 == null ? "" : var5.toString());
            var4.addProperty("expiresOn", dateFormat.format(p_152676_1_.func_152670_b()));
            return var4;
        }

        public ProfileEntry func_152675_a(JsonElement p_152675_1_, Type p_152675_2_, JsonDeserializationContext p_152675_3_) {
            if (p_152675_1_.isJsonObject()) {
                JsonObject var4 = p_152675_1_.getAsJsonObject();
                JsonElement var5 = var4.get("name");
                JsonElement var6 = var4.get("uuid");
                JsonElement var7 = var4.get("expiresOn");
                if (var5 != null && var6 != null) {
                    String var8 = var6.getAsString();
                    String var9 = var5.getAsString();
                    Date var10 = null;
                    if (var7 != null) {
                        try {
                            var10 = dateFormat.parse(var7.getAsString());
                        }
                        catch (ParseException var14) {
                            var10 = null;
                        }
                    }
                    if (var9 != null && var8 != null) {
                        UUID var11;
                        try {
                            var11 = UUID.fromString(var8);
                        }
                        catch (Throwable var13) {
                            return null;
                        }
                        PlayerProfileCache playerProfileCache = PlayerProfileCache.this;
                        playerProfileCache.getClass();
                        ProfileEntry var12 = playerProfileCache.new ProfileEntry(new GameProfile(var11, var9), var10, null);
                        return var12;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.func_152676_a((ProfileEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            return this.func_152675_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }

        Serializer(Object p_i46332_2_) {
            this();
        }
    }

}

