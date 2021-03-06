package info.u_team.gltf_parser;

import java.util.List;
import java.util.Optional;

public class GlTFUtil {
	
	public static int GLTF_COMPONENT_TYPE_SBYTE = 5120;
	public static int GLTF_COMPONENT_TYPE_UBYTE = 5121;
	public static int GLTF_COMPONENT_TYPE_SSHORT = 5122;
	public static int GLTF_COMPONENT_TYPE_USHORT = 5123;
	public static int GLTF_COMPONENT_TYPE_UINT = 5125;
	public static int GLTF_COMPONENT_TYPE_FLOAT = 5126;
	
	public static boolean equalsOptional(int indexIn, Object indexObj) {
		if(indexObj == null)
			return false;
		final int index = ((Number) indexObj).intValue();
		return index == indexIn;
	}
	
	public static <T> Optional<T> getOptionalEntry(List<T> list, Object indexObj) {
		if(indexObj == null)
			return Optional.empty();
		final int index = ((Number) indexObj).intValue();
		if (index < 0)
			return Optional.empty();
		return Optional.of(list.get(index));
	}
	
}
