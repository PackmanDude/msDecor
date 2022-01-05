package github.minersStudios.msDecor.enumerators;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public enum CustomDecorMaterial {
    ACACIA_SMALL_CHAIR("Aкациевый стул", 1000, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol(), Badge.COMMON.getBadgeSymbol()},
            true
    ),
    BIRCH_SMALL_CHAIR("Берёзовый стул", 1001, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    CRIMSON_SMALL_CHAIR("Багровый стул", 1002, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    DARK_OAK_SMALL_CHAIR("Стул из тёмного дуба", 1003, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    JUNGLE_SMALL_CHAIR("Тропический стул", 1004, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    OAK_SMALL_CHAIR("Дубовый стул", 1005, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    SPRUCE_SMALL_CHAIR("Еловый стул", 1006, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    WARPED_SMALL_CHAIR("Искажённый стул", 1007, Material.LEATHER_HORSE_ARMOR,
            Sound.BLOCK_WOOD_PLACE, Sound.BLOCK_WOOD_BREAK, 0.5f,
            HitBox.SOLID_FRAME, new String[]{Badge.PAINTABLE.getBadgeSymbol()},
            true
    ),
    ;

    private final String itemName;
    private final int itemCustomModelData;
    private final Material itemMaterial;

    private final Sound placeSound;
    private final Sound breakSound;
    private final float pitch;

    private final HitBox hitBox;
    private final String[] badges;

    private final boolean degreesDefault;

    CustomDecorMaterial(
            String itemName,
            int itemCustomModelData,
            Material itemMaterial,
            Sound placeSound,
            Sound breakSound,
            float pitch,
            HitBox hitBox,
            String[] badges,
            boolean degreesDefault
    ) {
        this.itemName = itemName;
        this.itemCustomModelData = itemCustomModelData;
        this.itemMaterial = itemMaterial;

        this.placeSound = placeSound;
        this.breakSound = breakSound;
        this.pitch = pitch;

        this.hitBox = hitBox;
        this.badges = badges;

        this.degreesDefault = degreesDefault;
    }

    /**
     * @return item name String
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * @return item CMD int
     */
    public int getItemCustomModelData(){
        return itemCustomModelData;
    }

    /**
     * @return item Material
     */
    public Material getItemMaterial(){
        return itemMaterial;
    }

    /**
     * @return custom decor hitbox
     */
    public HitBox getHitBox(){
        return hitBox;
    }

    /**
     * @return custom decor place Sound
     */
    public Sound getPlaceSound(){
        return placeSound;
    }

    /**
     * @return custom decor break Sound
     */
    public Sound getBreakSound(){
        return breakSound;
    }

    /**
     * @return custom decor sound pitch
     */
    public float getPitch(){
        return pitch;
    }

    /**
     * @return True if armor stand degrees = 45
     */
    public boolean isDegreesDefault(){
        return degreesDefault;
    }

    /**
     * @return custom decor ItemStack
     */
    public ItemStack getItemStack(){
        ItemStack itemStack = new ItemStack(itemMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(itemCustomModelData);
        itemMeta.setDisplayName(ChatColor.WHITE + itemName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * @return custom decor material by item in main hand
     */
    public static CustomDecorMaterial getCustomDecorMaterialByItem(@Nonnull ItemStack itemStack){
        assert itemStack.getItemMeta() != null;
        for(CustomDecorMaterial customBlockMaterial : CustomDecorMaterial.values()) {
            if(
                    customBlockMaterial.itemMaterial == itemStack.getType() &&
                    customBlockMaterial.itemCustomModelData == itemStack.getItemMeta().getCustomModelData()
            ){
                return customBlockMaterial;
            }
        }
        return null;
    }

    /**
     * @return custom decor material by item in entity
     */
    public static CustomDecorMaterial getCustomDecorMaterialByEntity(Entity entity){
        for(CustomDecorMaterial customBlockMaterial : CustomDecorMaterial.values()) {
            ItemStack itemStack;
            if(entity instanceof ArmorStand){
                assert ((ArmorStand) entity).getEquipment() != null;

                itemStack = ((ArmorStand) entity).getEquipment().getHelmet();
                assert itemStack != null;
                assert itemStack.getItemMeta() != null;
                if(
                        customBlockMaterial.getItemMaterial() == itemStack.getType() &&
                        customBlockMaterial.getItemCustomModelData() == itemStack.getItemMeta().getCustomModelData()
                ){
                    return customBlockMaterial;
                }
            } else if (entity instanceof ItemFrame){
                assert ((ItemFrame) entity).getItem().getType() != Material.AIR;

                itemStack = ((ItemFrame) entity).getItem();
                assert itemStack.getItemMeta() != null;
                if(
                        customBlockMaterial.getItemMaterial() == itemStack.getType() &&
                        customBlockMaterial.getItemCustomModelData() == itemStack.getItemMeta().getCustomModelData()
                ){
                    return customBlockMaterial;
                }
            }
        }
        return null;
    }
}