package com.github.MinersStudios.msDecor.crafts.home.chairs;

import com.github.MinersStudios.msDecor.Main;
import com.github.MinersStudios.msDecor.enums.CustomDecorMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;

public class CoolArmchair {

	public static void addRecipes() {
		Bukkit.addRecipe(craftCoolArmchair());
	}

	@Nonnull
	public static ShapedRecipe craftCoolArmchair() {
		CustomDecorMaterial customDecorMaterial = CustomDecorMaterial.COOL_ARMCHAIR;
		return new ShapedRecipe(new NamespacedKey(Main.plugin, customDecorMaterial.name()), customDecorMaterial.getItemStack())
				.shape(
						"L  ",
						"LLL",
						"I I"
				).setIngredient('I', Material.IRON_NUGGET)
				.setIngredient('L', Material.LEATHER);
	}
}
