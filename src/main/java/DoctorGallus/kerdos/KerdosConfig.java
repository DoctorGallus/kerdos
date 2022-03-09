package doctorgallus.kerdos;


import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import doctorgallus.kerdos.Kerdos;


@Config(modid = Kerdos.MODID, category = "")
public class KerdosConfig
{
	public static final GeneralConfig general = new GeneralConfig();

	public static class GeneralConfig
	{
		@Comment({"Initial funds players start with."})
		@Config.RangeInt(min = 0)
		public int startingFunds = 0;

		@Comment({"Minimum percentage of funds to be released on payout."})
		@Config.RangeDouble(min = 0D, max = 1D)
		public double payoutRateMin = 0.3D;

		@Comment({"Maximum percentage of funds to be released on payout."})
		@Config.RangeDouble(min = 0D, max = 1D)
		public double payoutRateMax = 0.5D;

		@Comment({
			"Currency items used for payout.",
			"Syntax: modid:itemname@value",
			"Syntax: modid:itemname:meta@value",
			"where \"value\" is the monetary value of that item.",
			"When omitting \"meta\" it defaults to \"0\"."
		})
		@Config.RequiresWorldRestart
		public String[] currencyItems = {
			"jjcoin:copper_coin@1",
			"jjcoin:silver_coin@10",
			"jjcoin:gold_coin@100",
			"jjcoin:diamond_coin@1000"
		};

		@Comment({"Loot tables to use for payout."})
		@Config.RequiresWorldRestart
		public String[] payoutLootTables = {
			"minecraft:chests/abandoned_mineshaft",
			"minecraft:chests/desert_pyramid",
			"minecraft:chests/end_city_treasure",
			"minecraft:chests/igloo_chest",
			"minecraft:chests/jungle_temple",
			"minecraft:chests/nether_bridge",
			"minecraft:chests/simple_dungeon",
			"minecraft:chests/stronghold_corridor",
			"minecraft:chests/stronghold_crossing",
			"minecraft:chests/stronghold_library",
			"minecraft:chests/village_blacksmith",
			"minecraft:chests/woodland_mansion",
			"minecraft:entities/evocation_illager",
			"minecraft:entities/giant",
			"minecraft:entities/husk",
			"minecraft:entities/skeleton",
			"minecraft:entities/stray",
			"minecraft:entities/villager",
			"minecraft:entities/vindication_illager",
			"minecraft:entities/witch",
			"minecraft:entities/wither_skeleton",
			"minecraft:entities/zombie",
			"minecraft:entities/zombie_pigman",
			"minecraft:entities/zombie_villager"
		};

		@Comment({
			"Blocks which increase your fund when harvested.",
			"Syntax: modid:itemname@value",
			"Syntax: modid:itemname:meta@value",
			"where \"value\" is the value by which a players fund should be increased.",
			"When omitting \"meta\" it defaults to \"0\".",
			"A \"*\" can be used to match all possible \"meta\" values."
		})
		public String[] fundSourceBlocks = {
			"minecraft:diamond_ore@10",
			"minecraft:emerald_block@5",
			"biomesoplenty:gem_ore:*@5"
		};

		@Comment({
			"If enabled, Fortune enchantments also apply to fund increases when harvesting a block.",
			"Uses the formula `base_value * (1 / (fortune_lvl + 2) + (fortune_lvl + 1) / 2)` which corresponds to the average drops increase for ores in vanilla minecraft."
		})
		public boolean applyFortuneEnchantment = true;
	}


	@EventBusSubscriber(modid = Kerdos.MODID)
	public static class Handler
	{
		public static TreeMap<Integer, ItemStack> currencyItems = new TreeMap();

		@SubscribeEvent
		public static void onConfigChangedEvent(OnConfigChangedEvent event)
		{
			if (event.getModID().equals(Kerdos.MODID))
			{
				ConfigManager.sync(Kerdos.MODID, Config.Type.INSTANCE);

				reloadConfig();
			}
		}

		public static void reloadConfig()
		{
			KerdosEventHandler.payoutLootPools = null;
			KerdosConfig.Handler.currencyItems.clear();
			for (String entry : KerdosConfig.general.currencyItems)
			{
				String[] data = entry.split("@");
				String[] name = data[0].split(":");

				if (name.length >= 2)
				{
					Item item = Item.getByNameOrId(name[0] + ":" + name[1]);

					if (item != null)
					{
						ItemStack stack = item.getDefaultInstance();

						if (name.length == 3)
						{
							int meta;

							try
							{
								meta = Integer.parseInt(name[2]);
							}
							catch (NumberFormatException e)
							{
								continue;
							}

							stack.setItemDamage(meta);
						}

						int value;
						try
						{
							value = Integer.parseInt(data[1]);
						}
						catch (NumberFormatException e)
						{
							continue;
						}

						KerdosConfig.Handler.currencyItems.put(value, stack);
					}
				}
			}
		}
	}
}

