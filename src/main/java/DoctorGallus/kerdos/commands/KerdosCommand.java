package doctorgallus.kerdos.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import doctorgallus.kerdos.commands.KerdosCommandFundsGet;
import doctorgallus.kerdos.commands.KerdosCommandFundsSet;
import doctorgallus.kerdos.commands.KerdosCommandFundsChange;
import doctorgallus.kerdos.commands.KerdosCommandPayout;


public class KerdosCommand extends CommandTreeBase
{
	public static final String COMMAND_NAME = "kerdos";
	public static final String COMMAND_USAGE = "";


	public KerdosCommand()
	{
		this.addSubcommand(new KerdosCommandFundsGet());
		this.addSubcommand(new KerdosCommandFundsSet());
		this.addSubcommand(new KerdosCommandFundsChange());
		this.addSubcommand(new KerdosCommandPayout());
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0; //
	}

	@Override
	public String getName()
	{
		return COMMAND_NAME;
	}

	@Override
	public String getUsage(final ICommandSender sender)
	{
		return COMMAND_USAGE;
	}
}

