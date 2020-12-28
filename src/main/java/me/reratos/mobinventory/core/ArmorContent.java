package me.reratos.mobinventory.core;

import me.reratos.mobinventory.enums.ArmorPart;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ArmorContent {

    public static boolean setArmorContent(EntityEquipment entityEquipment, int slot, ItemStack itemStack) {

        if(ArmorPart.getArmorPart(itemStack).getValue() != slot && !itemStack.getType().isAir()) {
            return false;
        }

        switch (ArmorPart.getArmorPart(slot)) {
            case HELMET:
                entityEquipment.setHelmet(itemStack);
                break;

            case CHESTPLATE:
                entityEquipment.setChestplate(itemStack);
                break;

            case LEGGINGS:
                entityEquipment.setLeggings(itemStack);
                break;

            case BOOTS:
                entityEquipment.setBoots(itemStack);
                break;

            default:
                return false;
        }

        return true;
    }

    public static boolean setHand(EntityEquipment entityEquipment, int slot, ItemStack itemStack) {
        switch (slot) {
            case 0:
                entityEquipment.setItemInMainHand(itemStack);
                break;

            case 1:
                entityEquipment.setItemInOffHand(itemStack);
                break;

            default:
                return false;
        }
        return true;
    }
}
