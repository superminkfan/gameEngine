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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

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
       // GuiTexture gui1 = new GuiTexture(loader.loadTexture("blow") , new Vector2f(0.5f,0.5f) , new Vector2f(0.25f,0.25f));
        //GuiTexture gui1 = new GuiTexture(loader.loadTexture("heightMap") , new Vector2f(0.4f,0.4f) , new Vector2f(0.25f,0.25f));
       // guis.add(gui1);
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
                new Vector3f(370,0,-270),0,0,0,13);
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
//-----------------------------------LAMP-----------------------------------------------

        ModelData lampData = OBJFileLoader.loadOBJ("lamp");

        RawModel lampModel = loader.loadToVAO(
                lampData.getVertices(),
                lampData.getTextureCoords() ,
                lampData.getNormals(),
                lampData.getIndices()
        );

        TexturedModel lampTexture = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));



//---------------------------------------------------------

        List<Terrain> terrains = new ArrayList<>();
        Terrain terrain = new Terrain(0,-1,loader , texturePack , blendMap , "heightmap");
        terrains.add(terrain);
        //Terrain terrain2 = new Terrain(-1,-1,loader , texturePack , blendMap , "heightmap");

        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer(loader);

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=LIGHTS==+=+=+=+=++==++=+=+=+=+=+=+=+=+
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,800,-3000),new Vector3f(1f,1f,1f)));//это типо солнце
        lights.add(new Light(new Vector3f(185,10,-293) , new Vector3f(4,0,0) , new Vector3f(1,0.01f,0.002f)));//а вот это фонари
        lights.add(new Light(new Vector3f(370,terrain.getHeightOfTerrain(370,-300) + 8,-300) , new Vector3f(0,4,4), new Vector3f(1,0.01f,0.002f)));
       // lights.add(new Light(new Vector3f(293,11,-305) , new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f)));

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=+=+=+=+=+=+=+=+=++==++=+=+=+=+=+=+=+=+


 //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity(lampTexture , new Vector3f(185 , terrain.getHeightOfTerrain(185,-293) , -293) , 0,0,0,1));
        entities.add(new Entity(lampTexture , new Vector3f(370 , terrain.getHeightOfTerrain(370,-300) , -300) , 0,0,0,1));
        entities.add(new Entity(lampTexture , new Vector3f(293 ,terrain.getHeightOfTerrain(293,-305)  , -305) , 0,0,0,1));
        entities.add(new Entity(lampTexture , new Vector3f(185 , terrain.getHeightOfTerrain(185,-293) , -293) , 0,0,0,1));

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        Random random = new Random();
       /* for(int i = 0 ; i < 50 ; i++)
        {
            float x = random.nextFloat()*800 -400;
            float z = random.nextFloat()* -600 ;
            float y = terrain.getHeightOfTerrain(x,z) + 4;
            entities.add(new Entity(houseTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,2));
        }*/

        entities.add(player);
        entities.add(player);//==================КОСТЫЛЬ=====АЛЁРТ=====



        for(int i=0 ; i <100 ; i++)
        {
            float x = random.nextFloat()*500;
            float z = random.nextFloat()* -500 ;
            float y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(fernTexture,random.nextInt(4) ,
                    new Vector3f(x,y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*1.5f));


             /*x = random.nextFloat()*800;
             z = random.nextFloat()* -800 ;
             y = terrain.getHeightOfTerrain(x,z)+5;
            entities.add(new Entity(boxTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,5));*/



            x = random.nextFloat()*800;
            z = random.nextFloat()* -800;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(grassTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,1));

            x = random.nextFloat()*800;
            z = random.nextFloat()* -800;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(flowerTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,1));
           /* x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(treeLowPoly_1_Texture,
                    new Vector3f(x,y,z)
                    ,0,random.nextFloat() *500,0,random.nextFloat()*2));*/

            x = random.nextFloat()*800;
            z = random.nextFloat()* -800;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(pineTexture,
                    new Vector3f(x, y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*2));

        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




//=====================================================
        //играемся с мышью
        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix() , terrain);

        //Entity lampEnt = (new Entity(lampTexture , new Vector3f(293,-6.8f , -305) , 0 , 0, 0, 1));
       // entities.add(lampEnt);
        Light lightLamp = (new Light( new Vector3f(293,-6.8f , -305) , new Vector3f(0,2,2) , new Vector3f(1 , 0.01f, 0.002f)));
        lights.add(lightLamp);


//=====================================================

        //играемся с водой

        WaterFrameBuffers fbos = new WaterFrameBuffers();

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader , renderer.getProjectionMatrix() , fbos);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile(400,-400,-5);
        waters.add(water);

/*        GuiTexture refraction = new GuiTexture(fbos.getRefractionTexture() , new Vector2f(0.5f,0.5f) , new Vector2f(0.25f,0.25f));
        GuiTexture refrection = new GuiTexture(fbos.getReflectionTexture() , new Vector2f(-0.5f,0.5f) , new Vector2f(0.25f,0.25f));
        guis.add(refraction);
        guis.add(refrection);*/




//=====================================================

        while(!Display.isCloseRequested()){
            player.move(terrain);
            camera.move();
            picker.update();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);


//------------------------------------------------
            //reflection
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();



            renderer.renderScene(entities , terrains , lights , camera , new Vector4f(0,1,0, -water.getHeight()));


            camera.getPosition().y += distance;
            camera.invertPitch();


            //refration
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities , terrains , lights , camera , new Vector4f(0,-1,0, water.getHeight()));



            //just render enth els
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(entities , terrains , lights , camera , new Vector4f(0,-1,0,100000));
            waterRenderer.render(waters , camera);
            guiRenderer.render(guis);




            /*Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if(terrainPoint!=null)
            {
                lampEnt.setPosition(terrainPoint);
                lightLamp.setPosition(new Vector3f(terrainPoint.x , terrainPoint.y +5 , terrainPoint.z));
            }*/


            DisplayManager.updateDisplay();


        }
        fbos.cleanUp();
        guiRenderer.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
