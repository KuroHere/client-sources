package wtf.shiyeno.command.impl;

import wtf.shiyeno.command.Command;
import wtf.shiyeno.command.CommandInfo;
import wtf.shiyeno.managment.Managment;
import wtf.shiyeno.util.ClientUtil;

@CommandInfo(name = "panic", description = "��������� ��� ������� ����")

public class PanicCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 1) {
            Managment.FUNCTION_MANAGER.getFunctions().stream().filter(function -> function.state).forEach(function -> function.setState(false));
            ClientUtil.sendMesage("�������� ��� ������!");
        } else error();
    }

    @Override
    public void error() {

    }
}
