package doctorgallus.kerdos;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import doctorgallus.kerdos.Kerdos;
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
}

