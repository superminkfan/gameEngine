package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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

//===============================TESTS===============================

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("blow") , new Vector2f(0.5f,0.5f) , new Vector2f(0.25f,0.25f));
        //GuiTexture gui1 = new GuiTexture(loader.loadTexture("heightMap") , new Vector2f(0.4f,0.4f) , new Vector2f(0.25f,0.25f));
        guis.add(gui);
        //guis.add(gui1);

        GuiRenderer guiRenderer = new GuiRenderer(loader);



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//=============================END==TESTS==============================




//====================================TERRAINS========================================

        TerrainTexture backgraondTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path1"));


        TerrainTexturePack texturePack = new TerrainTexturePack(backgraondTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));




//==============================PLAYER=====================================
        ModelData watData = OBJFileLoader.loadOBJ("wat");

        RawModel watModel = loader.loadToVAO(
                watData.getVertices(),
                watData.getTextureCoords() ,
                watData.getNormals(),
                watData.getIndices()
        );

        TexturedModel watTexture = new TexturedModel(watModel,new ModelTexture(loader.loadTexture("white")));


        Player player = new Player(watTexture,
                new Vector3f(100,0,-50),0,0,0,15);
//========================================================================================
//--------------------------------FERN---------------------------------

        ModelData fernData = OBJFileLoader.loadOBJ("fern");

        RawModel fernModel = loader.loadToVAO(
                fernData.getVertices(),
                fernData.getTextureCoords() ,
                fernData.getNormals(),
                fernData.getIndices()
                );

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fernTexture = new TexturedModel(OBJLoader.loadObjFile("fern" , loader) , fernTextureAtlas);
        fernTexture.getTexture().setHasTransparancey(true);

//--------------------------------GRAASS--AND--FLOWERS-----------------------------

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");

        RawModel grassModel = loader.loadToVAO(
                grassData.getVertices(),
                grassData.getTextureCoords() ,
                grassData.getNormals(),
                grassData.getIndices()
        );

        TexturedModel flowerTexture = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("flower")));
        flowerTexture.getTexture().setHasTransparancey(true);
        flowerTexture.getTexture().setUseFakeLighting(true);

        TexturedModel grassTexture = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grassTexture")));
        grassTexture.getTexture().setHasTransparancey(true);
        grassTexture.getTexture().setUseFakeLighting(true);

//---------------------------------LOW--POLY--TREE----------------------------

        ModelData lowPolyTreeData_1 = OBJFileLoader.loadOBJ("lowPolyTree");

        RawModel treeLowPolyModel_1 = loader.loadToVAO(
                lowPolyTreeData_1.getVertices(),
                lowPolyTreeData_1.getTextureCoords() ,
                lowPolyTreeData_1.getNormals(),
                lowPolyTreeData_1.getIndices()
        );

        TexturedModel treeLowPoly_1_Texture = new TexturedModel(treeLowPolyModel_1,new ModelTexture(loader.loadTexture("lowPolyTree")));


//-------------------------------BOX----------------------------------

        ModelData boxData = OBJFileLoader.loadOBJ("box");

        RawModel boxModel = loader.loadToVAO(
                boxData.getVertices(),
                boxData.getTextureCoords() ,
                boxData.getNormals(),
                boxData.getIndices()
        );

        TexturedModel boxTexture = new TexturedModel(boxModel,new ModelTexture(loader.loadTexture("box")));



//-------------------------------PINE---------------------------------

        ModelData pineData = OBJFileLoader.loadOBJ("pine");


        RawModel pineModel = loader.loadToVAO(
                pineData.getVertices(),
                pineData.getTextureCoords() ,
                pineData.getNormals(),
                pineData.getIndices()
        );

        TexturedModel pineTexture = new TexturedModel(pineModel,new ModelTexture(loader.loadTexture("pine")));
        pineTexture.getTexture().setHasTransparancey(true);


//---------------------------HOUSE---------------------------------

        ModelData houseData = OBJFileLoader.loadOBJ("dom");

        RawModel houseModel = loader.loadToVAO(
                houseData.getVertices(),
                houseData.getTextureCoords() ,
                houseData.getNormals(),
                houseData.getIndices()
        );

        TexturedModel houseTexture = new TexturedModel(houseModel,new ModelTexture(loader.loadTexture("water")));

   //================================================

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=LIGHTS==+=+=+=+=++==++=+=+=+=+=+=+=+=+
        Light light = new Light(new Vector3f(0,10000,-7000),new Vector3f(1,1,1));
        List<Light> lights = new ArrayList<>();
        lights.add(light);
        lights.add(new Light(new Vector3f(-200,10,-200) , new Vector3f(10,0,0)));
        lights.add(new Light(new Vector3f(200,10,200) , new Vector3f(0,0,10)));

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=+=+=+=+=+=+=+=+=++==++=+=+=+=+=+=+=+=+

        Terrain terrain = new Terrain(0,-1,loader , texturePack , blendMap , "heightmap");
        Terrain terrain2 = new Terrain(-1,-1,loader , texturePack , blendMap , "heightmap");

        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer();


        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i = 0 ; i < 50 ; i++)
        {
            float x = random.nextFloat()*800 -400;
            float z = random.nextFloat()* -600 ;
            float y = terrain.getHeightOfTerrain(x,z) + 4;
            entities.add(new Entity(houseTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,2));
        }

        for(int i=0 ; i <100 ; i++)
        {
            float x = random.nextFloat()*800 -400;
            float z = random.nextFloat()* -600 ;
            float y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(fernTexture,random.nextInt(4) ,
                    new Vector3f(x,y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*1.5f));


             x = random.nextFloat()*800 -400;
             z = random.nextFloat()* -600 ;
             y = terrain.getHeightOfTerrain(x,z)+5;
            entities.add(new Entity(boxTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,5));



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
            renderer.render(lights, camera);
           // guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
       // guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
