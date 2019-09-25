#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;



out vec4 out_Colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;


void main(void)
{
//все векторы должны быть нормализованными
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness*lightColour;//диффузное осфещение

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;//потому что он направлен в другую сторону
    vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);


    float specularFactor = dot(reflectedLightDirection,unitVectorToCamera);//считаем освещение спектральное
    specularFactor = max(specularFactor, 0.0);
    float damedFactor = pow(specularFactor , shineDamper);
    vec3 finalSpecular = damedFactor * reflectivity * lightColour;

    vec4 textureColour = texture(textureSampler,pass_textureCoords);

    if(textureColour.a < 0.5)
    {
        discard;
    }


    out_Colour = vec4(diffuse,1.0) * textureColour + vec4(finalSpecular,1.0) ;


    out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);

}