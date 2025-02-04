package me.napoleon.napoline.junk.openapi.script.api;

import me.napoleon.napoline.junk.values.type.Bool;
import me.napoleon.napoline.junk.values.type.NewMode;
import me.napoleon.napoline.junk.values.type.Num;
import me.napoleon.napoline.modules.Mod;
import me.napoleon.napoline.utils.client.ErrorUtil;
import me.napoleon.napoline.utils.player.PlayerUtil;

/**
 * @description: 添加参数
 * @author: QianXia
 * @create: 2020/11/5 14:40
 **/
public class Values {
    private Mod mod;

    public Values(Mod mod) {
        this.mod = mod;
    }

    public Num<Double> addNumberValue(String name, double value, double min, double max) {
        Num<Double> num = new Num<>(name, value, min, max);
        try {
            mod.addValues(num);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.printException(e);
            PlayerUtil.sendMessage("添加参数失败");
        }
        return num;
    }

    public Bool<Boolean> addBooleanValue(String name, boolean state) {
        Bool<Boolean> bool = new Bool<>(name, state);
        try {
            mod.addValues(bool);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.printException(e);
            PlayerUtil.sendMessage("添加参数失败");
        }
        return bool;
    }

    public NewMode addModeValue(String name, String[] values, String value) {
        NewMode mode = new NewMode(name, values, value);
        try {
            mod.addValues(mode);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorUtil.printException(e);
            PlayerUtil.sendMessage("添加参数失败");
        }
        return mode;
    }
}
