package me.reratos.mobinventory.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StorageMob {

    private final Material materialSeparator = Material.GRAY_STAINED_GLASS_PANE;
    private final Material materialInvNotUse = Material.RED_STAINED_GLASS_PANE;

    private final LivingEntity entity;
    private final Inventory storage;

    public StorageMob(LivingEntity entity) {
        this.entity = entity;
        this.storage = Bukkit.createInventory(null, 54, entity.getName() + "'s Inventory");

        initStorage();
        setArmorContents(entity.getEquipment());
    }

    private void initStorage() {
        ItemStack separator = new ItemStack(materialSeparator);
        ItemStack invNotUse = new ItemStack(materialInvNotUse);

        ItemMeta itemMetaSeparator = Bukkit.getItemFactory().getItemMeta(materialSeparator);
        ItemMeta itemMetaInvNotUse = Bukkit.getItemFactory().getItemMeta(materialInvNotUse);
        itemMetaSeparator.setDisplayName(" ");
        itemMetaInvNotUse.setDisplayName(" ");

        separator.setItemMeta(itemMetaSeparator);
        invNotUse.setItemMeta(itemMetaInvNotUse);

        for(int i = 1; i < 54; i ++) {
            this.storage.setItem(i, invNotUse);
        }

        for(int i = 1; i < 54; i += 9) {
            this.storage.setItem(i, separator);
        }

        for(int i = 38; i < 54; i++) {
            this.storage.setItem(i, separator);
        }
    }

    public void setArmorContents(EntityEquipment entityEquipment) {
        this.storage.setItem(0, entityEquipment.getHelmet());
        this.storage.setItem(9, entityEquipment.getChestplate());
        this.storage.setItem(18, entityEquipment.getLeggings());
        this.storage.setItem(27, entityEquipment.getBoots());
        this.storage.setItem(36, entityEquipment.getItemInMainHand());
        this.storage.setItem(45, entityEquipment.getItemInOffHand());
    }

    public void setInventoryMob(ItemStack[] itemStacks) {
        int countInv = 0;
        for(ItemStack item : itemStacks) {
            this.storage.setItem(((countInv % 7) + (2 + (9 * (countInv/7)))), item);
            countInv++;

            if(countInv >= 28) break;
        }
    }

    public void setInventoryMob(int slot, ItemStack itemStack) {
        if(entity instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) entity).getInventory();
            ItemStack itemMob = inv.getItem(slot);

            if(itemMob != null && itemMob.getType() == itemStack.getType()) {
                if(itemMob.getMaxStackSize() != itemMob.getAmount()) {
                    itemMob.setAmount(itemMob.getAmount() + itemStack.getAmount());
                }
            } else {
                inv.setItem(slot, itemStack);
            }
        }
    }

    public Inventory getStorage() {
        return this.storage;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public boolean hasStorage() {
        return entity instanceof InventoryHolder;
    }

    public int getMaxSizeStorage() {
        if(hasStorage()) {
            return ((InventoryHolder) entity).getInventory().getSize();
        } else {
            return -1;
        }
    }
}
