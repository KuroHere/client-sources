package org.spongepowered.asm.launch.platform;

public interface IMixinPlatformAgent {
  String getPhaseProvider();
  
  void prepare();
  
  void initPrimaryContainer();
  
  void inject();
  
  String getLaunchTarget();
}


/* Location:              C:\Users\Syz\Downloads\Senura (1).jar!\org\spongepowered\asm\launch\platform\IMixinPlatformAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */