package info.u_team.gltf_parser.tests;

import info.u_team.gltf_parser.GlTFParser;
import info.u_team.gltf_parser.TestModelResourceLoader;

public abstract class SimpleCubeGlTFParserTest extends GlTFParserTest {
	
	public static final class Binary extends SimpleCubeGlTFParserTest {
		
		private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_BIN);
		
		@Override
		protected GlTFParser parser() {
			return GlTFParser.fromBinary(simpleCube, 0, simpleCube.length);
		}
	}
	
	public static final class Json extends SimpleCubeGlTFParserTest {
		
		private final byte[] simpleCube = TestModelResourceLoader.readModel(TestModelResourceLoader.SIMPLE_CUBE_JSON);
		
		@Override
		protected GlTFParser parser() {
			return GlTFParser.fromJson(simpleCube, 0, simpleCube.length);
		}
	}
}
