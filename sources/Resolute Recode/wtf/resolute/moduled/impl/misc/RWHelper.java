package wtf.resolute.moduled.impl.misc;

import com.google.common.eventbus.Subscribe;
import wtf.resolute.evented.EventPacket;
import wtf.resolute.evented.interfaces.Event;
import wtf.resolute.moduled.Categories;
import wtf.resolute.moduled.Module;
import wtf.resolute.moduled.ModuleAnontion;
import wtf.resolute.moduled.settings.impl.BooleanSetting;
import wtf.resolute.moduled.settings.impl.ModeListSetting;
import wtf.resolute.utiled.math.StopWatch;
import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.UUID;

@ModuleAnontion(name = "RWHelper", type = Categories.Misc,server = "RW")
public class RWHelper extends Module {

    boolean joined;
    StopWatch stopWatch = new StopWatch();

    private final ModeListSetting s = new ModeListSetting("�������",
            new BooleanSetting("����������� ����������� �����", true),
            new BooleanSetting("��������� ����", true),
            new BooleanSetting("���� �����", true),
            new BooleanSetting("�����������", true));

    private UUID uuid;
    int x = -1, z = -1;
    private TrayIcon trayIcon;

    public RWHelper() {
        addSettings(s);
    }

    String[] banWords = new String[]{
            "�����", "���������",
            "������", "�������",
            "������", "������",
            "���������", "���������",
            "�����", "�����",
            "�������", "expa",
            "celka", "nurik",
            "expensive", "celestial",
            "nursultan", "������",
            "funpay", "fluger",
            "������", "akrien",
            "�������", "ft",
            "funtime", "���������",
            "rich", "���",
            "��� ������", "wild",
            "����", "excellent",
            "���������", "hvh",
            "���", "matix",
            "impact", "������",
            "������", "wurst"};

    @Subscribe
    private void onPacket(EventPacket e) {
        if (e.isSend()) {
            if (e.getPacket() instanceof CChatMessagePacket p) {
                boolean contains = false;
                for (String str : banWords) {
                    if (!p.getMessage().toLowerCase().contains(str)) continue;
                    contains = true;
                    break;
                }
                if (contains) {
                    print("RW Helper |" + TextFormatting.RED + " ���������� ����������� ����� � ����� ���������. " +
                            "�������� ��������, ����� �������� ���� �� ReallyWorld.");
                    e.cancel();
                }
            }
        }
        if (e.isReceive()) {
            if (e.getPacket() instanceof SUpdateBossInfoPacket packet) {
                if (s.getValueByName("���� �����").get()) {
                    updateBossInfo(packet);
                }
            }
            if (s.getValueByName("��������� ����").get()) {
                if (e.getPacket() instanceof SRespawnPacket p) {
                    joined = true;
                    stopWatch.reset();
                }
                if (e.getPacket() instanceof SOpenWindowPacket w) {
                    if (w.getTitle().getString().contains("����") && joined && !stopWatch.isReached(2000)) {
                        mc.player.closeScreen();
                        e.cancel();
                        joined = false;
                    }
                }
            }
        }
    }

    public void updateBossInfo(SUpdateBossInfoPacket packet) {
        if (packet.getOperation() == SUpdateBossInfoPacket.Operation.ADD) {
            String name = packet.getName().getString().toLowerCase().replaceAll("\\s+", " ");

            if (name.contains("�������")) {
                parseAirDrop(name);
                uuid = packet.getUniqueId();
            } else if (name.contains("��������")) {
                parseMascot(name);
                uuid = packet.getUniqueId();
            } else if (name.contains("������")) {
                parseScrooge(name);
                uuid = packet.getUniqueId();
            }
        } else if (packet.getOperation() == SUpdateBossInfoPacket.Operation.REMOVE) {
            if (packet.getUniqueId().equals(uuid)) {
                resetCoordinatesAndRemoveWaypoints();
            }
        }
    }

    private void parseAirDrop(String name) {
        x = extractCoordinate(name, "x: ");
        z = extractCoordinate(name, "z: ");
        if (s.getValueByName("�����������").get()) {
            windows("RWHelper", "�������� �������!", false);
        }
        mc.player.sendChatMessage(".gps add ������� " + x + " " + 100 + " " + z);
    }

    private void parseMascot(String name) {
        String[] words = name.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (isInteger(words[i]) && i + 1 < words.length && isInteger(words[i + 1])) {
                x = Integer.parseInt(words[i]);
                z = Integer.parseInt(words[i + 1]);
                if (s.getValueByName("�����������").get()) {
                    windows("RWHelper", "�������� ��������!", false);
                }
                mc.player.sendChatMessage(".gps add �������� " + x + " " + 100 + " " + z);
            }
        }
    }

    private void parseScrooge(String name) {
        int startIndex = name.indexOf("����������");
        if (startIndex == -1) {
            return;
        }
        String coordinatesSubstring = name.substring(startIndex + "����������".length()).trim();

        String[] words = coordinatesSubstring.split("\\s+");

        if (words.length >= 2) {
            x = Integer.parseInt(words[0]);
            z = Integer.parseInt(words[1]);
            if (s.getValueByName("�����������").get()) {
                windows("RWHelper", "�������� ������!", false);
            }
            mc.player.sendChatMessage(".gps add ������ " + x + " " + 100 + " " + z);
        }
    }

    private void resetCoordinatesAndRemoveWaypoints() {
        x = 0;
        z = 0;
        mc.player.sendChatMessage(".gps remove �������");
        mc.player.sendChatMessage(".gps remove ��������");
        mc.player.sendChatMessage(".gps remove ������");
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int extractCoordinate(String text, String coordinateIdentifier) {
        int coordinateStartIndex = text.indexOf(coordinateIdentifier);
        if (coordinateStartIndex != -1) {
            int coordinateValueStart = coordinateStartIndex + coordinateIdentifier.length();
            int coordinateValueEnd = text.indexOf(" ", coordinateValueStart);
            if (coordinateValueEnd == -1) {
                coordinateValueEnd = text.length();
            }
            String coordinateValueString = text.substring(coordinateValueStart, coordinateValueEnd);
            return Integer.parseInt(coordinateValueString.trim());
        }
        return 0;
    }

    private void windows(String name, String desc, boolean error) {
        print(desc);
        if (SystemTray.isSupported()) {
            try {
                if (trayIcon == null) {
                    SystemTray systemTray = SystemTray.getSystemTray();
                    Image image = Toolkit.getDefaultToolkit().createImage("");
                    trayIcon = new TrayIcon(image, "Baritone");
                    trayIcon.setImageAutoSize(true);
                    trayIcon.setToolTip(name);
                    systemTray.add(trayIcon);
                }
                trayIcon.displayMessage(name, desc, error ? TrayIcon.MessageType.ERROR : TrayIcon.MessageType.INFO);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
