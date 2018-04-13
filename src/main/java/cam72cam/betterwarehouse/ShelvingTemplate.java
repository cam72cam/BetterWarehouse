package cam72cam.betterwarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShelvingTemplate {

	private static List<ShelvingTemplate> templates = new ArrayList<ShelvingTemplate>();

	static {
		for (int radius = 3; radius <= 4; radius++) {
			for (EnumFacing facing : new EnumFacing[] { EnumFacing.EAST, EnumFacing.NORTH }) {
				templates.add(new ShelvingTemplate(radius, facing));
			}
		}
	}

	public static boolean tryFormStorage(World world, BlockPos pos) {
		for (ShelvingTemplate template : templates) {
			if (template.matches(world, pos)) {
				template.place(world, pos);
				return true;
			}
		}
		return false;
	}

	private final Map<BlockPos, Class<? extends Block>> positions;
	private final int radius;
	private final EnumFacing facing;

	public ShelvingTemplate(int radius, EnumFacing facing) {
		this.positions = new HashMap<BlockPos, Class<? extends Block>>();
		this.radius = radius;
		this.facing = facing;
		for (int y = 0; y < 2; y++) {
			for (int z = -1; z <= 1; z++) {
				for (int x = -radius; x <= radius; x++) {
					BlockPos pos;
					if (facing == EnumFacing.NORTH) {
						pos = new BlockPos(x, y, z);
					} else {
						pos = new BlockPos(z, y, x);
					}
					Class<? extends Block> block = Blocks.AIR.getClass();
					if (x == radius || x == -radius) {
						block = BlockFence.class;
					} else if (y == 0) {
						block = Blocks.WOODEN_SLAB.getClass();
					}

					positions.put(pos, block);
				}
			}
		}
	}

	public boolean matches(World world, BlockPos center) {
		for (BlockPos offset : positions.keySet()) {
			BlockPos pos = center.add(offset);
			if (!world.getBlockState(pos).getBlock().getClass().isAssignableFrom(positions.get(offset))) {
				return false;
			}
		}
		return true;
	}

	public void place(World world, BlockPos center) {
		Map<BlockPos, IBlockState> states = new HashMap<BlockPos, IBlockState>();
		for (BlockPos offset : positions.keySet()) {
			BlockPos pos = center.add(offset);
			IBlockState state = world.getBlockState(pos).getActualState(world, pos);
			states.put(offset, state);
		}
		
		for (BlockPos offset : positions.keySet()) {
			BlockPos pos = center.add(offset);
			world.setBlockState(pos, BetterWarehouse.SHELVING_BLOCK.getDefaultState());
			ShelvingTile shelving = ShelvingTile.get(world, pos);
			shelving.init(radius * 2 + 1, center, offset, states.get(offset), facing);
		}
	}
}