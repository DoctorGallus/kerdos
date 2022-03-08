package doctorgallus.kerdos;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import doctorgallus.kerdos.Kerdos;
import doctorgallus.kerdos.KerdosConfig;
import doctorgallus.kerdos.FundData;


@EventBusSubscriber(modid = Kerdos.MODID)
public class KerdosEventHandler
{
	public static Logger logger = LogManager.getLogger(Kerdos.MODID);


	@SubscribeEvent
	public static void onServerJoined(PlayerEvent.PlayerLoggedInEvent event)
	{
		FundData fund_data = FundData.get(event.player.getEntityWorld());
		int funds = fund_data.getFunds(event.player.getName());
		if (funds < 0)
		{
			logger.info("Registering player: " + event.player.getName());
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
}

