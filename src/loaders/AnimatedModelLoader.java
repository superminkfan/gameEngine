package loaders;

import animatedModel.AnimatedModel;
import animatedModel.Joint;
import colladaLoader.ColladaLoader;
import dataStructures.AnimatedModelData;
import dataStructures.JointData;
import dataStructures.MeshData;
import dataStructures.SkeletonData;
import engineTester.GeneralSettings;
import openglObjects.Vao;
import textures.Texture;
import utils.MyFile;

public class AnimatedModelLoader {

	public static AnimatedModel loadEntity(MyFile modelFile, MyFile textureFile) {
		System.out.println("Загружаю модель .dae");
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, GeneralSettings.MAX_WEIGHTS);
		System.out.println("загрузка закончена");
		System.out.println("Загружаю текстуру .png");

		Vao model = createVao(entityData.getMeshData());
		System.out.println("закончил");
		Texture texture = loadTexture(textureFile);
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}


	private static Texture loadTexture(MyFile textureFile) {
		Texture diffuseTexture = Texture.newTexture(textureFile).anisotropic().create();
		return diffuseTexture;
	}


	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}


	private static Vao createVao(MeshData data) {
		Vao vao = Vao.create();
		vao.bind();
		vao.createIndexBuffer(data.getIndices());
		vao.createAttribute(0, data.getVertices(), 3);
		vao.createAttribute(1, data.getTextureCoords(), 2);
		vao.createAttribute(2, data.getNormals(), 3);
		vao.createIntAttribute(3, data.getJointIds(), 3);
		vao.createAttribute(4, data.getVertexWeights(), 3);
		vao.unbind();
		return vao;
	}

}
