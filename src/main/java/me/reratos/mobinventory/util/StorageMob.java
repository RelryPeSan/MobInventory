package me.reratos.mobinventory.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StorageMob {

    private final Inventory storage;

    public StorageMob(LivingEntity entity) {
        this.storage = Bukkit.createInventory(null, 54, entity.getName() + "'s Inventory");

        initStorage();
        setArmorContents(entity.getEquipment());
    }

    private void initStorage() {
        ItemStack separator = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack invNotUse = new ItemStack(Material.BROWN_STAINED_GLASS_PANE);

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

    public Inventory getStorage() {
        return this.storage;
    }

}
