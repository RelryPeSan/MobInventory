package me.reratos.mobinventory.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if(entity instanceof LivingEntity) {
            player.sendMessage("\n============================");
            player.sendMessage("Right Click in Entity (" + entity.getName() + ")");
            LivingEntity livingEntity = (LivingEntity) entity;

            EntityEquipment entityEquipment = livingEntity.getEquipment();

            if(entityEquipment != null) {
                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                entityEquipment.setChestplate(chestplate);

                ItemStack[] armorContents = entityEquipment.getArmorContents();

                for(int i = 0; i < armorContents.length; i++) {
                    ItemStack item = armorContents[i];
                    player.sendMessage(" Item[" + i + "]: " + item.getType().name());
                }
            }

            if(livingEntity instanceof Villager) {
                Villager villager = (Villager) livingEntity;

                Inventory inventory = villager.getInventory();

                int sizeInventory = inventory.getSize();
                ItemStack[] itemStacks = inventory.getStorageContents();

                player.sendMessage("\nmax size inventory: " + sizeInventory);
                player.sendMessage("itens in inventory: " + itemStacks.length);

                for(int i = 0; i < itemStacks.length; i++) {
                    ItemStack item = itemStacks[i];

                    if(item != null) {
                        player.sendMessage(item.getAmount() + "x " + item.getType().name());
                    }
                }
            }


        }
    }
}
