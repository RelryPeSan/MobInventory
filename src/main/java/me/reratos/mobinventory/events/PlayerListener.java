package me.reratos.mobinventory.events;

import me.reratos.mobinventory.util.StorageMob;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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

            if(player.isSneaking()) {
                ItemStack itemInHandMain = player.getInventory().getItemInMainHand();

                switch (itemInHandMain.getType()) {
                    case CHAINMAIL_HELMET:
                    case DIAMOND_HELMET:
                    case GOLDEN_HELMET:
                    case IRON_HELMET:
                    case LEATHER_HELMET:
                    case NETHERITE_HELMET:
                    case TURTLE_HELMET:
                        entityEquipment.setHelmet(itemInHandMain);
                        break;

                    case CHAINMAIL_CHESTPLATE:
                    case DIAMOND_CHESTPLATE:
                    case GOLDEN_CHESTPLATE:
                    case IRON_CHESTPLATE:
                    case LEATHER_CHESTPLATE:
                    case NETHERITE_CHESTPLATE:
                        entityEquipment.setChestplate(itemInHandMain);
                        break;

                    case CHAINMAIL_LEGGINGS:
                    case DIAMOND_LEGGINGS:
                    case LEATHER_LEGGINGS:
                    case GOLDEN_LEGGINGS:
                    case IRON_LEGGINGS:
                    case NETHERITE_LEGGINGS:
                        entityEquipment.setLeggings(itemInHandMain);
                        break;

                    case CHAINMAIL_BOOTS:
                    case DIAMOND_BOOTS:
                    case GOLDEN_BOOTS:
                    case IRON_BOOTS:
                    case LEATHER_BOOTS:
                    case NETHERITE_BOOTS:
                        entityEquipment.setBoots(itemInHandMain);
                        break;

                    default:
//                    case DIAMOND_AXE:
//                    case DIAMOND_HOE:
//                    case DIAMOND_PICKAXE:
//                    case DIAMOND_SHOVEL:
//                    case DIAMOND_SWORD:
//                    case GOLDEN_SWORD:
//                    case IRON_SWORD:
//                    case NETHERITE_SWORD:
//                    case STONE_SWORD:
//                    case WOODEN_SWORD:
                        entityEquipment.setItemInMainHand(itemInHandMain);
                        entityEquipment.setItemInOffHand(itemInHandMain);
                        break;
                }

                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }

//            if(entityEquipment != null) {
//                ItemStack[] armorContents = entityEquipment.getArmorContents();
//
//                for(int i = 0; i < armorContents.length; i++) {
//                    ItemStack item = armorContents[i];
//                    player.sendMessage(" Item[" + i + "]: " + item.getType().name());
//                }
//            }

            StorageMob storageMob = new StorageMob(livingEntity);

            if(livingEntity instanceof InventoryHolder) {
                InventoryHolder inv = (InventoryHolder) livingEntity;

                Inventory inventory = inv.getInventory();

                int sizeInventory = inventory.getSize();
                ItemStack[] itemStacks = inventory.getStorageContents();

                player.sendMessage("\nmax size inventory: " + sizeInventory);

                for (ItemStack item : itemStacks) {
                    if (item != null) {
                        player.sendMessage(item.getAmount() + "x " + item.getType().name());
                    }
                }

                storageMob.setInventoryMob(itemStacks);
            }

            player.openInventory(storageMob.getStorage());
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {

    }
}
