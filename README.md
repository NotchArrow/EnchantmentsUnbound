![Enchantments Unbound](https://cdn.modrinth.com/data/cached_images/a5675e8e81dcb6f5e2f1e18a54c6f528e4d710b8.png)

<div align="center">

  <a href="https://discord.gg/ZCaGkZeb4C">
    <img src="https://cdn.modrinth.com/data/cached_images/b5c1c8717c38148ee9ecf2ae700554e8f011ac0b.png"
alt="Discord Badge"
    >
  </a>

  <a href="https://modrinth.com/mod/enchantments-unbound">
    <img src="https://cdn.modrinth.com/data/cached_images/5f5195044e56e02ee683df6265f364c473d6b2b0.png"
alt="Modrinth Badge"
    > 
  </a>

  <a href="https://github.com/NotchArrow/EnchantmentsUnbound">
    <img src="https://cdn.modrinth.com/data/cached_images/42bad645daf4235b5c050ed51afb7c44671f02b2.png"
alt="GitHub Badge"
    > 
  </a>

</div>

# About

Enchantments Unbound provides full control over removing enchantment limits and setting individual level caps. The mod is completely survival-friendly and allows you to combine enchantments directly in the anvil without worrying about the vanilla "Too Expensive!" limit. The mod can also provide overleveled enchantments through villager trading, and vanilla conflicts can be disabled to allow Mending & Infinity or Sharpness on tridents.

## Features at a Glance:
- **Remove all enchantment conflicts**
- **Bypass the “Too Expensive!” anvil limit**
- Set individual enchantment level caps
- Apply any enchantment to any item
- Generate high-level books via villager trading
- Customize anvil XP cost and scaling
- Infinite anvil durability
- Formatting in anvil renaming
- **Supports custom enchantments automatically**
- **Works on vanilla servers (client-side optional)**

## No Enchantment Conflicts

![No Conflicts](https://cdn.modrinth.com/data/cached_images/e3034c0d6e7c10f91530af7d3a6b71481f0219b7_0.webp)
<div align="center">
  A bow with Mending and Infinity applied at the same time in an anvil.
</div>
<br>

Disable enchantment conflicts, such as:
- Mending + Infinity
- Sharpness + Smite + Bane of Arthropods
- Protection + Fire Protection + Blast Protection + Projectile Protection

## No Too Expensive

![No Too Expensive](https://cdn.modrinth.com/data/cached_images/393084ec16cd0fdfade8e881e4b977d6f5954c0a.png)
<div align="center">
  An anvil transaction with a cost of 90 levels, well above the vanilla limit of 39.
</div>
<br>

Unlock all anvil transactions that you have enough xp for, and customize the price to balance your own gameplay. The new cost is rendered normally if the mod is installed on the client, but it is also displayed in the tooltip for players who are joining modded servers on vanilla clients.

## Uncapped Enchants

![Uncapped Enchants](https://cdn.modrinth.com/data/cached_images/2de7262ae8ebe2e99d9e3fa5747375f255cb4359_0.webp)
<div align="center">
  An anvil combination with enchantments above the vanilla values, with caps configured individually.
</div>
<br>

Enchantments Unbound allows custom anvil combinations that exceed vanilla enchantment level caps.
- **In vanilla:** Sharpness IV + Sharpness IV = Sharpness V
- **In Enchantments Unbound:** Sharpness V + Sharpness V = Sharpness VI.

Enchantment caps can be configured individually, and Enchantments Unbound has full support for custom enchants. For example: Sharpness can be capped at 6, Efficiency can be capped at 20, Unbreaking can stay at the vanilla value of 3, and a custom enchant can go up to 255.

## Custom XP Costs

![Custom XP Costs](https://cdn.modrinth.com/data/cached_images/57081ec8362b01587213190340ca4a49a4f07f71.png)
<div align="center">
  An anvil transaction with two different level costs, varying based on the configuration.
</div>
<br>

Enchantments Unbound supports three anvil pricing modes:
- **Static Cost:** Use a fixed cost for every anvil transaction. Every combination can cost 1 level, or every combination can cost 50 levels, depending on the configuration.
- **Scaling Cost Multiplier:** Use a scaling cost for every anvil transaction completed, based on a configured multiplier. Vanilla uses a multiplier of **2**, which makes costs exponentially more expensive for each combination or repair.
- **XP Per Enchantment Level:** Calculate a cost based on the total amount of XP levels on the final item. For example, a Sharpness V Unbreaking III sword would have 5 + 3 = 8 enchant levels, multiplied by the configured cost per level. Enchantments Unbound defaults to a value of 3, so 8 enchant levels * 3 XP per level = 24 XP levels for the combination.

## Colorful Item Renaming

![Colorful Item Renaming](https://cdn.modrinth.com/data/cached_images/09946a43c672ef66ce9f4640c245bb66ef8adba6_0.webp)
<div align="center">
  A sword displaying a colorful name via anvil formatting color codes.
</div>
<br>

Items can utilize formatting codes to rename items with custom styles and colors. The colors are based on the formatting codes from the Minecraft wiki, but the mod utilizes the `&` character instead of the `§`.

The image also showcases an optional built-in resource pack for the mod, which can visually change Sharpness V into Sharpness 5, and Sharpness CCLV into Sharpness 255.

<details>
<summary>Minecraft Formatting Codes</summary>

![Minecraft Formatting Codes](https://cdn.modrinth.com/data/cached_images/3236f8598dc80a473ec81bd497893e271362abcd.png)
<div align="center">
  A table of all Minecraft formatting and color codes.
</div>
<br>

The full details of the Minecraft formatting system can be found on the [Minecraft Wiki](https://minecraft.wiki/w/Formatting_codes).

</details>

## Villager Support

![Villager Support](https://cdn.modrinth.com/data/cached_images/4c6c0cc2f99096836475e0f0ebd7b51ff545c4d2_0.webp)
<div align="center">
  A librarian villager with a Smite VII book for sale.
</div>
<br>

Enchantments Unbound does not affect villagers by default, but it can be configured to allow librarian villager trades to reach other values defined in the configuration. While a normal villager could roll a Sharpness book between I and V, a modified villager could roll a Sharpness book between I and X.

## Creative Freedom

![Creative Freedom](https://cdn.modrinth.com/data/cached_images/ebbd3aca7eccda98fc8d5affe54f9ce4cb32bc17_0.webp)
<div align="center">
  An operator adding Knockback 34 to a stick in creative mode.
</div>
<br>

Enchantments Unbound adds other small quality of life features, such as modifying the `/enchant` command to support configured values and ignore vanilla restrictions. It also adds a setting that gives anvils infinite durability.

<hr>

# Configuration

### Everything in Enchantments Unbound is togglable or configurable.

The mod can be configured through the ingame [ModMenu](https://modrinth.com/mod/modmenu) and [Cloth Config](https://modrinth.com/mod/cloth-config) GUI. Server owners can configure settings in the `enchantmentsunbound.json`

<details>
<summary>Default Configuration Overview</summary>

- The anvil XP calculations are adjusted to cost 3 XP levels per enchantment level on the output item. For example, a Sharpness V Unbreaking III sword would have 5 + 3 = 8 enchant levels, so 8 enchant levels * 3 XP per level = 24 XP levels for the combination.
- Anvil caps default to 255 for every vanilla enchantment, while modded enchantments remain unchanged. Since the XP costs add up quickly when exponentially more books are required for higher levels, the configuration remains balanced for survival gameplay.
- Villagers are unaffected by the default configuration.
- The `/enchant` command is adjusted to allow levels up to 255 for every vanilla enchantment, while modded enchantments remain unchanged.
- Conflicts among Sharpness, Smite, and Bane of Arthropods are disabled.
- Conflicts among Protection, Blast Protection, Fire Protection, and Projectile Protection are disabled.
- Conflicts between Mending and Infinity are disabled.
- Color-coded anvil renaming is enabled, and anvil renaming costs are capped at 1 XP Level.
- The tooltip message in the anvil is enabled for vanilla client support, but the tooltip disappears after claiming the item and it can be easily disabled.

</details>

<details>
<summary>Default Configuration File</summary>

```json
{
  "staticCost": false,
  "levelCost": 50,
  "maxLevelCost": 1000,
  "levelCostScalingMultiplier": 2.0,
  "useXpPerEnchantLevel": true,
  "xpPerEnchantLevel": 3,
  "useCustomAnvilCap": true,
  "useGlobalAnvilCap": false,
  "globalAnvilCap": 255,
  "enchantmentAnvilCaps": {
    "minecraft:aqua_affinity": 255,
    "minecraft:bane_of_arthropods": 255,
    "minecraft:binding_curse": 255,
    "minecraft:blast_protection": 255,
    "minecraft:breach": 255,
    "minecraft:channeling": 255,
    "minecraft:density": 255,
    "minecraft:depth_strider": 255,
    "minecraft:efficiency": 255,
    "minecraft:feather_falling": 255,
    "minecraft:fire_aspect": 255,
    "minecraft:fire_protection": 255,
    "minecraft:flame": 255,
    "minecraft:fortune": 255,
    "minecraft:frost_walker": 255,
    "minecraft:impaling": 255,
    "minecraft:infinity": 255,
    "minecraft:knockback": 255,
    "minecraft:looting": 255,
    "minecraft:loyalty": 255,
    "minecraft:luck_of_the_sea": 255,
    "minecraft:lunge": 255,
    "minecraft:lure": 255,
    "minecraft:mending": 255,
    "minecraft:multishot": 255,
    "minecraft:piercing": 255,
    "minecraft:power": 255,
    "minecraft:projectile_protection": 255,
    "minecraft:protection": 255,
    "minecraft:punch": 255,
    "minecraft:quick_charge": 255,
    "minecraft:respiration": 255,
    "minecraft:riptide": 255,
    "minecraft:sharpness": 255,
    "minecraft:silk_touch": 255,
    "minecraft:smite": 255,
    "minecraft:soul_speed": 255,
    "minecraft:sweeping_edge": 255,
    "minecraft:swift_sneak": 255,
    "minecraft:thorns": 255,
    "minecraft:unbreaking": 255,
    "minecraft:vanishing_curse": 255,
    "minecraft:wind_burst": 255
  },
  "modifyVillagers": false,
  "useGlobalVillagerCap": false,
  "globalVillagerCap": 10,
  "enchantmentVillagerCaps": {
    "minecraft:aqua_affinity": 10,
    "minecraft:bane_of_arthropods": 10,
    "minecraft:binding_curse": 10,
    "minecraft:blast_protection": 10,
    "minecraft:breach": 10,
    "minecraft:channeling": 10,
    "minecraft:density": 10,
    "minecraft:depth_strider": 10,
    "minecraft:efficiency": 10,
    "minecraft:feather_falling": 10,
    "minecraft:fire_aspect": 10,
    "minecraft:fire_protection": 10,
    "minecraft:flame": 10,
    "minecraft:fortune": 10,
    "minecraft:frost_walker": 10,
    "minecraft:impaling": 10,
    "minecraft:infinity": 10,
    "minecraft:knockback": 10,
    "minecraft:looting": 10,
    "minecraft:loyalty": 10,
    "minecraft:luck_of_the_sea": 10,
    "minecraft:lunge": 10,
    "minecraft:lure": 10,
    "minecraft:mending": 10,
    "minecraft:multishot": 10,
    "minecraft:piercing": 10,
    "minecraft:power": 10,
    "minecraft:projectile_protection": 10,
    "minecraft:protection": 10,
    "minecraft:punch": 10,
    "minecraft:quick_charge": 10,
    "minecraft:respiration": 10,
    "minecraft:riptide": 10,
    "minecraft:sharpness": 10,
    "minecraft:silk_touch": 10,
    "minecraft:smite": 10,
    "minecraft:soul_speed": 10,
    "minecraft:sweeping_edge": 10,
    "minecraft:swift_sneak": 10,
    "minecraft:thorns": 10,
    "minecraft:unbreaking": 10,
    "minecraft:vanishing_curse": 10,
    "minecraft:wind_burst": 10
  },
  "useCustomCommandCap": true,
  "useGlobalCommandCap": false,
  "globalCommandCap": 255,
  "enchantmentCommandCaps": {
    "minecraft:aqua_affinity": 255,
    "minecraft:bane_of_arthropods": 255,
    "minecraft:binding_curse": 255,
    "minecraft:blast_protection": 255,
    "minecraft:breach": 255,
    "minecraft:channeling": 255,
    "minecraft:density": 255,
    "minecraft:depth_strider": 255,
    "minecraft:efficiency": 255,
    "minecraft:feather_falling": 255,
    "minecraft:fire_aspect": 255,
    "minecraft:fire_protection": 255,
    "minecraft:flame": 255,
    "minecraft:fortune": 255,
    "minecraft:frost_walker": 255,
    "minecraft:impaling": 255,
    "minecraft:infinity": 255,
    "minecraft:knockback": 255,
    "minecraft:looting": 255,
    "minecraft:loyalty": 255,
    "minecraft:luck_of_the_sea": 255,
    "minecraft:lunge": 255,
    "minecraft:lure": 255,
    "minecraft:mending": 255,
    "minecraft:multishot": 255,
    "minecraft:piercing": 255,
    "minecraft:power": 255,
    "minecraft:projectile_protection": 255,
    "minecraft:protection": 255,
    "minecraft:punch": 255,
    "minecraft:quick_charge": 255,
    "minecraft:respiration": 255,
    "minecraft:riptide": 255,
    "minecraft:sharpness": 255,
    "minecraft:silk_touch": 255,
    "minecraft:smite": 255,
    "minecraft:soul_speed": 255,
    "minecraft:sweeping_edge": 255,
    "minecraft:swift_sneak": 255,
    "minecraft:thorns": 255,
    "minecraft:unbreaking": 255,
    "minecraft:vanishing_curse": 255,
    "minecraft:wind_burst": 255
  },
  "damageConflicts": true,
  "protectionConflicts": true,
  "bowConflicts": true,
  "bootConflicts": false,
  "tridentConflicts": false,
  "crossbowConflicts": false,
  "toolConflicts": false,
  "infiniteAnvilDurability": false,
  "itemEnchantConflicts": true,
  "colorCodedRenaming": true,
  "lowRenamingCost": true,
  "showActionbarMessage": false,
  "showTooltipMessage": true
}
```

</details>

![Configuration](https://cdn.modrinth.com/data/cached_images/2cbeb7c35eabe47eebecca91638df581e2e19839_0.webp)
<div align="center">
  One of the main configuration screens with individual level caps for each enchantment.
</div>
<br>

## Player Commands

![Anvilcaps Command](https://cdn.modrinth.com/data/cached_images/ffef68813fc65ecc31d6314601736fdf5e7a10a5.png)
<div align="center">
  A player on the server viewing the configured anvil cap information.
</div>
<br>

![Anvilcost Command](https://cdn.modrinth.com/data/cached_images/d26d0ace7b0c909e63a83f90ce3a441dbaffc7c6.png)
<div align="center">
  A player on the server viewing the anvil level cost calculations.
</div>
<br>

![Misc Command](https://cdn.modrinth.com/data/cached_images/127ee1194554ac056554aedc3c7a70f75d947d9c.png)
<div align="center">
  A player on the server viewing miscellaneous modded changes.
</div>
<br>