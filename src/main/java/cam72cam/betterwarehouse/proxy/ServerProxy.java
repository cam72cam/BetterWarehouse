package cam72cam.betterwarehouse.proxy;

import cam72cam.betterwarehouse.BetterWarehouse;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = BetterWarehouse.MODID, value=Side.SERVER)
public class ServerProxy extends CommonProxy {

}
