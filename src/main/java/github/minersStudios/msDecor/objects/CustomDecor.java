package github.minersStudios.msDecor.objects;

import github.minersStudios.msDecor.Main;
import github.minersStudios.msDecor.enums.CustomDecorMaterial;
import github.minersStudios.msDecor.enums.HitBox;
import github.minersStudios.msDecor.utils.PlaySwingAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static github.minersStudios.msDecor.Main.coreProtectAPI;

/**
 * Custom decor Object
 */
@Nonnull
public class CustomDecor {
    private final Block block;
    private final Player player;
    private static ItemStack itemInMainHand;
    private CustomDecorMaterial customDecorMaterial;

    /**
     * @param block block at face
     * @param player player who places/breaks custom decor
     */
    public CustomDecor(@Nonnull Block block, @Nullable Player player){
        this.block = block;
        this.player = player;
    }

    /**
     * Sets custom decor
     *
     * @param customDecorMaterial custom decor that will be placed
     * @param blockFace block face on which the frame is to be spawned
     */
    public void setCustomDecor(CustomDecorMaterial customDecorMaterial, BlockFace blockFace) {
        assert player != null;
        itemInMainHand = player.getInventory().getItemInMainHand();
        this.customDecorMaterial = customDecorMaterial;
        if(customDecorMaterial.getHitBox().isArmorStand()) summonArmorStand();
        else summonItemFrame(blockFace);
        setHitBox();
        new PlaySwingAnimation(player, EquipmentSlot.HAND);
        block.getWorld().playSound(block.getLocation(), customDecorMaterial.getPlaceSound(), 1.0f, customDecorMaterial.getPitch());
        itemInMainHand.setAmount(player.getGameMode() == GameMode.SURVIVAL ? itemInMainHand.getAmount() - 1 : itemInMainHand.getAmount());
        coreProtectAPI.logPlacement(player.getName(), block.getLocation(), Material.VOID_AIR, block.getBlockData());
    }

    /**
     * Breaks custom block vanillish
     */
    public void breakCustomDecor(){
        Location blockLocation = block.getLocation();
        World world = block.getWorld();
        for (Entity nearbyEntity : block.getWorld().getNearbyEntities(blockLocation.add(0.5d, 0.5d, 0.5d), 0.5d, 0.5d, 0.5d)){
            assert nearbyEntity != null;
            blockLocation.add(-0.5d, -0.5d, -0.5d);
            if(nearbyEntity instanceof ItemFrame) {
                assert ((ItemFrame) nearbyEntity).getItem().getItemMeta() != null;
                customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(nearbyEntity);
                ItemStack itemStack = ((ItemFrame) nearbyEntity).getItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName(nearbyEntity.getName());
                itemStack.setItemMeta(itemMeta);
                world.dropItemNaturally(blockLocation, itemStack);
                nearbyEntity.remove();
            }
        }
        for (Entity nearbyEntity : block.getWorld().getNearbyEntities(blockLocation.add(0.5d, 0.0d, 0.5d), 0.2d, 0.3d, 0.2d)){
            assert nearbyEntity != null;
            blockLocation.add(-0.5d, -0.0d, -0.5d);
            if(nearbyEntity instanceof ArmorStand) {
                assert ((ArmorStand) nearbyEntity).getEquipment() != null;
                assert ((ArmorStand) nearbyEntity).getEquipment().getHelmet() != null;
                assert ((ArmorStand) nearbyEntity).getEquipment().getHelmet().getItemMeta() != null;
                customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(nearbyEntity);
                world.dropItemNaturally(blockLocation, ((ArmorStand) nearbyEntity).getEquipment().getHelmet());
                nearbyEntity.remove();
            }
        }
        world.playSound(blockLocation, customDecorMaterial.getBreakSound(), 1.0f, customDecorMaterial.getPitch());
        block.setType(Material.AIR);
        coreProtectAPI.logRemoval(player != null ? player.getName() : "Неизвестно", block.getLocation(), Material.VOID_AIR, block.getBlockData());
    }

    /**
     * Breaks custom block vanillish
     */
    public void breakCustomDecorEntity(Entity entity) {
        Location blockLocation = block.getLocation();
        World world = block.getWorld();
        if (entity instanceof ItemFrame) {
            assert ((ItemFrame) entity).getItem().getItemMeta() != null;
            customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(entity);
            ItemStack itemStack = ((ItemFrame) entity).getItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(entity.getName());
            itemStack.setItemMeta(itemMeta);
            world.dropItemNaturally(blockLocation, itemStack);
        } else if (entity instanceof ArmorStand) {
            assert ((ArmorStand) entity).getEquipment() != null;
            assert ((ArmorStand) entity).getEquipment().getHelmet() != null;
            assert ((ArmorStand) entity).getEquipment().getHelmet().getItemMeta() != null;
            customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(entity);
            world.dropItemNaturally(blockLocation, ((ArmorStand) entity).getEquipment().getHelmet());
        }

        world.playSound(blockLocation, customDecorMaterial.getBreakSound(), 1.0f, customDecorMaterial.getPitch());
        block.setType(Material.AIR);
        entity.remove();
        coreProtectAPI.logRemoval(player != null ? player.getName() : "Неизвестно", block.getLocation(), Material.VOID_AIR, block.getBlockData());
    }

    /**
     * Summons armor stand with custom decor item like hat
     */
    private void summonArmorStand(){
        assert player != null;
        block.getWorld().spawn(block.getLocation().add(0.5d, 0.0d, 0.5d), ArmorStand.class, (armorStand) -> {
            assert armorStand.getEquipment() != null;
            armorStand.setGravity(false);
            armorStand.setMarker(customDecorMaterial.getHitBox().isSolidHitBox());
            armorStand.setSmall(customDecorMaterial.getHitBox() == HitBox.SMALL_ARMOR_STAND || customDecorMaterial.getHitBox() == HitBox.SOLID_SMALL_ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setCollidable(false);
            armorStand.addScoreboardTag("customDecor");

            ItemStack itemStack = itemInMainHand.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
            itemStack.setItemMeta(itemMeta);
            armorStand.getEquipment().setHelmet(itemStack);

            Location location = armorStand.getLocation();
            if (player.getLocation().getYaw() >= 25 && player.getLocation().getYaw() <= 64 && player.getLocation().getYaw() != 45) location.setYaw(45);
            if (player.getLocation().getYaw() >= 65 && player.getLocation().getYaw() <= 119 && player.getLocation().getYaw() != 90) location.setYaw(90);
            if (player.getLocation().getYaw() >= 120 && player.getLocation().getYaw() <= 139 && player.getLocation().getYaw() != 135) location.setYaw(135);
            if (player.getLocation().getYaw() >= 140 && player.getLocation().getYaw() <= 180 && player.getLocation().getYaw() != 180) location.setYaw(180);
            if (player.getLocation().getYaw() >= -26 && player.getLocation().getYaw() <= 24 && player.getLocation().getYaw() != 0 && player.getLocation().getYaw() != -180) location.setYaw(0);
            if (player.getLocation().getYaw() <= -25 && player.getLocation().getYaw() >= -64 && player.getLocation().getYaw() != -45) location.setYaw(-45);
            if (player.getLocation().getYaw() <= -65 && player.getLocation().getYaw() >= -119 && player.getLocation().getYaw() != -90) location.setYaw(-90);
            if (player.getLocation().getYaw() <= -120 && player.getLocation().getYaw() >= -150 && player.getLocation().getYaw() != -135) location.setYaw(-135);
            if (player.getLocation().getYaw() <= -151 && player.getLocation().getYaw() >= -179 && player.getLocation().getYaw() != -180 && player.getLocation().getYaw() != 0) location.setYaw(-180);
            armorStand.teleport(location);
        });
    }

    /**
     * Summons item frame with custom decor item
     *
     * @param blockFace block face on which the frame is to be spawned
     */
    private void summonItemFrame(BlockFace blockFace){
        assert itemInMainHand.getItemMeta() != null;
        assert player != null;
        block.getWorld().spawn(block.getLocation().add(0.5d, 0.0d, 0.5d), ItemFrame.class, (itemFrame) -> {
            itemFrame.setItemDropChance(0.0f);
            itemFrame.setCustomName(itemInMainHand.getItemMeta().getDisplayName());
            itemFrame.setVisible(false);
            itemFrame.setFixed(customDecorMaterial.getHitBox() != HitBox.FRAME);
            itemFrame.setRotation(Rotation.CLOCKWISE);
            itemFrame.setFacingDirection(blockFace);
            itemFrame.addScoreboardTag("customDecor");

            ItemStack itemStack = itemInMainHand.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(null);
            itemStack.setItemMeta(itemMeta);
            itemFrame.setItem(itemStack);
        });
    }

    /**
     * Sets custom decor hitbox
     */
    private void setHitBox(){
        new BukkitRunnable(){
            @Override
            public void run() {
                block.setType(
                        customDecorMaterial.getHitBox().isStructureHitBox() ? Material.STRUCTURE_VOID
                                : customDecorMaterial.getHitBox().isSolidHitBox() ? Material.BARRIER
                                : !customDecorMaterial.getHitBox().isArmorStand() ? Material.LIGHT
                                : Material.AIR
                );
            }
        }.runTask(Main.plugin);
    }
}
