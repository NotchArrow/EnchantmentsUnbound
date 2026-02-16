package com.notcharrow.enchantmentsunbound.config;

import java.util.TreeMap;

public class EnchantmentsUnboundConfig {
	public boolean staticCost = false;
	public int levelCost = 50;
	public int maxLevelCost = 1000;
	public double levelCostScalingMultiplier = 2.0;
	public boolean useXpPerEnchantLevel = true;
	public int xpPerEnchantLevel = 3;

	public boolean useCustomAnvilCap = true;
	public boolean useGlobalAnvilCap = false;
	public int globalAnvilCap = 255;

	public TreeMap<String, Integer> enchantmentAnvilCaps = new TreeMap<>();

	public boolean damageConflicts = true;
	public boolean protectionConflicts = true;
	public boolean bowConflicts = true;
	public boolean bootConflicts = false;
	public boolean tridentConflicts = false;
	public boolean crossbowConflicts = false;
	public boolean toolConflicts = false;

	public boolean itemEnchantConflicts = true;
	public boolean colorCodedRenaming = true;
	public boolean lowRenamingCost = true;
	public boolean showActionbarMessage = false;
	public boolean showTooltipMessage = true;
}
