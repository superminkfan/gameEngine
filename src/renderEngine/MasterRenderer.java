package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.animatedModel.AnimatedModel;
import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.rendererAnim.AnimatedModelRenderer;
import renderEngine.rendererAnim.AnimatedModelShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    public static final float FOV = 50;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 7000f;

    public static final float RED = 0.3f;
    public static final float GREEN = 0.6f;
    public static final float BLUE = 0.3f;


    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();



    private NormalMappingRenderer normalMapRenderer;



    private AnimatedModelShader animShader = new AnimatedModelShader();
    private AnimatedModelRenderer animRenderer;



    private Map<TexturedModel, List<Entity>> entities =
            new HashMap<TexturedModel, List<Entity>>();


    //вот тут новый лист аним ентетис
    private List<AnimatedModel> animEntities = new ArrayList<>();

    private Map<TexturedModel, List<Entity>> normalMapEntities =
            new HashMap<TexturedModel, List<Entity>>();

    private List<Terrain> terrains = new ArrayList<>();


    private SkyboxRenderer skyboxRenderer;
    private ShadowMapMasterRenderer shadowMapMasterRenderer;








    public MasterRenderer(Loader loader , Camera camera) {

        enableCulling();

        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader,projectionMatrix);
        normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
        animRenderer = new AnimatedModelRenderer(animShader , projectionMatrix);
        this.shadowMapMasterRenderer = new ShadowMapMasterRenderer(camera);

    }

    public static void enableCulling()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);//чтоб внутренняя сторона не рендерелась
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void  disableCulling()
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }


    public Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }


    public void renderScene(List<Entity> entities , List<Entity> normalEntities, List<AnimatedModel> animEnt ,
                            List<Terrain> terrains , List<Light> lights ,
                            Camera camera , Vector4f clipPlane)
    {
        for(Terrain terrain:terrains){
            processTerrain(terrain);
        }

        for(Entity entity:entities){
            processEntity(entity);
        }



        for(Entity entity : normalEntities)
        {
            processNormalMapEntity(entity);
        }
        this.animEntities=animEnt;

        //вот тут добавь просесс аним ентети
        render(lights,camera,clipPlane);

    }



    public void render (List<Light> lights , Camera camera , Vector4f clipPlane)
    {
        prepare();
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkuColourVariable(RED, GREEN , BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        normalMapRenderer.render(normalMapEntities,clipPlane,lights,camera);


        //*********************************
        //гдето здесь шейдер анимаций его подготовка и загрузка параметров
        animShader.start();

        animShader.loadClipPlane(clipPlane);
        animShader.loadSkuColourVariable(RED, GREEN , BLUE);
        animShader.loadLights(lights);
        animShader.loadViewMatrix(camera);
        animRenderer.render(animEntities);//костыль!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        animShader.stop();
        //*********************************

        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkuColourVariable(RED, GREEN , BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains,shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
        terrainShader.stop();

        skyboxRenderer.render(camera ,RED, GREEN , BLUE );
        terrains.clear();
        entities.clear();
        normalMapEntities.clear();


    }


    public void processTerrain(Terrain terrain)
    {
        terrains.add(terrain);
    }


    //вот тут просес аним ентети

    public void processEntity(Entity entity)
    {
        TexturedModel entityModel = entity.getModel();
        List<Entity>  batch = entities.get(entityModel);
        if(batch != null)
        {
            batch.add(entity);
        }
        else {
            List<Entity> newBatch = new ArrayList<>();
            entities.put(entityModel,newBatch);
        }
    }




    public void processNormalMapEntity(Entity entity)
    {
        TexturedModel entityModel = entity.getModel();
        List<Entity>  batch = normalMapEntities.get(entityModel);
        if(batch != null)
        {
            batch.add(entity);
        }
        else {
            List<Entity> newBatch = new ArrayList<>();
            normalMapEntities.put(entityModel,newBatch);
        }
    }

    public void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(RED, GREEN,BLUE,1);//цвет дисполея
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D , getShadowMapTexture());
    }

    public void createProjectionMatrix()
    {
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }


    public void renderShadowMap(List<Entity> entityList, Light sun)
    {
       for(Entity entity: entityList)
         {
            processEntity(entity);
         }
         shadowMapMasterRenderer.render(entities,sun);
       entities.clear();
    }

    public int getShadowMapTexture()
    {
        return shadowMapMasterRenderer.getShadowMap();
    }

    public void cleanUp()
    {
        shader.cleanUp();
        animShader.cleanUp();
        terrainShader.cleanUp();
        normalMapRenderer.cleanUp();
        shadowMapMasterRenderer.cleanUp();
    }

}
