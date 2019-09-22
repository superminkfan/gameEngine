#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;



out vec4 out_Colour;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;



void main(void)
{
    vec4 blendMapColour = texture(blendMap , pass_textureCoords);

    float backTextureAmount = 1 - (blendMapColour.r  + blendMapColour.g + blendMapColour.b);

    vec2 tilledCoords = pass_textureCoords * 40;

    vec4 backgroundTextureColour = texture(backgroundTexture , tilledCoords) * backTextureAmount;

    vec4 rTextureColour = texture(rTexture , tilledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture , tilledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture , tilledCoords) * blendMapColour.b;


    vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness*lightColour;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);


    float specularFactor = dot(reflectedLightDirection,unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float damedFactor = pow(specularFactor , shineDamper);
    vec3 finalSpecular = damedFactor * reflectivity * lightColour;



    out_Colour = vec4(diffuse,1.0) * totalColour + vec4(finalSpecular,1.0) ;


    out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);


}