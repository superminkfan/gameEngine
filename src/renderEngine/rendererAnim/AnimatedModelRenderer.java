package renderEngine.rendererAnim;

import entities.animatedModel.AnimatedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import toolBox.Maths;
import utils.OpenGlUtils;

import java.util.List;


public class AnimatedModelRenderer {

	private AnimatedModelShader shader;


	public AnimatedModelRenderer(AnimatedModelShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}


	public void render(List<AnimatedModel> animEntities) {//пока так

	for(AnimatedModel entity : animEntities) {
		entity.getTexture().bindToUnit(0);
		entity.getModel().bind(0, 1, 2, 3, 4);
		shader.jointTransforms.loadMatrixArray(entity.getJointTransforms());
		shader.transformationMatrix.loadMatrix(Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(),
				entity.getRotZ(), entity.getScale()));


		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		entity.getModel().unbind(0, 1, 2, 3, 4);
	}
		OpenGlUtils.antialias(true);
		OpenGlUtils.disableBlending();
		OpenGlUtils.enableDepthTesting(true);
	}




}
