/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatStyle {
    private ChatStyle parentStyle;
    private EnumChatFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent chatClickEvent;
    private HoverEvent chatHoverEvent;
    private String insertion;
    private static final ChatStyle rootStyle = new ChatStyle(){
        private static final String __OBFID = "CL_00001267";

        @Override
        public EnumChatFormatting getColor() {
            return null;
        }

        @Override
        public boolean getBold() {
            return false;
        }

        @Override
        public boolean getItalic() {
            return false;
        }

        @Override
        public boolean getStrikethrough() {
            return false;
        }

        @Override
        public boolean getUnderlined() {
            return false;
        }

        @Override
        public boolean getObfuscated() {
            return false;
        }

        @Override
        public ClickEvent getChatClickEvent() {
            return null;
        }

        @Override
        public HoverEvent getChatHoverEvent() {
            return null;
        }

        @Override
        public String getInsertion() {
            return null;
        }

        @Override
        public ChatStyle setColor(EnumChatFormatting color) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setBold(Boolean p_150227_1_) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setItalic(Boolean italic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setStrikethrough(Boolean strikethrough) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setUnderlined(Boolean underlined) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setObfuscated(Boolean obfuscated) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatClickEvent(ClickEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setChatHoverEvent(HoverEvent event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatStyle setParentStyle(ChatStyle parent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Style.ROOT";
        }

        @Override
        public ChatStyle createShallowCopy() {
            return this;
        }

        @Override
        public ChatStyle createDeepCopy() {
            return this;
        }

        @Override
        public String getFormattingCode() {
            return "";
        }
    };
    private static final String __OBFID = "CL_00001266";

    public EnumChatFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    public boolean isEmpty() {
        if (this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.chatClickEvent == null && this.chatHoverEvent == null) {
            return true;
        }
        return false;
    }

    public ClickEvent getChatClickEvent() {
        return this.chatClickEvent == null ? this.getParent().getChatClickEvent() : this.chatClickEvent;
    }

    public HoverEvent getChatHoverEvent() {
        return this.chatHoverEvent == null ? this.getParent().getChatHoverEvent() : this.chatHoverEvent;
    }

    public String getInsertion() {
        return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
    }

    public ChatStyle setColor(EnumChatFormatting color) {
        this.color = color;
        return this;
    }

    public ChatStyle setBold(Boolean p_150227_1_) {
        this.bold = p_150227_1_;
        return this;
    }

    public ChatStyle setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public ChatStyle setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public ChatStyle setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public ChatStyle setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public ChatStyle setChatClickEvent(ClickEvent event) {
        this.chatClickEvent = event;
        return this;
    }

    public ChatStyle setChatHoverEvent(HoverEvent event) {
        this.chatHoverEvent = event;
        return this;
    }

    public ChatStyle setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public ChatStyle setParentStyle(ChatStyle parent) {
        this.parentStyle = parent;
        return this;
    }

    public String getFormattingCode() {
        if (this.isEmpty()) {
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
        }
        StringBuilder var1 = new StringBuilder();
        if (this.getColor() != null) {
            var1.append((Object)this.getColor());
        }
        if (this.getBold()) {
            var1.append((Object)EnumChatFormatting.BOLD);
        }
        if (this.getItalic()) {
            var1.append((Object)EnumChatFormatting.ITALIC);
        }
        if (this.getUnderlined()) {
            var1.append((Object)EnumChatFormatting.UNDERLINE);
        }
        if (this.getObfuscated()) {
            var1.append((Object)EnumChatFormatting.OBFUSCATED);
        }
        if (this.getStrikethrough()) {
            var1.append((Object)EnumChatFormatting.STRIKETHROUGH);
        }
        return var1.toString();
    }

    private ChatStyle getParent() {
        return this.parentStyle == null ? rootStyle : this.parentStyle;
    }

    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + (Object)((Object)this.color) + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getChatClickEvent() + ", hoverEvent=" + this.getChatHoverEvent() + ", insertion=" + this.getInsertion() + '}';
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatStyle)) {
            return false;
        }
        ChatStyle var2 = (ChatStyle)p_equals_1_;
        if (this.getBold() != var2.getBold()) return false;
        if (this.getColor() != var2.getColor()) return false;
        if (this.getItalic() != var2.getItalic()) return false;
        if (this.getObfuscated() != var2.getObfuscated()) return false;
        if (this.getStrikethrough() != var2.getStrikethrough()) return false;
        if (this.getUnderlined() != var2.getUnderlined()) return false;
        if (this.getChatClickEvent() != null) {
            if (!this.getChatClickEvent().equals(var2.getChatClickEvent())) {
                return false;
            }
        } else if (var2.getChatClickEvent() != null) return false;
        if (this.getChatHoverEvent() != null) {
            if (!this.getChatHoverEvent().equals(var2.getChatHoverEvent())) {
                return false;
            }
        } else if (var2.getChatHoverEvent() != null) return false;
        if (this.getInsertion() != null) {
            if (this.getInsertion().equals(var2.getInsertion())) return true;
            return false;
        } else if (var2.getInsertion() == null) return true;
        return false;
    }

    public int hashCode() {
        int var1 = this.color.hashCode();
        var1 = 31 * var1 + this.bold.hashCode();
        var1 = 31 * var1 + this.italic.hashCode();
        var1 = 31 * var1 + this.underlined.hashCode();
        var1 = 31 * var1 + this.strikethrough.hashCode();
        var1 = 31 * var1 + this.obfuscated.hashCode();
        var1 = 31 * var1 + this.chatClickEvent.hashCode();
        var1 = 31 * var1 + this.chatHoverEvent.hashCode();
        var1 = 31 * var1 + this.insertion.hashCode();
        return var1;
    }

    public ChatStyle createShallowCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.bold = this.bold;
        var1.italic = this.italic;
        var1.strikethrough = this.strikethrough;
        var1.underlined = this.underlined;
        var1.obfuscated = this.obfuscated;
        var1.color = this.color;
        var1.chatClickEvent = this.chatClickEvent;
        var1.chatHoverEvent = this.chatHoverEvent;
        var1.parentStyle = this.parentStyle;
        var1.insertion = this.insertion;
        return var1;
    }

    public ChatStyle createDeepCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.setBold(this.getBold());
        var1.setItalic(this.getItalic());
        var1.setStrikethrough(this.getStrikethrough());
        var1.setUnderlined(this.getUnderlined());
        var1.setObfuscated(this.getObfuscated());
        var1.setColor(this.getColor());
        var1.setChatClickEvent(this.getChatClickEvent());
        var1.setChatHoverEvent(this.getChatHoverEvent());
        var1.setInsertion(this.getInsertion());
        return var1;
    }

    static /* synthetic */ void access$0(ChatStyle chatStyle, Boolean bl) {
        chatStyle.bold = bl;
    }

    static /* synthetic */ void access$1(ChatStyle chatStyle, Boolean bl) {
        chatStyle.italic = bl;
    }

    static /* synthetic */ void access$2(ChatStyle chatStyle, Boolean bl) {
        chatStyle.underlined = bl;
    }

    static /* synthetic */ void access$3(ChatStyle chatStyle, Boolean bl) {
        chatStyle.strikethrough = bl;
    }

    static /* synthetic */ void access$4(ChatStyle chatStyle, Boolean bl) {
        chatStyle.obfuscated = bl;
    }

    static /* synthetic */ void access$5(ChatStyle chatStyle, EnumChatFormatting enumChatFormatting) {
        chatStyle.color = enumChatFormatting;
    }

    static /* synthetic */ void access$6(ChatStyle chatStyle, String string) {
        chatStyle.insertion = string;
    }

    static /* synthetic */ void access$7(ChatStyle chatStyle, ClickEvent clickEvent) {
        chatStyle.chatClickEvent = clickEvent;
    }

    static /* synthetic */ void access$8(ChatStyle chatStyle, HoverEvent hoverEvent) {
        chatStyle.chatHoverEvent = hoverEvent;
    }

    public static class Serializer
    implements JsonDeserializer,
    JsonSerializer {
        private static final String __OBFID = "CL_00001268";

        public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            if (p_deserialize_1_.isJsonObject()) {
                JsonObject var6;
                JsonPrimitive var7;
                ChatStyle var4 = new ChatStyle();
                JsonObject var5 = p_deserialize_1_.getAsJsonObject();
                if (var5 == null) {
                    return null;
                }
                if (var5.has("bold")) {
                    ChatStyle.access$0(var4, var5.get("bold").getAsBoolean());
                }
                if (var5.has("italic")) {
                    ChatStyle.access$1(var4, var5.get("italic").getAsBoolean());
                }
                if (var5.has("underlined")) {
                    ChatStyle.access$2(var4, var5.get("underlined").getAsBoolean());
                }
                if (var5.has("strikethrough")) {
                    ChatStyle.access$3(var4, var5.get("strikethrough").getAsBoolean());
                }
                if (var5.has("obfuscated")) {
                    ChatStyle.access$4(var4, var5.get("obfuscated").getAsBoolean());
                }
                if (var5.has("color")) {
                    ChatStyle.access$5(var4, (EnumChatFormatting)((Object)p_deserialize_3_.deserialize(var5.get("color"), EnumChatFormatting.class)));
                }
                if (var5.has("insertion")) {
                    ChatStyle.access$6(var4, var5.get("insertion").getAsString());
                }
                if (var5.has("clickEvent") && (var6 = var5.getAsJsonObject("clickEvent")) != null) {
                    String var10;
                    var7 = var6.getAsJsonPrimitive("action");
                    ClickEvent.Action var8 = var7 == null ? null : ClickEvent.Action.getValueByCanonicalName(var7.getAsString());
                    JsonPrimitive var9 = var6.getAsJsonPrimitive("value");
                    String string = var10 = var9 == null ? null : var9.getAsString();
                    if (var8 != null && var10 != null && var8.shouldAllowInChat()) {
                        ChatStyle.access$7(var4, new ClickEvent(var8, var10));
                    }
                }
                if (var5.has("hoverEvent") && (var6 = var5.getAsJsonObject("hoverEvent")) != null) {
                    var7 = var6.getAsJsonPrimitive("action");
                    HoverEvent.Action var11 = var7 == null ? null : HoverEvent.Action.getValueByCanonicalName(var7.getAsString());
                    IChatComponent var12 = (IChatComponent)p_deserialize_3_.deserialize(var6.get("value"), IChatComponent.class);
                    if (var11 != null && var12 != null && var11.shouldAllowInChat()) {
                        ChatStyle.access$8(var4, new HoverEvent(var11, var12));
                    }
                }
                return var4;
            }
            return null;
        }

        public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            JsonObject var5;
            if (p_serialize_1_.isEmpty()) {
                return null;
            }
            JsonObject var4 = new JsonObject();
            if (p_serialize_1_.bold != null) {
                var4.addProperty("bold", p_serialize_1_.bold);
            }
            if (p_serialize_1_.italic != null) {
                var4.addProperty("italic", p_serialize_1_.italic);
            }
            if (p_serialize_1_.underlined != null) {
                var4.addProperty("underlined", p_serialize_1_.underlined);
            }
            if (p_serialize_1_.strikethrough != null) {
                var4.addProperty("strikethrough", p_serialize_1_.strikethrough);
            }
            if (p_serialize_1_.obfuscated != null) {
                var4.addProperty("obfuscated", p_serialize_1_.obfuscated);
            }
            if (p_serialize_1_.color != null) {
                var4.add("color", p_serialize_3_.serialize((Object)p_serialize_1_.color));
            }
            if (p_serialize_1_.insertion != null) {
                var4.add("insertion", p_serialize_3_.serialize((Object)p_serialize_1_.insertion));
            }
            if (p_serialize_1_.chatClickEvent != null) {
                var5 = new JsonObject();
                var5.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
                var5.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
                var4.add("clickEvent", (JsonElement)var5);
            }
            if (p_serialize_1_.chatHoverEvent != null) {
                var5 = new JsonObject();
                var5.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
                var5.add("value", p_serialize_3_.serialize((Object)p_serialize_1_.chatHoverEvent.getValue()));
                var4.add("hoverEvent", (JsonElement)var5);
            }
            return var4;
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.serialize((ChatStyle)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }

}

