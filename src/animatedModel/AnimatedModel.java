package animatedModel;

import animation.Animation;
import animation.Animator;
import openglObjects.Vao;
import org.lwjgl.util.vector.Matrix4f;
import textures.Texture;

/**
 *
 * Этот класс представляет сущность в мире, которая может быть анимирована. Он
 * содержит VAO модели, которая содержит данные меша, текстуру и
 * корневой сустав иерархии суставов или «скелет». Он также содержит int, который
 * представляет количество соединений, которые содержит скелет модели, и имеет
 * его собственный экземпляр {@link Animator}, который можно использовать для применения анимации к
 * этой сущности.
 *
 */
public class AnimatedModel {

	// skin
	private final Vao model;
	private final Texture texture;

	// skeleton
	private final Joint rootJoint;
	private final int jointCount;

	private final Animator animator;

	/**

	 * Создаёт новую сущность способную к анимации. Inverse bind transform
	 * для всех суставов считается в этом конструкторе. Bind transform это
	 * просто оригинальная
	 *
	 * 
	 *
	 * 
	 */
	public AnimatedModel(Vao model, Texture texture, Joint rootJoint, int jointCount) {
		this.model = model;
		this.texture = texture;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		this.animator = new Animator(this);
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}


	public Vao getModel() {
		return model;
	}



	public Texture getTexture() {
		return texture;
	}


	public Joint getRootJoint() {
		return rootJoint;
	}


	public void delete() {
		model.delete();
		texture.delete();
	}


	public void doAnimation(Animation animation) {
		animator.doAnimation(animation);
	}


	public void update() {
		animator.update();
	}


	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}


	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

}
