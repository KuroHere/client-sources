package wtf.shiyeno.command.impl;

import net.minecraft.util.text.TextFormatting;
import wtf.shiyeno.command.Command;
import wtf.shiyeno.command.CommandInfo;
import wtf.shiyeno.managment.Managment;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.util.ClientUtil;

@CommandInfo(name = "t", description = "��������/��������� ������.")
public class ToggleCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 2) {
            Function func = Managment.FUNCTION_MANAGER.get(args[1]);
            func.setState(!func.isState());

            if (func.isState()) ClientUtil.sendMesage(TextFormatting.GREEN + "������ " + func.name + " �������.");
            else ClientUtil.sendMesage(TextFormatting.RED + "������ " + func.name + " ��������.");
        } else {
            ClientUtil.sendMesage(TextFormatting.RED + "�� ������� �������� �������� ������!");
        }
    }

    @Override
    public void error() {

    }
}
