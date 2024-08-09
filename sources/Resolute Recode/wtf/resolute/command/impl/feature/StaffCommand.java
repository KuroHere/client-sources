package wtf.resolute.command.impl.feature;


import wtf.resolute.command.*;
import wtf.resolute.command.impl.CommandException;
import wtf.resolute.manage.staffs.StaffStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCommand implements Command, CommandWithAdvice {

    final Prefix prefix;
    final Logger logger;

    @Override
    public void execute(Parameters parameters) {
        String commandType = parameters.asString(0).orElseThrow();
        switch (commandType) {
            case "add" -> addStaffToList(parameters, logger);
            case "remove" -> removeStaffFromList(parameters, logger);
            case "clear" -> clearStaffList(logger);
            case "list" -> getStaffList(logger);
            default -> throw new CommandException(TextFormatting.RED
                    + "������� ��� �������:" + TextFormatting.GRAY + " add, remove, clear, list");
        }
    }

    private void addStaffToList(Parameters parameters, Logger logger) {
        String staffName = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ��� ���������� ��� ����������/��������."));

        if (staffName.equalsIgnoreCase(Minecraft.getInstance().player.getName().getString())) {
            logger.log(TextFormatting.RED + "�� �� ������ �������� ���� � ������ �����������, ��� �� ��� �� ��������");
            return;

        }

        if (StaffStorage.isStaff(staffName)) {
            logger.log(TextFormatting.RED + "���� ����� ��� ��������� � ������.");
            return;
        }

        StaffStorage.add(staffName);
        logger.log(TextFormatting.GRAY + "�� ������� �������� " + TextFormatting.GRAY + staffName + TextFormatting.GRAY + " � ������ �����������!");
    }

    private void removeStaffFromList(final Parameters parameters, Logger logger) {
        String staff = parameters.asString(1)
                .orElseThrow(() -> new CommandException(TextFormatting.RED + "������� ��� ���������� ��� ����������/��������."));
        if (StaffStorage.isStaff(staff)) {
            StaffStorage.remove(staff);
            logger.log(TextFormatting.GRAY + "�� ������� ������� " + TextFormatting.GRAY + staff
                    + TextFormatting.GRAY + " �� ������!");
            return;
        }
        logger.log(TextFormatting.RED + staff + " �� ������ � ������ ������");
    }

    private void getStaffList(Logger logger) {
        if (StaffStorage.getStaffs().isEmpty()) {
            logger.log(TextFormatting.RED + "������ ����������� ������.");
            return;
        }

        logger.log(TextFormatting.GRAY + "������ �����������:");
        for (String friend : StaffStorage.getStaffs()) {
            logger.log(TextFormatting.GRAY + friend);
        }
    }

    private void clearStaffList(Logger logger) {
        if (StaffStorage.getStaffs().isEmpty()) {
            logger.log(TextFormatting.RED + "������ ����������� ������.");
            return;
        }
        StaffStorage.clear();
        logger.log(TextFormatting.GRAY + "������ ����������� ������.");
    }

    @Override
    public String name() {
        return "staff";
    }

    @Override
    public String description() {
        return "��������� ��������� ������� � ������ ���������";
    }

    @Override
    public List<String> adviceMessage() {
        String commandPrefix = prefix.get();
        return List.of(commandPrefix + "staff add <name> - �������� ��� � ������",
                commandPrefix + "staff remove <name> - ������� ��� �� ������",
                commandPrefix + "staff list - �������� ������ ����� ���������",
                commandPrefix + "staff clear - �������� ������ ����� ���������",
                "������: " + TextFormatting.RED + commandPrefix + "staff add Sosiloon"
        );
    }
}