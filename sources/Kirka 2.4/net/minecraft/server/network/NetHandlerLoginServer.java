/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.server.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerLoginServer
implements INetHandlerLoginServer,
IUpdatePlayerListBox {
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private final byte[] field_147330_e = new byte[4];
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginState currentLoginState = LoginState.HELLO;
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private String serverId = "";
    private SecretKey secretKey;
    private static final String __OBFID = "CL_00001458";

    public NetHandlerLoginServer(MinecraftServer p_i45298_1_, NetworkManager p_i45298_2_) {
        this.server = p_i45298_1_;
        this.networkManager = p_i45298_2_;
        RANDOM.nextBytes(this.field_147330_e);
    }

    @Override
    public void update() {
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.func_147326_c();
        }
        if (this.connectionTimer++ == 600) {
            this.closeConnection("Took too long to log in");
        }
    }

    public void closeConnection(String reason) {
        try {
            logger.info("Disconnecting " + this.func_147317_d() + ": " + reason);
            ChatComponentText var2 = new ChatComponentText(reason);
            this.networkManager.sendPacket(new S00PacketDisconnect(var2));
            this.networkManager.closeChannel(var2);
        }
        catch (Exception var3) {
            logger.error("Error whilst disconnecting player", (Throwable)var3);
        }
    }

    public void func_147326_c() {
        String var1;
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        if ((var1 = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile)) != null) {
            this.closeConnection(var1);
        } else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), (GenericFutureListener)new ChannelFutureListener(){
                    private static final String __OBFID = "CL_00001459";

                    public void operationComplete(ChannelFuture p_operationComplete_1_) {
                        NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
                    }
                }, new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
            this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
        }
    }

    @Override
    public void onDisconnect(IChatComponent reason) {
        logger.info(String.valueOf(this.func_147317_d()) + " lost connection: " + reason.getUnformattedText());
    }

    public String func_147317_d() {
        return this.loginGameProfile != null ? String.valueOf(this.loginGameProfile.toString()) + " (" + this.networkManager.getRemoteAddress().toString() + ")" : String.valueOf(this.networkManager.getRemoteAddress());
    }

    @Override
    public void processLoginStart(C00PacketLoginStart packetIn) {
        Validate.validState((boolean)(this.currentLoginState == LoginState.HELLO), (String)"Unexpected hello packet", (Object[])new Object[0]);
        this.loginGameProfile = packetIn.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.field_147330_e));
        } else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }

    @Override
    public void processEncryptionResponse(C01PacketEncryptionResponse packetIn) {
        Validate.validState((boolean)(this.currentLoginState == LoginState.KEY), (String)"Unexpected key packet", (Object[])new Object[0]);
        PrivateKey var2 = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.field_147330_e, packetIn.func_149299_b(var2))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = packetIn.func_149300_a(var2);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet()){
            private static final String __OBFID = "CL_00002268";

            @Override
            public void run() {
                GameProfile var1 = NetHandlerLoginServer.this.loginGameProfile;
                try {
                    String var2 = new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
                    NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile(null, var1.getName()), var2));
                    if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    } else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        logger.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.getOfflineProfile(var1));
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    } else {
                        NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
                        logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
                    }
                }
                catch (AuthenticationUnavailableException var3) {
                    if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        logger.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.getOfflineProfile(var1));
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    }
                    NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
                    logger.error("Couldn't verify username because servers are unavailable");
                }
            }
        }.start();
    }

    protected GameProfile getOfflineProfile(GameProfile original) {
        UUID var2 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(var2, original.getName());
    }

    static /* synthetic */ void access$4(NetHandlerLoginServer netHandlerLoginServer, GameProfile gameProfile) {
        netHandlerLoginServer.loginGameProfile = gameProfile;
    }

    static /* synthetic */ void access$6(NetHandlerLoginServer netHandlerLoginServer, LoginState loginState) {
        netHandlerLoginServer.currentLoginState = loginState;
    }

    static enum LoginState {
        HELLO("HELLO", 0),
        KEY("KEY", 1),
        AUTHENTICATING("AUTHENTICATING", 2),
        READY_TO_ACCEPT("READY_TO_ACCEPT", 3),
        ACCEPTED("ACCEPTED", 4);
        
        private static final LoginState[] $VALUES;
        private static final String __OBFID = "CL_00001463";

        static {
            $VALUES = new LoginState[]{HELLO, KEY, AUTHENTICATING, READY_TO_ACCEPT, ACCEPTED};
        }

        private LoginState(String p_i45297_1_, int p_i45297_2_) {
        }
    }

}

