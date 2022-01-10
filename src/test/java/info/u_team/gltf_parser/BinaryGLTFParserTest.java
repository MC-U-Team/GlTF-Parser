package info.u_team.gltf_parser;

public class BinaryGLTFParserTest extends GLTFParserTest {
	
	private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_BIN);
	
	@Override
	protected GLTFParser simpleCubeParser() {
		return GLTFParser.fromBinary(simpleCube, 0, simpleCube.length);
	}
	
}
