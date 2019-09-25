#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;



out vec4 out_Colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;


void main(void)
{
    //все векторы должны быть нормализованными
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDifuse = vec3(0.0);
    vec3 totalSpecural = vec3(0.0);

    for (int i = 0; i<4; i++)
    {
    vec3 unitLightVector = normalize(toLightVector[i]);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 lightDirection = -unitLightVector;//потому что он направлен в другую сторону
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);//считаем освещение спектральное
    specularFactor = max(specularFactor, 0.0);
    float damedFactor = pow(specularFactor, shineDamper);
        totalDifuse = totalDifuse + brightness * lightColour[i];//диффузное осфещение
        totalSpecural = totalSpecural + damedFactor * reflectivity * lightColour[i];
    }

    totalDifuse = max(totalDifuse , 0.2);
    vec4 textureColour = texture(textureSampler,pass_textureCoords);

    if(textureColour.a < 0.5)
    {
        discard;
    }


    out_Colour = vec4(totalDifuse,1.0) * textureColour + vec4(totalSpecural,1.0) ;


    out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);

}