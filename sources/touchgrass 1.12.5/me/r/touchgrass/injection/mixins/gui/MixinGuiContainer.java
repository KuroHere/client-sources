package me.r.touchgrass.injection.mixins.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Created by r on 19/01/2022
 */
@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends MixinGuiScreen {

    @Shadow protected int xSize;
    @Shadow protected int guiLeft;

}
