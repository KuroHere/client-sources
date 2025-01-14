/*
 * Decompiled with CFR 0.145.
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class S38PacketPlayerListItem
implements Packet {
    private Action field_179770_a;
    private final List field_179769_b = Lists.newArrayList();
    private static final String __OBFID = "CL_00001318";

    public S38PacketPlayerListItem() {
    }

    public S38PacketPlayerListItem(Action p_i45967_1_, EntityPlayerMP ... p_i45967_2_) {
        this.field_179770_a = p_i45967_1_;
        EntityPlayerMP[] var3 = p_i45967_2_;
        int var4 = p_i45967_2_.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            EntityPlayerMP var6 = var3[var5];
            this.field_179769_b.add(new AddPlayerData(var6.getGameProfile(), var6.ping, var6.theItemInWorldManager.getGameType(), var6.func_175396_E()));
        }
    }

    public S38PacketPlayerListItem(Action p_i45968_1_, Iterable p_i45968_2_) {
        this.field_179770_a = p_i45968_1_;
        for (EntityPlayerMP var4 : p_i45968_2_) {
            this.field_179769_b.add(new AddPlayerData(var4.getGameProfile(), var4.ping, var4.theItemInWorldManager.getGameType(), var4.func_175396_E()));
        }
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179770_a = (Action)data.readEnumValue(Action.class);
        int var2 = data.readVarIntFromBuffer();
        for (int var3 = 0; var3 < var2; ++var3) {
            GameProfile var4 = null;
            int var5 = 0;
            WorldSettings.GameType var6 = null;
            IChatComponent var7 = null;
            switch (SwitchAction.field_179938_a[this.field_179770_a.ordinal()]) {
                case 1: {
                    var4 = new GameProfile(data.readUuid(), data.readStringFromBuffer(16));
                    int var8 = data.readVarIntFromBuffer();
                    for (int var9 = 0; var9 < var8; ++var9) {
                        String var10 = data.readStringFromBuffer(32767);
                        String var11 = data.readStringFromBuffer(32767);
                        if (data.readBoolean()) {
                            var4.getProperties().put(var10, new Property(var10, var11, data.readStringFromBuffer(32767)));
                            continue;
                        }
                        var4.getProperties().put(var10, new Property(var10, var11));
                    }
                    var6 = WorldSettings.GameType.getByID(data.readVarIntFromBuffer());
                    var5 = data.readVarIntFromBuffer();
                    if (!data.readBoolean()) break;
                    var7 = data.readChatComponent();
                    break;
                }
                case 2: {
                    var4 = new GameProfile(data.readUuid(), null);
                    var6 = WorldSettings.GameType.getByID(data.readVarIntFromBuffer());
                    break;
                }
                case 3: {
                    var4 = new GameProfile(data.readUuid(), null);
                    var5 = data.readVarIntFromBuffer();
                    break;
                }
                case 4: {
                    var4 = new GameProfile(data.readUuid(), null);
                    if (!data.readBoolean()) break;
                    var7 = data.readChatComponent();
                    break;
                }
                case 5: {
                    var4 = new GameProfile(data.readUuid(), null);
                }
            }
            this.field_179769_b.add(new AddPlayerData(var4, var5, var6, var7));
        }
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeEnumValue(this.field_179770_a);
        data.writeVarIntToBuffer(this.field_179769_b.size());
        for (AddPlayerData var3 : this.field_179769_b) {
            switch (SwitchAction.field_179938_a[this.field_179770_a.ordinal()]) {
                case 1: {
                    data.writeUuid(var3.func_179962_a().getId());
                    data.writeString(var3.func_179962_a().getName());
                    data.writeVarIntToBuffer(var3.func_179962_a().getProperties().size());
                    for (Property var5 : var3.func_179962_a().getProperties().values()) {
                        data.writeString(var5.getName());
                        data.writeString(var5.getValue());
                        if (var5.hasSignature()) {
                            data.writeBoolean(true);
                            data.writeString(var5.getSignature());
                            continue;
                        }
                        data.writeBoolean(false);
                    }
                    data.writeVarIntToBuffer(var3.func_179960_c().getID());
                    data.writeVarIntToBuffer(var3.func_179963_b());
                    if (var3.func_179961_d() == null) {
                        data.writeBoolean(false);
                        break;
                    }
                    data.writeBoolean(true);
                    data.writeChatComponent(var3.func_179961_d());
                    break;
                }
                case 2: {
                    data.writeUuid(var3.func_179962_a().getId());
                    data.writeVarIntToBuffer(var3.func_179960_c().getID());
                    break;
                }
                case 3: {
                    data.writeUuid(var3.func_179962_a().getId());
                    data.writeVarIntToBuffer(var3.func_179963_b());
                    break;
                }
                case 4: {
                    data.writeUuid(var3.func_179962_a().getId());
                    if (var3.func_179961_d() == null) {
                        data.writeBoolean(false);
                        break;
                    }
                    data.writeBoolean(true);
                    data.writeChatComponent(var3.func_179961_d());
                    break;
                }
                case 5: {
                    data.writeUuid(var3.func_179962_a().getId());
                }
            }
        }
    }

    public void func_180743_a(INetHandlerPlayClient p_180743_1_) {
        p_180743_1_.handlePlayerListItem(this);
    }

    public List func_179767_a() {
        return this.field_179769_b;
    }

    public Action func_179768_b() {
        return this.field_179770_a;
    }

    @Override
    public void processPacket(INetHandler handler) {
        this.func_180743_a((INetHandlerPlayClient)handler);
    }

    public static enum Action {
        ADD_PLAYER("ADD_PLAYER", 0),
        UPDATE_GAME_MODE("UPDATE_GAME_MODE", 1),
        UPDATE_LATENCY("UPDATE_LATENCY", 2),
        UPDATE_DISPLAY_NAME("UPDATE_DISPLAY_NAME", 3),
        REMOVE_PLAYER("REMOVE_PLAYER", 4);
        
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00002295";

        static {
            $VALUES = new Action[]{ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER};
        }

        private Action(String p_i45966_1_, int p_i45966_2_) {
        }
    }

    public class AddPlayerData {
        private final int field_179966_b;
        private final WorldSettings.GameType field_179967_c;
        private final GameProfile field_179964_d;
        private final IChatComponent field_179965_e;
        private static final String __OBFID = "CL_00002294";

        public AddPlayerData(GameProfile p_i45965_2_, int p_i45965_3_, WorldSettings.GameType p_i45965_4_, IChatComponent p_i45965_5_) {
            this.field_179964_d = p_i45965_2_;
            this.field_179966_b = p_i45965_3_;
            this.field_179967_c = p_i45965_4_;
            this.field_179965_e = p_i45965_5_;
        }

        public GameProfile func_179962_a() {
            return this.field_179964_d;
        }

        public int func_179963_b() {
            return this.field_179966_b;
        }

        public WorldSettings.GameType func_179960_c() {
            return this.field_179967_c;
        }

        public IChatComponent func_179961_d() {
            return this.field_179965_e;
        }
    }

    static final class SwitchAction {
        static final int[] field_179938_a = new int[Action.values().length];
        private static final String __OBFID = "CL_00002296";

        static {
            try {
                SwitchAction.field_179938_a[Action.ADD_PLAYER.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAction.field_179938_a[Action.UPDATE_GAME_MODE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAction.field_179938_a[Action.UPDATE_LATENCY.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAction.field_179938_a[Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchAction.field_179938_a[Action.REMOVE_PLAYER.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchAction() {
        }
    }

}

