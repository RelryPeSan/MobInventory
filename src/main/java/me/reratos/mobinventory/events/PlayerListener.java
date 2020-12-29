package me.reratos.mobinventory.events;

import me.reratos.mobinventory.core.ArmorContent;
import me.reratos.mobinventory.enums.ArmorPart;
import me.reratos.mobinventory.util.StorageMob;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    public static StorageMob storageMob;

    public static final Map<ClickType, String> messageNotImplemented = new HashMap<>();

    static {
        messageNotImplemented.put(ClickType.SHIFT_LEFT, "Shift click");
        messageNotImplemented.put(ClickType.SHIFT_RIGHT, "Shift right click");
        messageNotImplemented.put(ClickType.RIGHT, "right click");
        messageNotImplemented.put(ClickType.NUMBER_KEY, "number key");
        messageNotImplemented.put(ClickType.DROP, "drop item");
        messageNotImplemented.put(ClickType.CONTROL_DROP, "drop all items");
        messageNotImplemented.put(ClickType.DOUBLE_CLICK, "double click");
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        EquipmentSlot hand = event.getHand();
//        MainHand mainHand = event.getPlayer().getMainHand();

        // Cancela o evento caso o evento não seja chamado com a mão principal
        if(!hand.equals(EquipmentSlot.HAND)) {
            event.setCancelled(true);
            return;
        }

        if(entity instanceof Player) {
            event.setCancelled(true);
        } else if(entity instanceof LivingEntity) {
//            player.sendMessage("\n ====== PlayerInteractEntityEvent ======");
//            player.sendMessage("Right Click in Entity (" + entity.getName() + ")");
            LivingEntity livingEntity = (LivingEntity) entity;

            EntityEquipment entityEquipment = livingEntity.getEquipment();

            if(player.isSneaking()) {
                ItemStack itemInHandMain = player.getInventory().getItemInMainHand();
                ItemStack itemForDrop;

                assert  entityEquipment != null;
                switch (ArmorPart.getArmorPart(itemInHandMain)) {
                    case HELMET:
                        itemForDrop = entityEquipment.getHelmet();
                        entityEquipment.setHelmet(itemInHandMain);
                        break;

                    case CHESTPLATE:
                        itemForDrop = entityEquipment.getChestplate();
                        entityEquipment.setChestplate(itemInHandMain);
                        break;

                    case LEGGINGS:
                        itemForDrop = entityEquipment.getLeggings();
                        entityEquipment.setLeggings(itemInHandMain);
                        break;

                    case BOOTS:
                        itemForDrop = entityEquipment.getBoots();
                        entityEquipment.setBoots(itemInHandMain);
                        break;

                    default:
                        itemForDrop = entityEquipment.getItemInMainHand();
                        entityEquipment.setItemInMainHand(itemInHandMain);
                        break;
                }

                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }

                // dropa o item se foi trocado
                if(itemForDrop != null && !itemForDrop.getType().equals(Material.AIR)) {
                    entity.getWorld().dropItem(entity.getLocation(), itemForDrop);
                }
            } else {

                StorageMob storageMob = new StorageMob(livingEntity);

                if(livingEntity instanceof InventoryHolder) {
                    InventoryHolder inv = (InventoryHolder) livingEntity;

                    Inventory inventory = inv.getInventory();
                    ItemStack[] itemStacks = inventory.getStorageContents();

                    storageMob.setInventoryMob(itemStacks);
                }

                PlayerListener.storageMob = storageMob;
                player.openInventory(storageMob.getStorage());
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();

        try {

            // inventario do mob teve interação
            if(storageMob.getStorage() == inv) {
                int slot = event.getSlot();
                ItemStack itemStackCursor = event.getCursor() == null ?
                        new ItemStack(Material.AIR) : event.getCursor();

                if(slot % 9 == 0) {
                    boolean ret = false;
                    if(slot / 9 < 4) {
                        ret = !ArmorContent.setArmorContent(storageMob.getEntity().getEquipment(),
                                slot/9, itemStackCursor);
                    } else {
                        ret = !ArmorContent.setHand(storageMob.getEntity().getEquipment(),
                                slot/9 - 4, itemStackCursor);
                    }
                    event.setCancelled(ret);
                    return;
                }

                if(!storageMob.hasStorage()) {
                    event.setCancelled(true);
                    return;
                }

                // cancela evento quando clicado na coluna 2
                if(slot % 9 == 1) {
                    event.setCancelled(true);
                }

                ClickType clickType = event.getClick();

                if(event.isShiftClick() || event.isRightClick() || clickType == ClickType.NUMBER_KEY ||
                        clickType == ClickType.DROP || clickType == ClickType.DOUBLE_CLICK) {
                    sendNotImplemented(player, messageNotImplemented.get(clickType));
                    event.setCancelled(true);
                } else if(event.isLeftClick()) {


//                    player.sendMessage("slotNum: " + slot);

                    setItemStorageMob(slot, itemStackCursor);
                }
            }

            // inventario do mob está aberto, mas não foi neste inventario que o player interagiu
            if(event.getInventory() == storageMob.getStorage()) {
                if(event.isShiftClick()) {
                    sendNotImplemented(player, messageNotImplemented.get(ClickType.SHIFT_LEFT));
                    event.setCancelled(true);
                }
            }

        } catch (RuntimeException e) {
            player.sendMessage("An error has occurred: " + e.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        try {
            Inventory inv = event.getView().getInventory((Integer) event.getNewItems().keySet().toArray()[0]);

            if(storageMob.getStorage() == inv) {
                Map<Integer, ItemStack> mapItemStack = event.getNewItems();

                if(mapItemStack.size() == 1) {
                    int slot = (int) mapItemStack.keySet().toArray()[0];
                    ItemStack itemStackCursor = (ItemStack) mapItemStack.values().toArray()[0];

                    //se a alteração for na area de equipamento
                    if(slot % 9 == 0) {
                        boolean ret = false;

                        if(slot / 9 < 4) {
                            ret = !ArmorContent.setArmorContent(storageMob.getEntity().getEquipment(),
                                    slot/9, itemStackCursor);
                        } else {
                            ret = !ArmorContent.setHand(storageMob.getEntity().getEquipment(),
                                    slot/9 - 4, itemStackCursor);
                        }
                        event.setCancelled(ret);
                        return;
                    } else { //senão, se for na area de inventario
                        setItemStorageMob(slot, itemStackCursor);
                    }

                } else {
                    sendNotImplemented(player, "Drag items in the mob inventory.");
                    event.setCancelled(true);
                }
            }
        } catch (RuntimeException e) {
            player.sendMessage("There was an error dragging item into inventory: " + e.getMessage());
            event.setCancelled(true);
        }
    }

    private void setItemStorageMob(int slot, ItemStack item) {
        int num = (slot / 9) * 7 + ((slot % 9) - 2);

        if(storageMob.hasStorage()) {
            int maxSizeStorage = storageMob.getMaxSizeStorage();

            if(num >= maxSizeStorage) {
                throw new ArrayIndexOutOfBoundsException("Inventory number outside the index.\nMaximum size: " +
                        maxSizeStorage + "\nTrying to access index: " + num);
            }
            storageMob.setInventoryMob(num, item);
        }
    }

    private void sendNotImplemented(CommandSender sender, String text) {
        sender.sendMessage(ChatColor.YELLOW + "This feature has not yet been developed: " +
                ChatColor.WHITE + text + ChatColor.YELLOW + "\nWait for future updates. :)");
    }
}
