#version 400 core

const vec2 lightBias = vec2(0.7, 0.6);//just indicates the balance between diffuse and ambient lighting

in vec2 pass_textureCoords;
in vec3 pass_normal;

in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Colour;

uniform sampler2D diffuseMap;
uniform vec3 lightDirection;


uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
const float shineDamper = 20;
const float reflectivity = 0.7f;
uniform vec3 skyColour;

void main(void){



	
	/*vec4 diffuseColour = texture(diffuseMap, pass_textureCoords);
	vec3 unitNormal = normalize(pass_normal);
	float diffuseLight = max(dot(-lightDirection, unitNormal), 0.0) * lightBias.x + lightBias.y;
	out_colour = diffuseColour * diffuseLight;*/
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	vec3 totalDifuse = vec3(0.0);
	vec3 totalSpecural = vec3(0.0);

	for (int i = 0; i<4; i++)
	{
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 lightDirection = -unitLightVector;//потому что он направлен в другую сторону
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);//считаем освещение спектральное
		specularFactor = max(specularFactor, 0.0);
		float damedFactor = pow(specularFactor, shineDamper);
		totalDifuse = totalDifuse + (brightness * lightColour[i]) / attFactor;//диффузное осфещение
		totalSpecural = totalSpecural + (damedFactor * reflectivity * lightColour[i]) / attFactor;
	}

	totalDifuse = max(totalDifuse , 0.2);
	vec4 textureColour = texture(diffuseMap,pass_textureCoords);

	if(textureColour.a < 0.5)
	{
		discard;
	}


	out_Colour = vec4(totalDifuse,1.0) * textureColour + vec4(totalSpecural,1.0) ;


	out_Colour = mix(vec4(skyColour,1.0) , out_Colour , visibility);
	
}