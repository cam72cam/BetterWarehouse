package cam72cam.betterwarehouse;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import org.apache.logging.log4j.Logger;

import cam72cam.betterwarehouse.block.ShelvingBlock;
import cam72cam.betterwarehouse.proxy.CommonProxy;

@Mod(modid = BetterWarehouse.MODID, name = BetterWarehouse.NAME, version = BetterWarehouse.VERSION)
public class BetterWarehouse
{
    public static final String MODID = "betterwarehouse";
    public static final String NAME = "Better Warehouse";
    public static final String VERSION = "1.0";

    private static Logger logger;
	public static BetterWarehouse instance;
	
	public static ShelvingBlock SHELVING_BLOCK = new ShelvingBlock();
	
	@SidedProxy(clientSide="cam72cam.betterwarehouse.proxy.ClientProxy", serverSide="cam72cam.betterwarehouse.proxy.ServerProxy")
	public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        instance = this;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }
    
    public static void debug(String msg, Object...params) {
    	if (logger == null) {
    		System.out.println("DEBUG: " + String.format(msg, params));
    		return;
    	}
    	
		logger.debug(String.format(msg, params));
    }
    public static void info(String msg, Object...params) {
    	if (logger == null) {
    		System.out.println("INFO: " + String.format(msg, params));
    		return;
    	}
    	
    	logger.info(String.format(msg, params));
    }
    public static void warn(String msg, Object...params) {
    	if (logger == null) {
    		System.out.println("WARN: " + String.format(msg, params));
    		return;
    	}
    	
    	logger.warn(String.format(msg, params));
    }
    public static void error(String msg, Object...params) {
    	if (logger == null) {
    		System.out.println("ERROR: " + String.format(msg, params));
    		return;
    	}
    	
    	logger.error(String.format(msg, params));
    }
	public static void catching(Throwable ex) {
    	if (logger == null) {
    		ex.printStackTrace();
    		return;
    	}
    	
		logger.catching(ex);
	}
}
