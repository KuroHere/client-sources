/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.util;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentProcessor {
    private static final String __OBFID = "CL_00002310";

    public static IChatComponent func_179985_a(ICommandSender p_179985_0_, IChatComponent p_179985_1_, Entity p_179985_2_) throws CommandException {
        IChatComponent var3 = null;
        if (p_179985_1_ instanceof ChatComponentScore) {
            ChatComponentScore var4 = (ChatComponentScore)p_179985_1_;
            String var5 = var4.func_179995_g();
            if (PlayerSelector.hasArguments(var5)) {
                List var6 = PlayerSelector.func_179656_b(p_179985_0_, var5, Entity.class);
                if (var6.size() != 1) {
                    throw new EntityNotFoundException();
                }
                var5 = ((Entity)var6.get(0)).getName();
            }
            var3 = p_179985_2_ != null && var5.equals("*") ? new ChatComponentScore(p_179985_2_.getName(), var4.func_179994_h()) : new ChatComponentScore(var5, var4.func_179994_h());
            ((ChatComponentScore)var3).func_179997_b(var4.getUnformattedTextForChat());
        } else if (p_179985_1_ instanceof ChatComponentSelector) {
            String var7 = ((ChatComponentSelector)p_179985_1_).func_179992_g();
            var3 = PlayerSelector.func_150869_b(p_179985_0_, var7);
            if (var3 == null) {
                var3 = new ChatComponentText("");
            }
        } else if (p_179985_1_ instanceof ChatComponentText) {
            var3 = new ChatComponentText(((ChatComponentText)p_179985_1_).getChatComponentText_TextValue());
        } else {
            if (!(p_179985_1_ instanceof ChatComponentTranslation)) {
                return p_179985_1_;
            }
            Object[] var8 = ((ChatComponentTranslation)p_179985_1_).getFormatArgs();
            int var10 = 0;
            while (var10 < var8.length) {
                Object var12 = var8[var10];
                if (var12 instanceof IChatComponent) {
                    var8[var10] = ChatComponentProcessor.func_179985_a(p_179985_0_, (IChatComponent)var12, p_179985_2_);
                }
                ++var10;
            }
            var3 = new ChatComponentTranslation(((ChatComponentTranslation)p_179985_1_).getKey(), var8);
        }
        ChatStyle var9 = p_179985_1_.getChatStyle();
        if (var9 != null) {
            var3.setChatStyle(var9.createShallowCopy());
        }
        for (IChatComponent var13 : p_179985_1_.getSiblings()) {
            var3.appendSibling(ChatComponentProcessor.func_179985_a(p_179985_0_, var13, p_179985_2_));
        }
        return var3;
    }
}

