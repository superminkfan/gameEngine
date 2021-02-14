package entities.animatedModel;

import animation.Animation;
import animation.Animator;
import openglObjects.Vao;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
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
	private  Vao model;
	private  Texture texture;

	// skeleton
	private  Joint rootJoint;
	private  int jointCount;

	private  Animator animator;

	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;


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

	public AnimatedModel(AnimatedModel animModel,
						 Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = animModel.getModel();
		this.texture = animModel.getTexture();
		this.rootJoint = animModel.getRootJoint();
		this.jointCount = animModel.getJointCount();
		this.animator = animModel.getAnimator();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public  void increasePosition(float dx, float dy , float dz)
	{
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	public void increaseRotation (float dx, float dy , float dz)
	{
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}


	public Vao getModel() {
		return model;
	}

	public int getJointCount() {
		return jointCount;
	}

	public Animator getAnimator() {
		return animator;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
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


	public void update() {//вот тут сделай чтоб обрабатывался список
		animator.update();
	}

	public void test(Animation animation)
	{
		doAnimation(animation);
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
