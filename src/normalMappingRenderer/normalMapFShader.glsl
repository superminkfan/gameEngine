#version 400 core

in vec2 pass_textureCoordinates;
in vec3 toLightVector[5];
in vec3 toCameraVector;
in float visibility;
in vec3 pass_tangent;
in vec3 surfaceNormal;




in vec3 viewPos;
in vec3 fragPos;



out vec4 out_Color;

uniform sampler2D modelTexture;
uniform sampler2D normalMap;

uniform sampler2D depthMap;

uniform vec3 lightColour[5];
uniform vec3 attenuation[5];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir);
float height_scale = 0.05;


void main(void){
    vec3 unitVectorToCamera = normalize(toCameraVector);




    //***********************************************************************

     vec2 textureCoords = pass_textureCoordinates;
     vec3 viewDir =  viewPos - fragPos;
    // vec2 textureCoords = ParallaxMapping(pass_textureCoordinates , normalize(viewDir));
     vec4 normalMapValue = 2.0 * texture(normalMap , textureCoords) - 1;
    // vec3 unitNormal = normalize(normalMapValue.rgb);
vec3 unitNormal = normalize(surfaceNormal);

    //***********************************************************************



    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i=0;i<5;i++){
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDotl = dot(unitNormal,unitLightVector);
        float brightness = max(nDotl,0.0);
        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
        float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);

        specularFactor = max(specularFactor,0.0);
        float dampedFactor = pow(specularFactor,shineDamper);
        totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attFactor;
        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i])/attFactor;

        //!!
       // totalSpecular = vec3(0.0);
    }


    totalDiffuse = max(totalDiffuse, 0.2);

    //vec4 textureColour = texture(modelTexture,pass_textureCoordinates);
    vec4 textureColour = texture(modelTexture,textureCoords);
    if(textureColour.a<0.5){
        discard;
    }

    out_Color =  vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
    out_Color = mix(vec4(skyColour,1.0),out_Color, visibility);
   // out_Color = normalMapValue;
  // out_Color = vec4(pass_tangent, 1.0);

}



vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir)
{
    // количество слоев глубины
    const float numLayers = 50;
    // размер каждого слоя
    float layerDepth = 1.0 / numLayers;
    // глубина текущего слоя
    float currentLayerDepth = 0.0;
    // величина шага смещения текстурных координат на каждом слое
    // расчитывается на основе вектора P
    vec2 P = viewDir.xy * height_scale;
    vec2 deltaTexCoords = P / numLayers;



    // начальная инициализация
    vec2  currentTexCoords     = texCoords;
    float currentDepthMapValue = texture(depthMap, currentTexCoords).r;

    while(currentLayerDepth < currentDepthMapValue)
    {
        // смещаем текстурные координаты вдоль вектора P
        currentTexCoords -= deltaTexCoords;
        // делаем выборку из карты глубин в текущих текстурных координатах
        currentDepthMapValue = texture(depthMap, currentTexCoords).r;
        // рассчитываем глубину следующего слоя
        currentLayerDepth += layerDepth;
    }



    // код реализации Relief PM
    // ======

    // уполовиниваем смещение текстурных координат и размер слоя глубины
    deltaTexCoords *= 0.5;
    layerDepth *= 0.5;
    // сместимся в обратном направлении от точки, найденной в Steep PM
    currentTexCoords += deltaTexCoords;
    currentLayerDepth -= layerDepth;

    // установим максимум итераций поиска…
    const int _reliefSteps = 10;
    int currentStep = _reliefSteps;
    while (currentStep > 0) {
        currentDepthMapValue = texture(depthMap, currentTexCoords).r;
        deltaTexCoords *= 0.5;
        layerDepth *= 0.5;
        // если выборка глубины больше текущей глубины слоя,
        // то уходим в левую половину интервала
        if (currentDepthMapValue > currentLayerDepth) {
            currentTexCoords -= deltaTexCoords;
            currentLayerDepth += layerDepth;
        }
        // иначе уходим в правую половину интервала
        else {
            currentTexCoords += deltaTexCoords;
            currentLayerDepth -= layerDepth;
        }
        currentStep--;
    }

     //lastDepthValue = currentDepthValue;


    return currentTexCoords;
}