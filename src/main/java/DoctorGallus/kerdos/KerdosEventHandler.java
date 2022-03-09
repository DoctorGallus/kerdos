package doctorgallus.kerdos;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import doctorgallus.kerdos.Kerdos;
import doctorgallus.kerdos.KerdosConfig;
import doctorgallus.kerdos.loot.KerdosLootFunction;
import doctorgallus.kerdos.FundData;


@EventBusSubscriber(modid = Kerdos.MODID)
public class KerdosEventHandler
{
	public static List<LootPool> payoutLootPools;
	public static Logger logger = LogManager.getLogger(Kerdos.MODID);


	@SubscribeEvent
	public static void onServerJoined(PlayerEvent.PlayerLoggedInEvent event)
	{
		FundData fund_data = FundData.get(event.player.getEntityWorld());
		int funds = fund_data.getFunds(event.player.getName());
		if (funds < 0)
		{
			logger.debug("Registering player: " + event.player.getName());
			fund_data.setFunds(event.player.getName(), KerdosConfig.general.startingFunds);
		}
	}

	@SubscribeEvent
	public static void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event)
	{
		if (event.getHarvester() != null)
		{
			EntityPlayer player = event.getHarvester();
			Block block = event.getState().getBlock();

			String[] block_name = block.getRegistryName().toString().split(":");
			String block_meta = Integer.toString(block.getMetaFromState(event.getState()));

			for (String entry : KerdosConfig.general.fundSourceBlocks)
			{
				String[] entry_data = entry.split("@");
				String[] entry_name = entry_data[0].split(":");
				String entry_meta = "0";

				if (entry_data.length == 2 && (entry_name.length == 2 || entry_name.length == 3))
				{
					if (entry_name.length == 3)
					{
						entry_meta = entry_name[2];
					}

					if (block_name[0].equals(entry_name[0]) && block_name[1].equals(entry_name[1])
						&& (entry_meta.equals("*") || block_meta.equals(entry_meta)))
					{
						int value;

						try
						{
							value = Integer.parseInt(entry_data[1]);
							if (!event.isSilkTouching() && KerdosConfig.general.applyFortuneEnchantment)
							{
								int fortune_lvl = event.getFortuneLevel();
								value = (int) Math.round(Math.floor(value * ((1D / (fortune_lvl + 2D)) + ((fortune_lvl + 1D) / 2D))));
							}
						}
						catch (NumberFormatException e)
						{
							continue;
						}

						FundData.get(event.getWorld()).changeFunds(player.getName(), value);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLootTableLoad(LootTableLoadEvent event)
	{
		if (KerdosEventHandler.payoutLootPools == null)
		{
			KerdosEventHandler.payoutLootPools = new ArrayList();
			KerdosConfig.Handler.currencyItems.forEach((value, stack) -> {
				logger.debug("Adding item to payout loot: " + Item.REGISTRY.getNameForObject(stack.getItem()).toString() + ":" + Integer.toString(stack.getMetadata()));
				LootEntry loot_entry = new LootEntryItem(stack.getItem(), 1, 1,
							new LootFunction[] { new KerdosLootFunction() },
							new LootCondition[0],
							Kerdos.MODID + ":payout_item_" + Integer.toString(value));
				KerdosEventHandler.payoutLootPools.add(new LootPool( new LootEntry[] { loot_entry },
						new LootCondition[0],
						new RandomValueRange(1F),
						new RandomValueRange(0F),
						Kerdos.MODID + ":payout_pool_" + Integer.toString(value)));
			});
		}

		String table_name = event.getName().toString();
		for (String curr_table : KerdosConfig.general.payoutLootTables)
		{
			if (table_name.equals(curr_table))
			{
				logger.debug("Injecting payout loot into LootTable: " + table_name);
				for (LootPool pool : KerdosEventHandler.payoutLootPools)
				{
					event.getTable().addPool(pool);
				}
				break;
			}
		}
	}
}

