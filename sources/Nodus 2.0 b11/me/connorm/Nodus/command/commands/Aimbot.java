/*  1:   */ package me.connorm.Nodus.command.commands;
/*  2:   */ 
/*  3:   */ import me.connorm.Nodus.Nodus;
/*  4:   */ import me.connorm.Nodus.command.NodusCommand;
/*  5:   */ 
/*  6:   */ public class Aimbot
/*  7:   */   extends NodusCommand
/*  8:   */ {
/*  9:   */   public Aimbot()
/* 10:   */   {
/* 11:11 */     super("aimbot");
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void runCommand(String commandRun, String[] commandArgs)
/* 15:   */   {
/* 16:   */     try
/* 17:   */     {
/* 18:19 */       if (commandArgs[0].equalsIgnoreCase("mode"))
/* 19:   */       {
/* 20:21 */         String aimbotMode = commandArgs[1];
/* 21:22 */         if (aimbotMode.equalsIgnoreCase("both"))
/* 22:   */         {
/* 23:24 */           me.connorm.Nodus.module.modules.utils.AimbotUtils.aimbotMode = 0;
/* 24:25 */           Nodus.theNodus.addChatMessage("Aimbot mode set to both.");
/* 25:   */         }
/* 26:27 */         if (aimbotMode.equalsIgnoreCase("player"))
/* 27:   */         {
/* 28:29 */           me.connorm.Nodus.module.modules.utils.AimbotUtils.aimbotMode = 1;
/* 29:30 */           Nodus.theNodus.addChatMessage("Aimbot mode set to player.");
/* 30:   */         }
/* 31:32 */         if (aimbotMode.equalsIgnoreCase("mob"))
/* 32:   */         {
/* 33:34 */           me.connorm.Nodus.module.modules.utils.AimbotUtils.aimbotMode = 2;
/* 34:35 */           Nodus.theNodus.addChatMessage("Aimbot mode set to mob.");
/* 35:   */         }
/* 36:   */       }
/* 37:   */     }
/* 38:   */     catch (Exception commandSyntaxException)
/* 39:   */     {
/* 40:40 */       commandSyntaxException.printStackTrace();
/* 41:41 */       Nodus.theNodus.addChatMessage("Command Usage: " + getSyntax());
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   public String getDescription()
/* 46:   */   {
/* 47:48 */     return "Sets aimbot mode";
/* 48:   */   }
/* 49:   */   
/* 50:   */   public String getSyntax()
/* 51:   */   {
/* 52:54 */     return "mode <player, mob, both>";
/* 53:   */   }
/* 54:   */   
/* 55:   */   public String getConsoleDisplay()
/* 56:   */   {
/* 57:60 */     return "aimbot";
/* 58:   */   }
/* 59:   */ }


/* Location:           C:\Users\D\AppData\Roaming\.minecraft\versions\Nodus_2.0-1.7.x\Nodus_2.0-1.7.x.jar
 * Qualified Name:     me.connorm.Nodus.command.commands.Aimbot
 * JD-Core Version:    0.7.0.1
 */