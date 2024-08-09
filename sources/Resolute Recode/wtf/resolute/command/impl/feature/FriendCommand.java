package wtf.resolute.command.impl.feature;

import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import wtf.resolute.manage.friends.FriendStorage;
import wtf.resolute.utiled.player.PlayerUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendCommand implements Command, CommandWithAdvice {

    final Prefix prefix;
    final Logger logger;
    final Minecraft mc;

    @Override
    public void execute(Parameters parameters) {
        String commandType = parameters.asString(0).orElseThrow();
        switch (commandType) {
            case "add" -> addFriend(parameters, logger);
            case "remove" -> removeFriend(parameters, logger);
            case "clear" -> clearFriendList(logger);
            case "list" -> getFriendList(logger);
            default -> throw new CommandException(TextFormatting.RED
                    + "������� ��� �������:" + TextFormatting.GRAY + " add, remove, clear, list");
        }
    }

    @Override
    public String name() {
        return "friend";
    }

    @Override
    public String description() {
        return "��������� ��������� ������� ������";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();
        return List.of(commandPrefix + "friend add <name> - �������� ����� �� �����",
                commandPrefix + "friend remove <name> - ������� ����� �� �����",
                commandPrefix + "friend list - �������� ������ ������",
                commandPrefix + "friend clear - �������� ������ ������",
                "������: " + TextFormatting.RED + commandPrefix + "friend add wermitist"
        );
    }

    private void addFriend(final Parameters parameters, Logger logger) {
        String friendName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ��� ����� ��� ����������/��������."));

        if (!PlayerUtils.isNameValid(friendName)) {
            logger.log(TextFormatting.RED + "������������ ���.");
            return;
        }

        if (friendName.equalsIgnoreCase(mc.player.getName().getString())) {
            logger.log(TextFormatting.RED + "�� �� ������ �������� ���� � ������, ��� �� ��� �� ��������");
            return;

        }

        if (FriendStorage.isFriend(friendName)) {
            logger.log(TextFormatting.RED + "���� ����� ��� ��������� � ����� ������ ������.");
            return;
        }
        FriendStorage.add(friendName);
        logger.log(TextFormatting.GRAY + "�� ������� �������� " + TextFormatting.GRAY + friendName + TextFormatting.GRAY + " � ������!");
    }

    private void removeFriend(final Parameters parameters, Logger logger) {
        String friendName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ��� ����� ��� ����������/��������."));
        if (FriendStorage.isFriend(friendName)) {
            FriendStorage.remove(friendName);
            logger.log(TextFormatting.GRAY + "�� ������� ������� " + TextFormatting.GRAY + friendName
                    + TextFormatting.GRAY + " �� ������!");
            return;
        }
        logger.log(TextFormatting.RED + friendName + " �� ������ � ������ ������");
    }

    private void getFriendList(Logger logger) {
        if (FriendStorage.getFriends().isEmpty()) {
            logger.log(TextFormatting.RED + "������ ������ ������.");
            return;
        }

        logger.log(TextFormatting.GRAY + "������ ������:");
        for (String friend : FriendStorage.getFriends()) {
            logger.log(TextFormatting.GRAY + friend);
        }
    }

    private void clearFriendList(Logger logger) {
        if (FriendStorage.getFriends().isEmpty()) {
            logger.log(TextFormatting.RED + "������ ������ ������.");
            return;
        }
        FriendStorage.clear();
        logger.log(TextFormatting.GRAY + "������ ������ ������.");
    }
}
