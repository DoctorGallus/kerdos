package doctorgallus.kerdos;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import doctorgallus.kerdos.KerdosConfig;
import doctorgallus.kerdos.commands.KerdosCommand;
import doctorgallus.kerdos.loot.KerdosLootFunction;


@Mod(modid = Kerdos.MODID, version = Kerdos.VERSION, name = Kerdos.NAME, acceptableRemoteVersions = "*", useMetadata = true)
public class Kerdos
{
	public static final String NAME = "Kerdos";
	public static final String MODID = "kerdos";
	public static final String VERSION = "1.12-1.0.0.0-rc1";

	public static Logger logger = LogManager.getLogger(Kerdos.MODID);
	// public static KerdosConfig config = KerdosConfig();
	

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LootFunctionManager.registerFunction(new KerdosLootFunction.Serializer());
	}


	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}


	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		KerdosConfig.Handler.reloadConfig();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new KerdosCommand());
	}
}

