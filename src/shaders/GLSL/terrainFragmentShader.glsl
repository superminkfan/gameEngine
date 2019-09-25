#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;



out vec4 out_Colour;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour[4];
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
    vec3 unitVectorToCamera = normalize(toCameraVector);



    vec3 totalDifuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    for (int i = 0; i<4; i++)
{
    vec3 unitLightVector = normalize(toLightVector[i]);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float damedFactor = pow(specularFactor, shineDamper);

    totalSpecular = totalSpecular + damedFactor * reflectivity * lightColour[i];
    totalDifuse = totalDifuse + brightness * lightColour[i];
}
    totalDifuse = max(totalDifuse , 0.2);





    out_Colour = vec4(totalDifuse,1.0) * totalColour + vec4(totalSpecular,1.0) ;


    out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);


}