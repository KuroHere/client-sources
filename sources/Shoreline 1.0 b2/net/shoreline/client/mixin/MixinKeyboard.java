package net.shoreline.client.mixin;

import net.shoreline.client.Shoreline;
import net.shoreline.client.impl.event.keyboard.KeyboardInputEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 *
 * @author linus
 * @since 1.0
 *
 * @see Keyboard
 */
@Mixin(Keyboard.class)
public class MixinKeyboard
{
    // @see Keyboard#client
    @Shadow
    @Final
    private MinecraftClient client;

    /**
     *
     *
     * @param window
     * @param key
     * @param scancode
     * @param action
     * @param modifiers
     * @param ci
     */
    @Inject(method = "onKey", at = @At(value = "HEAD"), cancellable = true)
    private void hookOnKey(long window, int key, int scancode, int action,
                           int modifiers, CallbackInfo ci)
    {
        if (client.getWindow().getHandle() == window && action != 0)
        {
            KeyboardInputEvent keyboardInputEvent = new KeyboardInputEvent(key);
            Shoreline.EVENT_HANDLER.dispatch(keyboardInputEvent);
            // prevent keyboard input
            if (keyboardInputEvent.isCanceled())
            {
                ci.cancel();
            }
        }
    }
}