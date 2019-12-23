package renderEngine.rendererAnim;

import animatedModel.AnimatedModel;
import entities.Camera;
import entities.ICamera;
import entities.Light;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.MasterRenderer;
import toolBox.Maths;
import utils.OpenGlUtils;

import java.util.List;


public class AnimatedModelRenderer {

	private AnimatedModelShader shader;


	public AnimatedModelRenderer() {
		this.shader = new AnimatedModelShader();
	}


	public void render(AnimatedModel entity, Camera camera, Vector3f lightDir , List<Light> lights , Vector4f clipPlane) {
		prepare(camera, lightDir , lights ,clipPlane);
		entity.getTexture().bindToUnit(0);
		entity.getModel().bind(0, 1, 2, 3, 4);
		shader.jointTransforms.loadMatrixArray(entity.getJointTransforms());
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		entity.getModel().unbind(0, 1, 2, 3, 4);
		finish();
	}


	public void cleanUp() {
		shader.cleanUp();
	}


	private void prepare(Camera camera, Vector3f lightDir ,  List<Light> lights , Vector4f clipPlane) {
		shader.start();

		shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
//*********************************************************************************************
		//блядство
		shader.projectionMatrix.loadMatrix(Maths.createProjectionMatrix());
		shader.transformationMatrix.loadMatrix(Maths.createTransformationMatrix(new Vector3f(400,10,-400) ,
				0f,0f,0f,10f));
		shader.viewMatrix.loadMatrix(Maths.createViewMatrix(camera));
		shader.loadLights(lights);
		shader.loadClipPlane(clipPlane);
		shader.loadSkuColourVariable(MasterRenderer.RED,MasterRenderer.GREEN, MasterRenderer.BLUE);
//*********************************************************************************************

		shader.lightDirection.loadVec3(lightDir);
		OpenGlUtils.antialias(true);
		OpenGlUtils.disableBlending();
		OpenGlUtils.enableDepthTesting(true);
	}


	private void finish() {
		shader.stop();
	}

}
