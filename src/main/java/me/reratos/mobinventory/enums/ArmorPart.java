package me.reratos.mobinventory.enums;

import org.bukkit.inventory.ItemStack;

public enum ArmorPart {
    NONE(-1),
    HELMET(0),
    CHESTPLATE(1),
    LEGGINGS(2),
    BOOTS(3);

    private final int valueArmorPart;

    ArmorPart(int armorPart) {
        this.valueArmorPart = armorPart;
    }

    public int getValue() {
        return valueArmorPart;
    }

    public static boolean isHelmet(ItemStack itemStack) {
        if(itemStack == null) {
            throw new RuntimeException("itemStack can't be null");
        }

        switch (itemStack.getType()) {
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLDEN_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
            case NETHERITE_HELMET:
            case TURTLE_HELMET:
                return true;
            default:
                return false;
        }
    }

    public static boolean isChestplate(ItemStack itemStack) {
        if(itemStack == null) {
            throw new RuntimeException("itemStack can't be null");
        }

        switch (itemStack.getType()) {
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLDEN_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
            case NETHERITE_CHESTPLATE:
            case ELYTRA:
                return true;
            default:
                return false;
        }
    }

    public static boolean isLeggings(ItemStack itemStack) {
        if(itemStack == null) {
            throw new RuntimeException("itemStack can't be null");
        }

        switch (itemStack.getType()) {
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLDEN_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
            case NETHERITE_LEGGINGS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBoots(ItemStack itemStack) {
        if(itemStack == null) {
            throw new RuntimeException("itemStack can't be null");
        }

        switch (itemStack.getType()) {
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLDEN_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
            case NETHERITE_BOOTS:
                return true;
            default:
                return false;
        }
    }

    public static ArmorPart getArmorPart(ItemStack itemStack) {
        if(isHelmet(itemStack)) {
            return HELMET;
        } else if(isChestplate(itemStack)) {
            return CHESTPLATE;
        } else if(isLeggings(itemStack)) {
            return LEGGINGS;
        } else if(isBoots(itemStack)) {
            return BOOTS;
        } else {
            return NONE;
        }
    }

    public static ArmorPart getArmorPart(int slot) {
        switch (slot){
            case 0:
                return HELMET;

            case 1:
                return CHESTPLATE;

            case 2:
                return LEGGINGS;

            case 3:
                return BOOTS;

            default:
                return NONE;
        }
    }
}
