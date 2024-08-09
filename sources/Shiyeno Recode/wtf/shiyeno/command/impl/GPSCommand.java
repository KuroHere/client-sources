package wtf.shiyeno.command.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import wtf.shiyeno.command.Command;
import wtf.shiyeno.command.CommandInfo;
import wtf.shiyeno.managment.Managment;
import wtf.shiyeno.util.ClientUtil;
import wtf.shiyeno.util.font.Fonts;

import java.awt.*;

import static wtf.shiyeno.util.render.RenderUtil.Render2D.drawShadow;
import static wtf.shiyeno.util.render.RenderUtil.Render2D.drawTriangle;

@CommandInfo(name = "gps", description = "������������ ���� �� ���������")
public class GPSCommand extends Command {

    public static boolean enabled;

    public static Vector3d vector3d;

    @Override
    public void run(String[] args) throws Exception {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("off")) {
                ClientUtil.sendMesage(TextFormatting.GRAY + "GPS ��������!");

                enabled = false;
                vector3d = null;
                return;
            }
            if (args.length == 3) {
                int x = Integer.parseInt(args[1]), y = Integer.parseInt(args[2]);
                enabled = true;
                vector3d = new Vector3d(x, 0, y);
                ClientUtil.sendMesage(TextFormatting.GRAY + "GPS �������! ���������� " + x + ";" + y);
            }
        } else {
            error();
        }
    }

    public static void drawArrow(MatrixStack stack) {
        if (!enabled)
            return;

        double x = vector3d.x - mc.getRenderManager().info.getProjectedView().getX();
        double z = vector3d.z - mc.getRenderManager().info.getProjectedView().getZ();

        double cos = MathHelper.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
        double sin = MathHelper.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
        double rotY = -(z * cos - x * sin);
        double rotX = -(x * cos + z * sin);
        double dst = Math.sqrt(Math.pow(vector3d.x - mc.player.getPosX(), 2) + Math.pow(vector3d.z - mc.player.getPosZ(), 2));

        float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
        double x2 = 75 * MathHelper.cos(Math.toRadians(angle)) + mc.getMainWindow().getScaledWidth() / 2f;
        double y2 = 75 * (mc.player.rotationPitch / 90) * MathHelper.sin(Math.toRadians(angle)) + mc.getMainWindow().getScaledHeight() / 2f;

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.translated(x2, y2, 0);
        GlStateManager.rotatef(angle, 0, 0, 1);

        int clr = Managment.STYLE_MANAGER.getCurrentStyle().getColor(100);

        drawShadow(-3F, -3F, 8, 6F, 8, clr);
        drawTriangle(-4, -1F, 4F, 7F, new Color(0, 0, 0, 32));
        drawTriangle(-3F, 0F, 3F, 5F, new Color(clr));
        GlStateManager.rotatef(90, 0, 0, 1);
        Fonts.gilroy[14].drawCenteredStringWithOutline(stack, "GPS", 0, 7, -1);

        Fonts.gilroy[14].drawCenteredStringWithOutline(stack, (int) dst + "m", 0, 15, -1);

        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "������ � �������������" + TextFormatting.WHITE + ":");
        sendMessage(TextFormatting.WHITE + ".gps " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "x, z" + TextFormatting.GRAY + ">");
        sendMessage(TextFormatting.WHITE + ".gps " + TextFormatting.GRAY + "<"
                + TextFormatting.RED + "off" + TextFormatting.GRAY + ">");
    }
}
