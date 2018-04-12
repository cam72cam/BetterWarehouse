package cam72cam.betterwarehouse.proxy;

import cam72cam.betterwarehouse.BetterWarehouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = BetterWarehouse.MODID, value=Side.SERVER)
public class ServerProxy extends CommonProxy {
	@Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int nop1, int nop2) {
    	return null;
    }
}
