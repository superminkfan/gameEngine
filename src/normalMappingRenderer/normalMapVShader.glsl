#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoordinates;

out vec3 toLightVector[5];
out vec3 toCameraVector;
out float visibility;
out vec3 pass_tangent;
out vec3 surfaceNormal;


out vec3 viewPos;
out vec3 fragPos;



uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositionEyeSpace[5];

uniform float numberOfRows;
uniform vec2 offset;

const float density = 0;
const float gradient = 5.0;

uniform vec4 plane;
uniform vec3 test;


void main(void){

    pass_tangent = tangent;//удоли потом
    vec4 worldPosition = transformationMatrix * vec4(position,1.0);
    gl_ClipDistance[0] = dot(worldPosition, plane);
    mat4 modelViewMatrix = viewMatrix * transformationMatrix;
    vec4 positionRelativeToCam = modelViewMatrix * vec4(position,1.0);//fragPos
    gl_Position = projectionMatrix * positionRelativeToCam;

    pass_textureCoordinates = (textureCoordinates/numberOfRows) + offset;
    surfaceNormal = (modelViewMatrix * vec4(normal,0.0)).xyz;


    vec3 norm = normalize(surfaceNormal);
    vec3 tang = normalize((modelViewMatrix * vec4(tangent , 0.0)).xyz);
    vec3 bitang = normalize(cross(norm , tang));

    mat3 toTangentSpace = mat3 (
    tang.x , bitang.x , norm.x,
    tang.y , bitang.y , norm.y,
    tang.z , bitang.z , norm.z
    );

    for(int i=0;i<5;i++){
        toLightVector[i] = toTangentSpace * (lightPositionEyeSpace[i] - positionRelativeToCam.xyz);
    }

    toCameraVector = toTangentSpace* (-positionRelativeToCam.xyz);

//***********************************************************
    vec3 watViewPos = normalize(test);
    vec3 watFragPos =  vec3(modelViewMatrix * vec4(position, 1.0));

    mat4 model = inverse(viewMatrix) * transformationMatrix;
    //vec3 watViewPos = normalize(test);
    //vec3 watFragPos =  vec3(positionRelativeToCam);


    fragPos =   toTangentSpace * watFragPos;
    viewPos =   toTangentSpace * watViewPos;
//***********************************************************
    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance*density),gradient));
    visibility = clamp(visibility,0.0,1.0);

}