/*    */ package ru.onlydev.enchant;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*    */ import org.bukkit.event.player.PlayerDropItemEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.EnchantmentStorageMeta;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Main
/*    */   extends JavaPlugin
/*    */   implements Listener
/*    */ {
/* 24 */   private final HashMap<Player, ItemStack> memory = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 29 */     saveDefaultConfig();
/* 30 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
/* 31 */     System.out.println(getConfig().getString("tip").equals(""));
/*    */   }
/*    */   
/*    */   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
/*    */   private void onClick(InventoryClickEvent e) {
/* 36 */     Player p = (Player)e.getWhoClicked();
/*    */     
/* 38 */     if (!getConfig().getBoolean("use-permission"))
/* 39 */       return;  if (!p.hasPermission(getConfig().getString("permission")))
/*    */       return; 
/* 41 */     ItemStack current = e.getCurrentItem();
/*    */ 
/*    */     
/* 44 */     if (!this.memory.containsKey(p)) {
/* 45 */       if (current.getType().equals(Material.ENCHANTED_BOOK)) {
/* 46 */         this.memory.put(p, current);
/* 47 */         sendMessage(p, "tip");
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 53 */     EnchantmentStorageMeta meta = (EnchantmentStorageMeta)((ItemStack)this.memory.get(p)).getItemMeta();
/*    */     try {
/* 55 */       if (current == null)
/* 56 */         return;  current.addEnchantments(meta.getStoredEnchants());
/* 57 */       e.setCancelled(true);
/*    */ 
/*    */       
/* 60 */       Bukkit.getScheduler().runTask((Plugin)this, () -> p.getInventory().setItem(e.getSlot(), current));
/*    */       
/* 62 */       sendMessage(p, "success");
/* 63 */     } catch (IllegalArgumentException ex) {
/* 64 */       sendMessage(p, current.getType().equals(Material.AIR) ? "empty" : "fail");
/*    */     } 
/*    */ 
/*    */     
/* 68 */     this.memory.remove(p);
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   private void onClose(InventoryCloseEvent e) {
/* 74 */     if (e.getPlayer() instanceof Player) this.memory.remove(e.getPlayer()); 
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onDrop(PlayerDropItemEvent e) {
/* 79 */     this.memory.remove(e.getPlayer());
/*    */   }
/*    */   
/*    */   private void sendMessage(Player p, String path) {
/* 83 */     String s = getConfig().getString(path);
/* 84 */     if (s.equals(""))
/* 85 */       return;  p.sendMessage(s.replace('&', '§'));
/*    */   }
/*    */ }
