/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.realms.action;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import net.minecraft.util.text.TranslationTextComponent;

public class StartMinigameRealmsAction
extends LongRunningTask {
    private final long field_238142_c_;
    private final WorldTemplate field_238143_d_;
    private final RealmsConfigureWorldScreen field_238144_e_;

    public StartMinigameRealmsAction(long l, WorldTemplate worldTemplate, RealmsConfigureWorldScreen realmsConfigureWorldScreen) {
        this.field_238142_c_ = l;
        this.field_238143_d_ = worldTemplate;
        this.field_238144_e_ = realmsConfigureWorldScreen;
    }

    @Override
    public void run() {
        RealmsClient realmsClient = RealmsClient.func_224911_a();
        this.func_224989_b(new TranslationTextComponent("mco.minigame.world.starting.screen.title"));
        for (int i = 0; i < 25; ++i) {
            try {
                if (this.func_224988_a()) {
                    return;
                }
                if (!realmsClient.func_224905_d(this.field_238142_c_, this.field_238143_d_.field_230647_a_).booleanValue()) continue;
                StartMinigameRealmsAction.func_238127_a_(this.field_238144_e_);
                break;
            } catch (RetryCallException retryCallException) {
                if (this.func_224988_a()) {
                    return;
                }
                StartMinigameRealmsAction.func_238125_a_(retryCallException.field_224985_e);
                continue;
            } catch (Exception exception) {
                if (this.func_224988_a()) {
                    return;
                }
                field_238124_a_.error("Couldn't start mini game!");
                this.func_237703_a_(exception.toString());
            }
        }
    }
}

