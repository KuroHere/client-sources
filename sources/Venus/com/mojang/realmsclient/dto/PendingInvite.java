/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PendingInvite
extends ValueObject {
    private static final Logger field_230568_f_ = LogManager.getLogger();
    public String field_230563_a_;
    public String field_230564_b_;
    public String field_230565_c_;
    public String field_230566_d_;
    public Date field_230567_e_;

    public static PendingInvite func_230755_a_(JsonObject jsonObject) {
        PendingInvite pendingInvite = new PendingInvite();
        try {
            pendingInvite.field_230563_a_ = JsonUtils.func_225171_a("invitationId", jsonObject, "");
            pendingInvite.field_230564_b_ = JsonUtils.func_225171_a("worldName", jsonObject, "");
            pendingInvite.field_230565_c_ = JsonUtils.func_225171_a("worldOwnerName", jsonObject, "");
            pendingInvite.field_230566_d_ = JsonUtils.func_225171_a("worldOwnerUuid", jsonObject, "");
            pendingInvite.field_230567_e_ = JsonUtils.func_225173_a("date", jsonObject);
        } catch (Exception exception) {
            field_230568_f_.error("Could not parse PendingInvite: " + exception.getMessage());
        }
        return pendingInvite;
    }
}

