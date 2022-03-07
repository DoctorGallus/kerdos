package io.notuxic.kerdos;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Kerdos.MODID, version = Kerdos.VERSION, name = Kerdos.NAME, useMetadata = true)
public class Kerdos
{
	public static final String NAME = "Kerdos";
	public static final String MODID = "kerdos";
	public static final String VERSION = "1.12-1.0.0.0-beta1";

	public static final Logger LOGGER = LogManager.getLogger(Kerdos.MODID);
	

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.info(Kerdos.NAME + ": preInit");
	}


	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		LOGGER.info(Kerdos.NAME + ": init");
	}


	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		LOGGER.info(Kerdos.NAME + ": postInit");
	}
}
