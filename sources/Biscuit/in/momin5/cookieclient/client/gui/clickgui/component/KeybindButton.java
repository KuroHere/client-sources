package in.momin5.cookieclient.client.gui.clickgui.component;

import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.settings.KeybindComponent;
import com.lukflug.panelstudio.settings.KeybindSetting;
import com.lukflug.panelstudio.theme.Renderer;
import org.lwjgl.input.Keyboard;

public class KeybindButton extends KeybindComponent {
    public KeybindButton (Renderer renderer, KeybindSetting keybind) {
        super(renderer, keybind);
    }

    @Override
    public void handleKey (Context context, int scancode) {
        context.setHeight(renderer.getHeight(false));

        if (hasFocus(context) && (scancode == Keyboard.KEY_DELETE || scancode == Keyboard.KEY_BACK)) {
            keybind.setKey(0);
            releaseFocus();
            return;
        }
        super.handleKey(context, scancode);
    }
}
