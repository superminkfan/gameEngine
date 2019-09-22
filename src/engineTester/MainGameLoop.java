package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {


    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        Loader loader = new Loader();


        //terreans pac


        TerrainTexture backgraondTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));


        TerrainTexturePack texturePack = new TerrainTexturePack(backgraondTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));


//-----------------------------------------------------------------

        ModelData fernData = OBJFileLoader.loadOBJ("fern");

        RawModel fernModel = loader.loadToVAO(
                fernData.getVertices(),
                fernData.getTextureCoords() ,
                fernData.getNormals(),
                fernData.getIndices()
                );
//-----------------------------------------------------------------

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");

        RawModel grassModel = loader.loadToVAO(
                grassData.getVertices(),
                grassData.getTextureCoords() ,
                grassData.getNormals(),
                grassData.getIndices()
        );

//-----------------------------------------------------------------

        ModelData lowPolyTreeData_1 = OBJFileLoader.loadOBJ("lowPolyTree");

        RawModel treeLowPolyModel_1 = loader.loadToVAO(
                lowPolyTreeData_1.getVertices(),
                lowPolyTreeData_1.getTextureCoords() ,
                lowPolyTreeData_1.getNormals(),
                lowPolyTreeData_1.getIndices()
        );

//-----------------------------------------------------------------

        ModelData testData = OBJFileLoader.loadOBJ("pine");


        RawModel testModel = loader.loadToVAO(
                testData.getVertices(),
                testData.getTextureCoords() ,
                testData.getNormals(),
                testData.getIndices()
        );
//-----------------------------------------------------------------
        TexturedModel fernTexture = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
        fernTexture.getTexture().setHasTransparancey(true);

//-----------------------------------------------------------------

        TexturedModel flowerTexture = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("flower")));
        flowerTexture.getTexture().setHasTransparancey(true);
        flowerTexture.getTexture().setUseFakeLighting(true);

        TexturedModel grassTexture = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grassTexture")));
        grassTexture.getTexture().setHasTransparancey(true);
        grassTexture.getTexture().setUseFakeLighting(true);

//-----------------------------------------------------------------



        TexturedModel treeLowPoly_1_Texture = new TexturedModel(treeLowPolyModel_1,new ModelTexture(loader.loadTexture("lowPolyTree")));


        //-----------------------------------------------------------------

        TexturedModel testTexture = new TexturedModel(testModel,new ModelTexture(loader.loadTexture("pine")));
        testTexture.getTexture().setHasTransparancey(true);
       

//-----------------------------------------------------------------


        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<100;i++){
            entities.add(new Entity(fernTexture,
                    new Vector3f(random.nextFloat()*800 - 400,
                    0,random.nextFloat() * -600),
                    0,random.nextFloat() *500,0,1));

            entities.add(new Entity(grassTexture,
                    new Vector3f(random.nextFloat()*800 - 400,
                            0,random.nextFloat() * -600)
                    ,0,random.nextFloat() *500,0,1));

            entities.add(new Entity(flowerTexture,
                    new Vector3f(random.nextFloat()*800 - 400,
                            0,random.nextFloat() * -600)
                    ,0,random.nextFloat() *500,0,1));

            entities.add(new Entity(treeLowPoly_1_Texture,
                    new Vector3f(random.nextFloat()*800 - 400
                            ,0,random.nextFloat() * -600)
                    ,0,random.nextFloat() *500,0,1f));

            entities.add(new Entity(testTexture,
                    new Vector3f(random.nextFloat()*800 - 400,
                            0,random.nextFloat() * -600),
                    0,random.nextFloat() *500,0,1f));


        }
        //---------------------------------------------
        entities.add(new Entity(testTexture,
                new Vector3f(0,0,-200),
                0,random.nextFloat() *500,0,1f));
        //---------------------------------------------


        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));


        Terrain terrain = new Terrain(0,-1,loader , texturePack , blendMap);
        Terrain terrain2 = new Terrain(-1,-1,loader , texturePack , blendMap);

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()){
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
                //entity.increaseRotation(0.0f, 0.2f, 0.0f);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
