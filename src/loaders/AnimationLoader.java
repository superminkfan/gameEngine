package loaders;

import animation.Animation;
import animation.JointTransform;
import animation.KeyFrame;
import animation.Quaternion;
import colladaLoader.ColladaLoader;
import dataStructures.AnimationData;
import dataStructures.JointTransformData;
import dataStructures.KeyFrameData;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import utils.MyFile;

import java.util.HashMap;
import java.util.Map;


public class AnimationLoader {

	public static Animation loadAnimation(MyFile colladaFile) {
		AnimationData animationData = ColladaLoader.loadColladaAnimation(colladaFile);
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(animationData.lengthSeconds, frames);
	}


	private static KeyFrame createKeyFrame(KeyFrameData data) {
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jointData : data.jointTransforms) {
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}


	private static JointTransform createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}

}
