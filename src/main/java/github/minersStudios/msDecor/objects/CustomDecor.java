package github.minersStudios.msDecor.objects;

import github.minersStudios.msDecor.Main;
import github.minersStudios.msDecor.enums.CustomDecorFacing;
import github.minersStudios.msDecor.enums.CustomDecorMaterial;
import github.minersStudios.msDecor.enums.HitBox;
import github.minersStudios.msDecor.utils.EntityUtils;
import github.minersStudios.msDecor.utils.PlaySwingAnimation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
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
     * @param player player who places/breaks custom decor, if player == null the log will not record the player's nickname
     */
    public CustomDecor(@Nonnull Block block, @Nullable Player player) {
        this.block = block;
        this.player = player;
    }

    /**
     * Sets custom decor
     *
     * @param customDecorMaterial custom decor that will be placed
     * @param blockFace block face on which the frame is to be spawned
     */
    public void setCustomDecor(@Nonnull CustomDecorMaterial customDecorMaterial, @Nonnull BlockFace blockFace) {
        assert player != null;
        itemInMainHand = player.getInventory().getItemInMainHand();
        this.customDecorMaterial = customDecorMaterial;
        if (customDecorMaterial.getHitBox().isArmorStand()) summonArmorStand();
        else summonItemFrame(blockFace);
        setHitBox();
        new PlaySwingAnimation(player, EquipmentSlot.HAND);
        if (customDecorMaterial.getPlaceSound() != null)
            block.getWorld().playSound(block.getLocation(), customDecorMaterial.getPlaceSound(), 1.0f, customDecorMaterial.getPitch());
        itemInMainHand.setAmount(player.getGameMode() == GameMode.SURVIVAL ? itemInMainHand.getAmount() - 1 : itemInMainHand.getAmount());
        coreProtectAPI.logPlacement(player.getName(), block.getLocation(), Material.VOID_AIR, block.getBlockData());
    }

    /**
     * Breaks custom block vanillish
     */
    public void breakCustomDecor() {
        assert player != null;
        Location blockLocation = block.getLocation();
        World world = block.getWorld();
        for (Entity nearbyEntity : block.getWorld().getNearbyEntities(blockLocation.clone().add(0.5d, 0.5d, 0.5d), 0.5d, 0.5d, 0.5d)) {
            if (nearbyEntity instanceof ItemFrame && ((ItemFrame) nearbyEntity).getItem().getItemMeta() != null) {
                customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(nearbyEntity, true);
                if(customDecorMaterial == null) return;
                block.setType(Material.AIR);
                if (customDecorMaterial.getBreakSound() != null)
                    world.playSound(blockLocation, customDecorMaterial.getBreakSound(), 1.0f, customDecorMaterial.getPitch());
                coreProtectAPI.logRemoval(player.getName(), block.getLocation(), Material.VOID_AIR, block.getBlockData());
                nearbyEntity.remove();
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    ItemStack itemStack = ((ItemFrame) nearbyEntity).getItem();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(nearbyEntity.getName());
                    itemMeta.setCustomModelData(customDecorMaterial.getItemCustomModelData());
                    itemStack.setItemMeta(itemMeta);
                    world.dropItemNaturally(blockLocation, itemStack);
                }
                return;
            }
        }
        for (Entity nearbyEntity : block.getWorld().getNearbyEntities(blockLocation.clone().add(0.5d, 0.0d, 0.5d), 0.2d, 0.3d, 0.2d)) {
            if (nearbyEntity instanceof ArmorStand && ((ArmorStand) nearbyEntity).getEquipment() != null && ((ArmorStand) nearbyEntity).getEquipment().getHelmet() != null) {
                customDecorMaterial = CustomDecorMaterial.getCustomDecorMaterialByEntity(nearbyEntity, true);
                if(customDecorMaterial == null) return;
                block.setType(Material.AIR);
                if (customDecorMaterial.getBreakSound() != null)
                    world.playSound(blockLocation, customDecorMaterial.getBreakSound(), 1.0f, customDecorMaterial.getPitch());
                coreProtectAPI.logRemoval(player.getName(), block.getLocation(), Material.VOID_AIR, block.getBlockData());
                nearbyEntity.remove();
                if (player.getGameMode() == GameMode.SURVIVAL) {
                    ItemStack itemStack = ((ArmorStand) nearbyEntity).getEquipment().getHelmet();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    assert itemMeta != null;
                    itemMeta.setCustomModelData(customDecorMaterial.getItemCustomModelData());
                    itemStack.setItemMeta(itemMeta);
                    world.dropItemNaturally(blockLocation, itemStack);
                }
            }
        }
    }

    /**
     * Summons armor stand with custom decor item like hat
     */
    private void summonArmorStand() {
        assert player != null;
        block.getWorld().spawn(block.getLocation().add(0.5d, 0.0d, 0.5d), ArmorStand.class, (armorStand) -> {
            assert armorStand.getEquipment() != null;
            armorStand.setGravity(false);
            armorStand.setMarker(customDecorMaterial.getHitBox().isSolidHitBox() || customDecorMaterial.getHitBox().isStructureHitBox());
            armorStand.setSmall(customDecorMaterial.getHitBox() == HitBox.SMALL_ARMOR_STAND || customDecorMaterial.getHitBox() == HitBox.SOLID_SMALL_ARMOR_STAND || customDecorMaterial.getHitBox() == HitBox.STRUCTURE_SMALL_ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setCollidable(false);
            armorStand.addScoreboardTag("customDecor");

            ItemStack itemStack = itemInMainHand.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
            itemStack.setItemMeta(itemMeta);
            armorStand.getEquipment().setHelmet(itemStack);
            new EntityUtils().rotateArmorStandByPlayer(armorStand, player);
        });
    }

    /**
     * Summons item frame with custom decor item
     *
     * @param blockFace block face on which the frame is to be spawned
     */
    private void summonItemFrame(BlockFace blockFace) {
        assert itemInMainHand.getItemMeta() != null;
        assert player != null;
        block.getWorld().spawn(block.getLocation().add(0.5d, 0.0d, 0.5d), ItemFrame.class, (itemFrame) -> {
            itemFrame.setItemDropChance(0.0f);
            itemFrame.setCustomName(itemInMainHand.getItemMeta().getDisplayName());
            itemFrame.setVisible(false);
            itemFrame.setSilent(true);
            itemFrame.setFixed(customDecorMaterial.getHitBox() != HitBox.FRAME);
            itemFrame.setFacingDirection(blockFace);
            itemFrame.addScoreboardTag("customDecor");

            ItemStack itemStack = itemInMainHand.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(null);
            itemStack.setItemMeta(itemMeta);
            itemFrame.setItem(itemStack);

            if (customDecorMaterial.getFacing() != CustomDecorFacing.WALL)
                new EntityUtils().rotateItemFrameByPlayer(itemFrame, player);
        });
    }

    /**
     * Sets custom decor hitbox
     */
    private void setHitBox() {
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(
                        customDecorMaterial.getHitBox().isStructureHitBox() ? Material.STRUCTURE_VOID
                        : customDecorMaterial.getHitBox().isSolidHitBox() ? Material.BARRIER
                        : Material.LIGHT
                );

                if(block.getType() != Material.LIGHT) return;
                Levelled level0 = (Levelled) block.getBlockData();
                level0.setLevel(0);
                block.setBlockData(level0, true);
            }
        }.runTask(Main.plugin);
    }
}
