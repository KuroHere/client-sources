package wtf.resolute.command.impl.feature;


import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HClipCommand implements Command, CommandWithAdvice {
    final Prefix prefix;
    final Logger logger;
    final Minecraft mc;

    @Override
    public void execute(Parameters parameters) {

        String direction = parameters.asString(0).orElseThrow(() -> new CommandException(TextFormatting.RED + "���������� ������� ����������."));

        if (!NumberUtils.isNumber(direction)) {
            logger.log(TextFormatting.RED + "����������, ������� ����� ��� ���� �������.");
            return;
        }

        double blocks = Double.parseDouble(direction);
        Vector3d lookVector = Minecraft.getInstance().player.getLook(1F).mul(blocks, 0, blocks);

        double newX = mc.player.getPosX() + lookVector.getX();
        double newZ = mc.player.getPosZ() + lookVector.getZ();

        for (int i = 0; i < 5; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(newX, mc.player.getPosY(), newZ, false));
        }

        mc.player.setPositionAndUpdate(newX, mc.player.getPosY(), newZ);

        for (int i = 0; i < 5; i++) {
            mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(newX, mc.player.getPosY(), newZ, false));
        }
        String blockUnit = (blocks > 1) ? "�����" : "����";
        String message = String.format("�� ���� ������� ��������������� �� %.1f %s �� �����������", blocks, blockUnit);
        logger.log(TextFormatting.GRAY + message);
    }

    @Override
    public String name() {
        return "hclip";
    }

    @Override
    public String description() {
        return "������������� �����/����� �� �����������";
    }


    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();
        return List.of(
                TextFormatting.GRAY + commandPrefix + "hclip <distance> - ������������ �� ��������� ����������",
                "������: " + TextFormatting.RED + commandPrefix + "hclip 1"
        );
    }
}