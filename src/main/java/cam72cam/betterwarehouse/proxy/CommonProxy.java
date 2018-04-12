package cam72cam.betterwarehouse.proxy;

import cam72cam.betterwarehouse.BetterWarehouse;
import cam72cam.betterwarehouse.ShelvingTemplate;
import cam72cam.betterwarehouse.block.ShelvingBlock;
import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = BetterWarehouse.MODID)
public abstract class CommonProxy {
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
		event.getRegistry().register(BetterWarehouse.SHELVING_BLOCK);
		GameRegistry.registerTileEntity(ShelvingTile.class, ShelvingBlock.NAME);
    }
	
	@SubscribeEvent
	public static void onClickBlock(RightClickBlock event) {
		World world = event.getWorld();
		if (world.isRemote) {
			return;
		}
		
		EntityPlayer player = event.getEntityPlayer();
		if (!player.isSneaking()) {
			return;
		}
		
		IBlockState block = world.getBlockState(event.getPos());
		if (block.getBlock() == Blocks.WOODEN_SLAB) {
			ShelvingTemplate.tryFormStorage(event.getWorld(), event.getPos());
		}
	}
}
