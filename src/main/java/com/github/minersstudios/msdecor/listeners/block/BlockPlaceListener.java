package com.github.minersstudios.msdecor.listeners.block;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msdecor.utils.BlockUtils;
import com.github.minersstudios.msdecor.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class BlockPlaceListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(@NotNull BlockPlaceEvent event) {
		if (
				BlockUtils.isCustomDecorMaterial(event.getBlockReplacedState().getType())
				|| PlayerUtils.isItemCustomDecor(event.getPlayer().getInventory().getItemInMainHand())
		) {
			event.setCancelled(true);
		}
	}
}
