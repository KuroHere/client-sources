package me.r.touchgrass.module.modules.render;

import me.r.touchgrass.module.Category;
import me.r.touchgrass.module.Info;
import me.r.touchgrass.module.Module;
import me.r.touchgrass.settings.Setting;

/**
 * Created by r on 19/01/2022
 */
@Info(name = "Animations", description = "Adds back pre 1.8 animations", category = Category.Render)
public class Animations extends Module {

    public Animations() {
        addSetting(new Setting("BlockHit", this, true)); // done
        addSetting(new Setting("Rod", this, true)); // done
        addSetting(new Setting("Bow", this, true)); // done
        addSetting(new Setting("Third-person Block", this, true)); // done
        addSetting(new Setting("Armor Damage", this, true)); // done
        addSetting(new Setting("Inventory Offset", this, true)); // done
        addSetting(new Setting("Dropped Items", this, true)); // done
        addSetting(new Setting("Tab List", this, true)); // done
    }

}
