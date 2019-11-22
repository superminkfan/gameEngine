#version 400 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

uniform sampler2D dudvMap;

uniform float moveFactor;

const float waveStrength = 0.012f;


void main(void) {

    vec2 ndc = (clipSpace.xy / clipSpace.w)/2.0 + 0.5;
    vec2 reflectCoords = vec2(ndc.x , -ndc.y);
    vec2 refractCoords = vec2(ndc.x , ndc.y);

   /* vec2 distortion1 = (texture(dudvMap , vec2(textureCoords.x + moveFactor , textureCoords.y)).rg * 2.0 - 1.0) * waveStrength;
    vec2 distortion2 = (texture(dudvMap , vec2(textureCoords.x + moveFactor , textureCoords.y + moveFactor)).rg * 2.0 - 1.0) * waveStrenght;
    vec2 totalDistortion = distortion1 + distortion2;*/
    vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg*0.1;
    distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y+moveFactor);
    vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength ;
    //* clamp(waterDepth/20.0, 0.0, 1.0);



    refractCoords += totalDistortion;
    reflectCoords += totalDistortion;


    refractCoords =clamp(refractCoords , 0.001 , 0.999);
    reflectCoords.x = clamp(reflectCoords.x , 0.001 , 0.999);
    reflectCoords.y = clamp(reflectCoords.y , -0.999 , -0.001);


    vec4 reflectColour = texture(reflectionTexture,reflectCoords);
    vec4 refractColour = texture(refractionTexture,refractCoords);

    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector , vec3(0.0,1.0,0.0));
    refractiveFactor = pow(refractiveFactor , 0.55);


    out_Color = mix(reflectColour,refractColour , refractiveFactor);
    out_Color = mix(out_Color,vec4(0.0,0.3,0.5,1.0) , 0.3);



}
