package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
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


        TerrainTexture backgraondTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path1"));


        TerrainTexturePack texturePack = new TerrainTexturePack(backgraondTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap2"));




//==============================здесь текстуры и модель игрока=====================================
        ModelData watData = OBJFileLoader.loadOBJ("wat");

        RawModel watModel = loader.loadToVAO(
                watData.getVertices(),
                watData.getTextureCoords() ,
                watData.getNormals(),
                watData.getIndices()
        );

        TexturedModel watTexture = new TexturedModel(watModel,new ModelTexture(loader.loadTexture("box")));


        Player player = new Player(watTexture,
                new Vector3f(100,0,-50),0,0,0,15);
//========================================================================================
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

        ModelData boxData = OBJFileLoader.loadOBJ("dom");

        RawModel boxModel = loader.loadToVAO(
                boxData.getVertices(),
                boxData.getTextureCoords() ,
                boxData.getNormals(),
                boxData.getIndices()
        );


//-----------------------------------------------------------------

        ModelData pineData = OBJFileLoader.loadOBJ("pine");


        RawModel pineModel = loader.loadToVAO(
                pineData.getVertices(),
                pineData.getTextureCoords() ,
                pineData.getNormals(),
                pineData.getIndices()
        );
//-----------------------------------------------------------------

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fernTexture = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
        fernTexture.getTexture().setHasTransparancey(true);

//-----------------------------------------------------------------

        TexturedModel boxTexture = new TexturedModel(boxModel,new ModelTexture(loader.loadTexture("box")));

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

        TexturedModel pineTexture = new TexturedModel(pineModel,new ModelTexture(loader.loadTexture("pine")));
        pineTexture.getTexture().setHasTransparancey(true);


//-----------------------------------------------------------------




        //---------------------------------------------

        //---------------------------------------------


        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));


        Terrain terrain = new Terrain(0,-1,loader , texturePack , blendMap , "heightmap");
        Terrain terrain2 = new Terrain(-1,-1,loader , texturePack , blendMap , "heightmap");

        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer();

        //+++++++++++++++++++++++++++++++++++
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i = 0 ; i < 50 ; i++)
        {
            float x = random.nextFloat()*800 -400;
            float z = random.nextFloat()* -600  +  7;
            float y = terrain.getHeightOfTerrain(x,z);
            entities.add(new Entity(boxTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,2));
        }

        for(int i=0 ; i <100 ; i++)
        {
            float x = random.nextFloat()*800 -400;
            float z = random.nextFloat()* -600  +  7;
            float y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(fernTexture,random.nextInt(4) ,
                    new Vector3f(x,y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*1.5f));

            x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(grassTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,1));

            x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(flowerTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,1));
            x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(treeLowPoly_1_Texture,
                    new Vector3f(x,y,z)
                    ,0,random.nextFloat() *500,0,random.nextFloat()*2));

            x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(pineTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*2));

        }
        entities.add(player);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++






        while(!Display.isCloseRequested()){
            camera.move();

            player.move(terrain);
            renderer.processEntity(player);

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
