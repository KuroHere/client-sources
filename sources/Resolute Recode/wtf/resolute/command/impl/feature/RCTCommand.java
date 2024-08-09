package wtf.resolute.command.impl.feature;


import wtf.resolute.command.Command;
import wtf.resolute.command.Logger;
import wtf.resolute.command.MultiNamedCommand;
import wtf.resolute.command.Parameters;
import wtf.resolute.utiled.client.ClientUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RCTCommand implements Command, MultiNamedCommand {

    final Logger logger;
    final Minecraft mc;

    @Override
    public void execute(Parameters parameters) {
        if (!ClientUtil.isConnectedToServer("funtime")) {
            logger.log("���� RCT �������� ������ �� ������� FunTime");
            return;
        }

        int server = getAnarchyServerNumber();

        if (server == -1) {
            logger.log("�� ������� �������� ����� �������.");
            return;
        }
        mc.player.sendChatMessage("/hub");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mc.player.sendChatMessage("/an" + server);
    }

    private int getAnarchyServerNumber() {
        if (mc.ingameGUI.getTabList().header != null) {
            String serverHeader = TextFormatting.getTextWithoutFormattingCodes(mc.ingameGUI.getTabList().header.getString());
            if (serverHeader != null && serverHeader.contains("�������-")) {
                return Integer.parseInt(serverHeader.split("�������-")[1].trim());
            }
        }
        return -1;
    }

    @Override
    public String name() {
        return "rct";
    }

    @Override
    public String description() {
        return "����������� �� �������";
    }


    @Override
    public List<String> aliases() {
        return Collections.singletonList("reconnect");
    }
}
