package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import io.github.liticane.monoxide.util.FastUUID;

import java.util.Date;
import java.util.UUID;

public class UserListBansEntry extends BanEntry<GameProfile>
{
    public UserListBansEntry(GameProfile profile)
    {
        this(profile, null, null, null, null);
    }

    public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason)
    {
        super(profile, endDate, banner, endDate, banReason);
    }

    public UserListBansEntry(JsonObject p_i1136_1_)
    {
        super(func_152648_b(p_i1136_1_), p_i1136_1_);
    }

    protected void onSerialization(JsonObject data)
    {
        if (this.getValue() != null)
        {
            data.addProperty("uuid", this.getValue().getId() == null ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
        }
    }

    private static GameProfile func_152648_b(JsonObject p_152648_0_)
    {
        if (p_152648_0_.has("uuid") && p_152648_0_.has("name"))
        {
            String s = p_152648_0_.get("uuid").getAsString();
            UUID uuid;

            try
            {
                uuid = FastUUID.parseUUID(s);
            }
            catch (Throwable var4)
            {
                return null;
            }

            return new GameProfile(uuid, p_152648_0_.get("name").getAsString());
        }
        else
        {
            return null;
        }
    }
}
