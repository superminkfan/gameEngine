#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

uniform float numberOfRows;
uniform vec2 offset;




const float density = 0.002;
const float gradient = 8;


void main (void)
{
    vec4 worldPosition = transformationMatrix * vec4(position.x , position.y , position.z , 1.0);

    vec4 positionRelativeToCam = viewMatrix  * worldPosition;

    gl_Position =projectionMatrix* positionRelativeToCam;
    pass_textureCoords = (textureCoords/numberOfRows) + offset;

    vec3 actualNormal = normal;
    if(useFakeLighting > 0.5)
    {
        actualNormal = vec3(0.0,1.0,0.0); //тупо вверх
        //пришлось сделать обманку для цветов так как у них нормали в разные стороны
    }

    surfaceNormal =  (transformationMatrix * vec4(normal,0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance*density) , gradient));//считаем туман

    visibility = clamp(visibility, 0.0 , 1.0);
}