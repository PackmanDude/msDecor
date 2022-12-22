package com.github.minersstudios.msdecor.utils;

import com.github.minersstudios.msdecor.customdecor.CustomDecorData;
import com.github.minersstudios.msdecor.customdecor.FaceableByType;
import com.github.minersstudios.msdecor.customdecor.Typed;
import com.github.minersstudios.msdecor.customdecor.christmas.*;
import com.github.minersstudios.msdecor.customdecor.decorations.home.*;
import com.github.minersstudios.msdecor.customdecor.decorations.home.heads.DeerHead;
import com.github.minersstudios.msdecor.customdecor.decorations.home.heads.HoglinHead;
import com.github.minersstudios.msdecor.customdecor.decorations.home.heads.ZoglinHead;
import com.github.minersstudios.msdecor.customdecor.decorations.home.plushes.BMOPlush;
import com.github.minersstudios.msdecor.customdecor.decorations.home.plushes.BrownBearPlush;
import com.github.minersstudios.msdecor.customdecor.decorations.home.plushes.RacoonPlush;
import com.github.minersstudios.msdecor.customdecor.decorations.street.*;
import com.github.minersstudios.msdecor.customdecor.furniture.chairs.*;
import com.github.minersstudios.msdecor.customdecor.furniture.lamps.BigLamp;
import com.github.minersstudios.msdecor.customdecor.furniture.lamps.SmallLamp;
import com.github.minersstudios.msdecor.customdecor.furniture.nightstand.*;
import com.github.minersstudios.msdecor.customdecor.furniture.tables.BigTable;
import com.github.minersstudios.msdecor.customdecor.furniture.tables.SmallTable;
import com.github.minersstudios.msdecor.customdecor.halloween.SkeletonHand;
import com.github.minersstudios.msdecor.customdecor.other.Poop;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CustomDecorUtils {
	public static final Map<String, CustomDecorData> CUSTOM_DECORS = new HashMap<>();
	public static final List<Recipe> CUSTOM_DECOR_RECIPES = new ArrayList<>();

	public static final boolean isChristmas = true;
	public static final boolean isHalloween = false;

	private CustomDecorUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static void registerCustomDecors() {
		//<editor-fold desc="Custom decors">
		new Ball().register(isChristmas);
		new TallBall().register(isChristmas);
		new SnowmanBall().register(isChristmas);
		new SnowflakeOnString().register(isChristmas);
		new StarOnString().register(isChristmas);
		new SantaSock().register(isChristmas);
		new Snowman().register(isChristmas);
		new TreeStar().register(isChristmas);

		new SkeletonHand().register(isHalloween);

		new Poop().register();

		new DeerHead().register();
		new HoglinHead().register();
		new ZoglinHead().register();

		new BMOPlush().register();
		new BrownBearPlush().register();
		new RacoonPlush().register();

		new Cell().register();
		new CookingPot().register();
		new OldCamera().register();
		new Patefon().register();
		new Piggybank().register();
		new SmallClock().register();
		new SmallGlobus().register();

		new Bankomat().register();
		new Brazier().register();
		new FireHydrant().register();
		new IronTrashcan().register();
		new Wheelbarrow().register();

		new BigLamp().register();
		new SmallLamp().register();

		new BigTable().register();
		new SmallTable().register();

		new Armchair().register();
		new BarStool().register();
		new Chair().register();
		new CoolArmchair().register();
		new CoolChair().register();
		new RockingChair().register();
		new SmallArmchair().register();
		new SmallChair().register();

		new AcaciaNightstand().register();
		new BirchNightstand().register();
		new CrimsonNightstand().register();
		new DarkOakNightstand().register();
		new JungleNightstand().register();
		new MangroveNightstand().register();
		new OakNightstand().register();
		new SpruceNightstand().register();
		new WarpedNightstand().register();
		//</editor-fold>
	}

	public static @Nullable CustomDecorData getCustomDecorDataByLocation(@NotNull Location location) {
		for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location.clone().add(0.5d, 0.0d, 0.5d), 0.2d, 0.3d, 0.2d)) {
			if (nearbyEntity instanceof ArmorStand armorStand) {
				return getCustomDecorDataByEntity(armorStand);
			}
		}
		for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location.toCenterLocation(), 0.5d, 0.5d, 0.5d)) {
			if (nearbyEntity instanceof ItemFrame itemFrame) {
				return getCustomDecorDataByEntity(itemFrame);
			}
		}
		return null;
	}

	@Contract("null, _ -> null")
	public static @Nullable CustomDecorData getCustomDecorDataWithFace(@Nullable ItemStack itemStack, @Nullable BlockFace blockFace) {
		CustomDecorData customDecorData = getCustomDecorDataByItem(itemStack);
		if (customDecorData instanceof FaceableByType faceableByType) {
			Typed.Type type = faceableByType.getTypeByFace(blockFace);
			if (type != null) {
				return faceableByType.createCustomDecorData(type);
			}
		}
		return customDecorData;
	}

	@Contract("null -> null")
	public static @Nullable CustomDecorData getCustomDecorDataByItem(@Nullable ItemStack itemStack) {
		if (itemStack == null || itemStack.getItemMeta() == null) return null;
		for (CustomDecorData customDecorData : CUSTOM_DECORS.values()) {
			if (customDecorData.isSimilar(itemStack)) {
				return customDecorData;
			}
			if (customDecorData instanceof Typed typed) {
				Typed.Type type = typed.getType(itemStack);
				if (type != null) {
					return typed.createCustomDecorData(type);
				}
			}
		}
		return null;
	}

	@Contract("null -> null")
	public static @Nullable CustomDecorData getCustomDecorDataByEntity(@Nullable Entity entity) {
		if (!EntityUtils.isCustomDecorEntity(entity)) return null;
		if (entity instanceof ArmorStand armorStand) {
			return getCustomDecorDataByItem(armorStand.getEquipment().getHelmet());
		} else if (entity instanceof ItemFrame itemFrame) {
			return getCustomDecorDataByItem(itemFrame.getItem());
		}
		return null;
	}
}