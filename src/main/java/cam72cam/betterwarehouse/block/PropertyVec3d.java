package cam72cam.betterwarehouse.block;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyVec3d implements IUnlistedProperty<Vec3d> {
	private final String name;

	public PropertyVec3d(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Vec3d value) {
		return true;
	}

	@Override
	public Class<Vec3d> getType() {
		return Vec3d.class;
	}

	@Override
	public String valueToString(Vec3d value) {
		return value.toString();
	}
}
