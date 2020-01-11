#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[5];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;


out vec4 out_Colour;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;


uniform vec3 lightColour[5];
uniform vec3 attenuation[5];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;



void main(void)
{

    float objectNearestLight = texture(shadowMap , shadowCoords.xy).r;
    float lightFactor = 1.0;
    if(shadowCoords.z > objectNearestLight)
    {
        lightFactor = 1.0 - (shadowCoords.w * 0.4);
    }


    vec4 blendMapColour = texture(blendMap , pass_textureCoords);
    float backTextureAmount = 1 - (blendMapColour.r  + blendMapColour.g + blendMapColour.b);
    vec2 tilledCoords = pass_textureCoords * 40;
    vec4 backgroundTextureColour = texture(backgroundTexture , tilledCoords) * backTextureAmount;
    vec4 rTextureColour = texture(rTexture , tilledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture , tilledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture , tilledCoords) * blendMapColour.b;
    vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;


    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);



    vec3 totalDifuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    for (int i = 0; i<5; i++)
{
    float distance = length(toLightVector[i]);
    float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

    vec3 unitLightVector = normalize(toLightVector[i]);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float damedFactor = pow(specularFactor, shineDamper);

    totalSpecular = totalSpecular + (damedFactor * reflectivity * lightColour[i]) / attFactor;
    totalDifuse = totalDifuse + brightness * lightColour[i] / attFactor;
}
    totalDifuse = max(totalDifuse , 0.2) * lightFactor;





    out_Colour = vec4(totalDifuse,1.0) * totalColour + vec4(totalSpecular,1.0) ;


    out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);


}