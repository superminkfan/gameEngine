package renderEngine.rendererAnim;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.*;
import toolBox.Maths;
import utils.MyFile;

import java.util.List;

public class AnimatedModelShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 4;

	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
	private static final int DIFFUSE_TEX_UNIT = 0;

	private static final MyFile VERTEX_SHADER = new MyFile("C:/Users/SEM/IdeaProjects/NewTryGL/src/renderEngine/rendererAnim/animatedEntityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("C:/Users/SEM/IdeaProjects/NewTryGL/src/renderEngine/rendererAnim/animatedEntityFragment.glsl");


	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuation[];
	private int location_skyColour;
	private int location_plane;


	//вот тут********************************************************************************


	protected UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", MAX_JOINTS);
	private UniformSampler diffuseMap = new UniformSampler("diffuseMap");


	public AnimatedModelShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords", "in_normal", "in_jointIndices",
				"in_weights");
		super.storeAllUniformLocations( diffuseMap, jointTransforms, transformationMatrix, projectionMatrix, viewMatrix);

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];

		for (int i = 0 ; i< MAX_LIGHTS ; i++)
		{
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_plane = super.getUniformLocation("plane");
		location_skyColour = super.getUniformLocation("skyColour");

		connectTextureUnits();
	}


	private void connectTextureUnits() {
		super.start();
		diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}

	@Override
	protected void getAllUniformLocations() {

	}

	@Override
	protected void bindAttributes() {

	}

	public void loadViewMatrix(Camera camera)
	{
		Matrix4f vm = Maths.createViewMatrix(camera);
		this.viewMatrix.loadMatrix(vm);
	}



	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());

			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));//АЛЁРТ    ТУТ    КОСТЫЛЬ!!!
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));//ЗАПОЛНЯЕМ НУЛЯМ ЧТОБ СРОСЛОСЬ!!!!!!!!
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadSkuColourVariable(float r , float g , float b)
	{
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	public void loadClipPlane(Vector4f plane)
	{
		super.loadVector(location_plane,plane);
	}

}
