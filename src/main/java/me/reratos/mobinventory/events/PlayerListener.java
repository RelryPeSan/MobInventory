package me.reratos.mobinventory.events;

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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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

        if(entity instanceof LivingEntity) {
//            player.sendMessage("\n ====== PlayerInteractEntityEvent ======");
//            player.sendMessage("Right Click in Entity (" + entity.getName() + ")");
            LivingEntity livingEntity = (LivingEntity) entity;

            EntityEquipment entityEquipment = livingEntity.getEquipment();

            if(player.isSneaking()) {
                ItemStack itemInHandMain = player.getInventory().getItemInMainHand();
                ItemStack itemForDrop;

                assert  entityEquipment != null;
                switch (itemInHandMain.getType()) {
                    case CHAINMAIL_HELMET:
                    case DIAMOND_HELMET:
                    case GOLDEN_HELMET:
                    case IRON_HELMET:
                    case LEATHER_HELMET:
                    case NETHERITE_HELMET:
                    case TURTLE_HELMET:
                        itemForDrop = entityEquipment.getHelmet();
                        entityEquipment.setHelmet(itemInHandMain);
                        break;

                    case CHAINMAIL_CHESTPLATE:
                    case DIAMOND_CHESTPLATE:
                    case GOLDEN_CHESTPLATE:
                    case IRON_CHESTPLATE:
                    case LEATHER_CHESTPLATE:
                    case NETHERITE_CHESTPLATE:
                    case ELYTRA:
                        itemForDrop = entityEquipment.getChestplate();
                        entityEquipment.setChestplate(itemInHandMain);
                        break;

                    case CHAINMAIL_LEGGINGS:
                    case DIAMOND_LEGGINGS:
                    case LEATHER_LEGGINGS:
                    case GOLDEN_LEGGINGS:
                    case IRON_LEGGINGS:
                    case NETHERITE_LEGGINGS:
                        itemForDrop = entityEquipment.getLeggings();
                        entityEquipment.setLeggings(itemInHandMain);
                        break;

                    case CHAINMAIL_BOOTS:
                    case DIAMOND_BOOTS:
                    case GOLDEN_BOOTS:
                    case IRON_BOOTS:
                    case LEATHER_BOOTS:
                    case NETHERITE_BOOTS:
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

//                    int sizeInventory = inventory.getSize();
                    ItemStack[] itemStacks = inventory.getStorageContents();

//                    player.sendMessage("\nmax size inventory: " + sizeInventory);

//                    for (ItemStack item : itemStacks) {
//                        if (item != null) {
//                            player.sendMessage(item.getAmount() + "x " + item.getType().name());
//                        }
//                    }

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

                if(!storageMob.hasStorage()) {
                    event.setCancelled(true);
                    return;
                }

                int slot = event.getSlot();

                // temporariamente cancela evento quando clicado nas armadura ou arma do mob
                if(slot % 9 < 2) {
                    event.setCancelled(true);
                }

                ClickType clickType = event.getClick();

                if(event.isShiftClick() || event.isRightClick() || clickType == ClickType.NUMBER_KEY ||
                        clickType == ClickType.DROP || clickType == ClickType.DOUBLE_CLICK) {
                    sendNotImplemented(player, messageNotImplemented.get(clickType));
                    event.setCancelled(true);
                } else if(event.isLeftClick()) {
                    ItemStack itemStackCursor = event.getCursor() == null ?
                            new ItemStack(Material.AIR) : event.getCursor();

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
            player.sendMessage("Ocorreu um erro: " + e.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        try {
            if(storageMob.getStorage() == inv) {
                Map<Integer, ItemStack> mapItemStack = event.getNewItems();

                sendNotImplemented(player, "Arrastar itens no inventario do mob.");
                event.setCancelled(true);

//                if(mapItemStack.size() > 1) {
//                } else {
//                    setItemStorageMob((Integer) mapItemStack.keySet().toArray()[0],
//                            (ItemStack) mapItemStack.values().toArray()[0]);
//                }
            }
        } catch (RuntimeException e) {
            player.sendMessage("Ocorreu um erro ao arrastar item no inventário: " + e.getMessage());
            event.setCancelled(true);
        }
    }

    private void setItemStorageMob(int slot, ItemStack item) {
        int num = (slot / 9) * 7 + ((slot % 9) - 2);

        if(storageMob.hasStorage()) {
            int maxSizeStorage = storageMob.getMaxSizeStorage();

            if(num >= maxSizeStorage) {
                throw new ArrayIndexOutOfBoundsException("numero de inventario estourado.\nTamanho Maximo: " +
                        maxSizeStorage + "\nTentando acessar index: " + num);
            }
            storageMob.setInventoryMob(num, item);
        }
    }

    private void sendNotImplemented(CommandSender sender, String text) {
        sender.sendMessage(ChatColor.YELLOW + "Esta funcionalidade ainda não foi desenvolvida: " +
                ChatColor.WHITE + text + ChatColor.YELLOW + "\nAguarde atualizações futuras. :)");
    }
}
