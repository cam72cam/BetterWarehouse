package cam72cam.betterwarehouse.block;

import javax.annotation.Nonnull;

import cam72cam.betterwarehouse.BetterWarehouse;
import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class ShelvingBlock extends Block {
	public static final String NAME = "shelving_block";
	
	public static final AxisAlignedBB AIR_BB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	public static final AxisAlignedBB SLAB_BB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	public static final AxisAlignedBB SIDES_BB_EAST = new AxisAlignedBB(0, 0, 0, 0.25, 1, 1);
	public static final AxisAlignedBB SIDES_BB_WEST = SIDES_BB_EAST.offset(0.75, 0, 0);
	public static final AxisAlignedBB SIDES_BB_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, 0.25);
	public static final AxisAlignedBB SIDES_BB_NORTH = SIDES_BB_SOUTH.offset(0, 0, 0.75);
	
	public static final PropertyIBlockState STATE = new PropertyIBlockState("STATE");
	public static final PropertyIBlockState ADTL_STATE = new PropertyIBlockState("ADTL_STATE");
	public static final PropertyVec3d SLIDE = new PropertyVec3d("SLIDE");
	
	public ShelvingBlock() {
		super(Material.WOOD);
		setHardness(2.0F);
		
		setRegistryName(new ResourceLocation(BetterWarehouse.MODID, NAME));
        setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty<?>[] { STATE, ADTL_STATE, SLIDE });
    }

	@Override
    public IBlockState getExtendedState(IBlockState origState, IBlockAccess world, BlockPos pos)
    {
    	IExtendedBlockState state = (IExtendedBlockState)origState;
    	ShelvingTile te = ShelvingTile.get(world, pos);
    	if (te != null && te.isLoaded()) {
			state = state.withProperty(STATE, te.getState());
			if(te.getState().getBlock() instanceof BlockFence) {
				int radius = (te.getSize()-1)/2;
				if (te.getOffset().getX() == radius) {
					state = state.withProperty(SLIDE, new Vec3d(0.5, 0, 0));
					ShelvingTile fte = ShelvingTile.get(world, pos.west());
					if (fte != null) {
						state = state.withProperty(ADTL_STATE, fte.getState());
					}
				} else if (te.getOffset().getZ() == radius) {
					state = state.withProperty(SLIDE, new Vec3d(0, 0, 0.5));
					ShelvingTile fte = ShelvingTile.get(world, pos.north());
					if (fte != null) {
						state = state.withProperty(ADTL_STATE, fte.getState());
					}
				} else if (te.getOffset().getX() == -radius) {
					state = state.withProperty(SLIDE, new Vec3d(-0.5, 0, 0));
					ShelvingTile fte = ShelvingTile.get(world, pos.east());
					if (fte != null) {
						state = state.withProperty(ADTL_STATE, fte.getState());
					}
				} else if (te.getOffset().getZ() == -radius) {
					state = state.withProperty(SLIDE, new Vec3d(0, 0, -0.5));
					ShelvingTile fte = ShelvingTile.get(world, pos.south());
					if (fte != null) {
						state = state.withProperty(ADTL_STATE, fte.getState());
					}
				}
			}
    	}
        return state;
    }
	
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new ShelvingTile();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			return false;
		}
		
		ShelvingTile tile = ShelvingTile.get(world, pos);
		if (tile != null && tile.isLoaded()) {
			if (!world.isRemote) {
				player.openGui(BetterWarehouse.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		ShelvingTile tile = ShelvingTile.get(world, pos);
		if (tile != null) {
			tile.onBreak();
		} else {
			super.breakBlock(world, pos, state);
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		ShelvingTile tile = ShelvingTile.get(world, pos);
		if (tile != null) {
			tile.checkCenter();
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TESR Renderer
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		ShelvingTile tile = ShelvingTile.get(world, pos);
		if (tile != null && tile.isLoaded()) {
			state = tile.getState();
			if (state.getBlock() instanceof BlockAir) {
				return AIR_BB;
			}
			if (state.getBlock() instanceof BlockSlab) {
				return SLAB_BB;
			}
			if (state.getBlock() instanceof BlockFence) {
				int radius = (tile.getSize()-1)/2;
				if (tile.getOffset().getX() == radius) {
					return SIDES_BB_WEST;
				} else if (tile.getOffset().getZ() == radius) {
					return SIDES_BB_NORTH;
				} else if (tile.getOffset().getX() == -radius) {
					return SIDES_BB_EAST;
				} else if (tile.getOffset().getZ() == -radius) {
					return SIDES_BB_SOUTH;
				}
			}
		}
		return AIR_BB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getBoundingBox(state, world, pos);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return this.getBoundingBox(state, world, pos);
		
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
