package wtf.shiyeno.command.impl;

import wtf.shiyeno.command.Command;
import wtf.shiyeno.command.CommandInfo;
import wtf.shiyeno.managment.Managment;

@CommandInfo(name = "reload", description = "������������ �������")
public class ReloadCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        Managment.SCRIPT_MANAGER.reload();
        sendMessage("��� ������� �������������.");
    }

    @Override
    public void error() {

    }
}