package info.u_team.gltf_parser.tests;

import info.u_team.gltf_parser.GlTFParser;
import info.u_team.gltf_parser.TestModelResourceLoader;

public abstract class SimpleCubeGLTFParserTest extends GLTFParserTest {
	
	public static final class Binary extends SimpleCubeGLTFParserTest {
		
		private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_BIN);
		
		@Override
		protected GlTFParser parser() {
			return GlTFParser.fromBinary(simpleCube, 0, simpleCube.length);
		}
	}
	
	public static final class Json extends SimpleCubeGLTFParserTest {
		
		private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_JSON);
		
		@Override
		protected GlTFParser parser() {
			return GlTFParser.fromJson(simpleCube, 0, simpleCube.length);
		}
	}
}
