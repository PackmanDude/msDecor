package com.github.minersstudios.msdecor.listeners.player;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msdecor.customdecor.CustomDecor;
import com.github.minersstudios.msdecor.customdecor.CustomDecorData;
import com.github.minersstudios.msdecor.utils.BlockUtils;
import com.github.minersstudios.msdecor.utils.CustomDecorUtils;
import com.github.minersstudios.msdecor.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerInteractListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlaceArmorStand(@NotNull PlayerInteractEvent event) {
		if (
				event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| event.getClickedBlock() == null
				|| event.getHand() == null
		) return;
		if (
				BlockUtils.isCustomDecorMaterial(event.getClickedBlock().getRelative(event.getBlockFace()).getType())
				|| PlayerUtils.isItemCustomDecor(event.getPlayer().getInventory().getItemInMainHand())
		) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
		if (event.getClickedBlock() == null || event.getHand() == null) return;
		Action action = event.getAction();
		BlockFace blockFace = event.getBlockFace();
		Block clickedBlock = event.getClickedBlock();
		Block replaceableBlock =
				BlockUtils.REPLACE.contains(clickedBlock.getType())
				? clickedBlock
				: clickedBlock.getRelative(blockFace);
		Location replaceableBlockLocation = replaceableBlock.getLocation();

		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		GameMode gameMode = player.getGameMode();
		EquipmentSlot hand = event.getHand();
		ItemStack itemInMainHand = inventory.getItemInMainHand();
		if (PlayerUtils.isItemCustomBlock(itemInMainHand)) return;
		if (hand != EquipmentSlot.HAND && PlayerUtils.isItemCustomDecor(itemInMainHand)) {
			hand = EquipmentSlot.HAND;
		}
		ItemStack itemInHand = inventory.getItem(hand);

		if (
				action == Action.RIGHT_CLICK_BLOCK
				&& PlayerUtils.isItemCustomDecor(itemInHand)
				&& (event.getHand() == EquipmentSlot.HAND || hand == EquipmentSlot.OFF_HAND)
				&& gameMode != GameMode.ADVENTURE
				&& gameMode != GameMode.SPECTATOR
				&& (
				(!clickedBlock.getType().isInteractable()
				|| Tag.STAIRS.isTagged(clickedBlock.getType()))
				|| (player.isSneaking() && clickedBlock.getType().isInteractable()
				) || clickedBlock.getType() == Material.NOTE_BLOCK)
				&& BlockUtils.REPLACE.contains(clickedBlock.getRelative(blockFace).getType())
		) {
			CustomDecorData customDecorData = CustomDecorUtils.getCustomDecorDataWithFace(itemInHand, blockFace);
			if (customDecorData == null) return;

			for (Entity nearbyEntity : player.getWorld().getNearbyEntities(replaceableBlockLocation.toCenterLocation(), 0.5d, 0.5d, 0.5d)) {
				EntityType entityType = nearbyEntity.getType();
				if (
						entityType != EntityType.DROPPED_ITEM
						&& (customDecorData.getHitBox().isSolidHitBox()
						|| entityType == EntityType.ARMOR_STAND
						|| entityType == EntityType.ITEM_FRAME)
				) return;
			}

			CustomDecor customDecor = new CustomDecor(replaceableBlock, player, customDecorData);
			CustomDecorData.Facing facing = customDecorData.getFacing();
			if (
					facing == null || blockFace != BlockFace.DOWN
					&& replaceableBlock.getRelative(BlockFace.DOWN).getType().isSolid()
					&& facing == CustomDecorData.Facing.FLOOR
			) {
				customDecor.setCustomDecor(BlockFace.UP, hand, null);
			} else if (
					blockFace != BlockFace.UP
					&& replaceableBlock.getRelative(BlockFace.UP).getType().isSolid()
					&& facing == CustomDecorData.Facing.CEILING
			) {
				customDecor.setCustomDecor(BlockFace.DOWN, hand, null);
			} else if (
					blockFace != BlockFace.UP
					&& blockFace != BlockFace.DOWN
					&& facing == CustomDecorData.Facing.WALL
			) {
				customDecor.setCustomDecor(blockFace, hand, null);
			}
			BlockUtils.removeBlock(replaceableBlockLocation);
		}

		if (
				action == Action.LEFT_CLICK_BLOCK
				&& BlockUtils.isCustomDecorMaterial(clickedBlock.getType())
				&& (player.isSneaking() && player.getGameMode() == GameMode.SURVIVAL
				|| gameMode == GameMode.SURVIVAL && clickedBlock.getType() == Material.STRUCTURE_VOID
				|| gameMode == GameMode.CREATIVE)
		) {
			CustomDecorData customDecorData = CustomDecorUtils.getCustomDecorDataByLocation(clickedBlock.getLocation());
			if (customDecorData == null) return;
			new CustomDecor(clickedBlock, player, customDecorData).breakCustomDecor();
		}

		if (
				action == Action.RIGHT_CLICK_BLOCK
				&& Tag.SHULKER_BOXES.isTagged(clickedBlock.getType())
				&& clickedBlock.getBlockData() instanceof Directional directional
				&& BlockUtils.isCustomDecorMaterial(clickedBlock.getRelative(directional.getFacing()).getType())
		) {
			event.setCancelled(true);
		}
	}
}
