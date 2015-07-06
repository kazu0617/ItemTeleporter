package net.kazu0617.itemteleporter;

import java.util.logging.Logger;

public class ConsoleLog
{
	@SuppressWarnings("unused")
	private Teleporter plugin;
	public Logger log = Logger.getLogger("Minecraft");
	public String cPrefix = "[Tereporter] ";
	public ConsoleLog(Teleporter plugin)
	{
		this.plugin = plugin;
	}
	public void info(String Mess)
	{
		log.info(this.cPrefix + Mess);
	}
	public void warn(String Mess)
	{
		log.warning(this.cPrefix + Mess);
	}
}
