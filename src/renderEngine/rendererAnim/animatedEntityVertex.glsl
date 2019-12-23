#version 400 core

const int MAX_JOINTS = 50;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 3;//max number of joints that can affect a vertex

in vec3 in_position;
in vec2 in_textureCoords;
in vec3 in_normal;
in ivec3 in_jointIndices;
in vec3 in_weights;

out vec2 pass_textureCoords;
out vec3 pass_normal;
out vec3 surfaceNormal;
out vec3 toLightVector[4];//четыре потому что если будет блдьше то не потянет
//4 источника света могут влиять на основной цвет
out vec3 toCameraVector;
out float visibility;




uniform mat4 jointTransforms[MAX_JOINTS];
uniform mat4 projectionViewMatrix;//бесполезная



uniform vec3 lightPosition[4];//new
uniform vec4 plane ;

const float density = 0;
const float gradient = 5;



uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){


	vec4 worldPosition = transformationMatrix * vec4(in_position.x , in_position.y , in_position.z , 1.0);
	vec4 positionRelativeToCam = viewMatrix  * worldPosition;
	//gl_Position = projectionMatrix * positionRelativeToCam;
	gl_ClipDistance[0] = dot(worldPosition,plane);
	for(int i = 0 ; i<4 ; i++)
	{
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}

	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density) , gradient));//считаем туман

	visibility = clamp(visibility, 0.0 , 1.0);

	vec4 totalLocalPos = vec4(0,-5,0,5);
	vec4 totalNormal = vec4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = jointTransforms[in_jointIndices[i]];
		vec4 posePosition = jointTransform * vec4(in_position, 1.0);
		totalLocalPos += posePosition * in_weights[i];
		
		vec4 worldNormal = jointTransform * vec4(in_normal, 0.0);
		totalNormal += worldNormal * in_weights[i];
	}
	//нужно добавить нормальную проекционную
	//видовю и позицию отностистельно камеры а так же расположение сущности
	//gl_Position = projectionViewMatrix * totalLocalPos;
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * totalLocalPos;


	surfaceNormal =  (transformationMatrix * vec4(in_normal,0.0)).xyz;
	//surfaceNormal =  totalNormal.xyz;

	pass_normal = totalNormal.xyz;
	pass_textureCoords = in_textureCoords;

}