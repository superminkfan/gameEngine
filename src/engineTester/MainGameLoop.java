package engineTester;

import animation.Animation;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.animatedModel.AnimatedModel;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import gui.GuiRenderer;
import gui.GuiTexture;
import loaders.AnimatedModelLoader;
import loaders.AnimationLoader;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.MousePicker;
import utils.MyFile;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {


    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        //************************************************************************************





        //************************************************************************************


        List<Terrain> terrains = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<AnimatedModel> animEntities = new ArrayList<>();
        List<Light> lights = new ArrayList<>();
        List<GuiTexture> guis = new ArrayList<>();
        List<WaterTile> waters = new ArrayList<>();
        List<Entity> normalMapEntities = new ArrayList<>();
        //-----------------------------------
        //тестики




//==============================PLAYER=====================================
        ModelData watData = OBJFileLoader.loadOBJ("wat");

        RawModel watModel = loader.loadToVAO(
                watData.getVertices(),
                watData.getTextureCoords() ,
                watData.getNormals(),
                watData.getIndices()
        );

        TexturedModel watTexture = new TexturedModel(watModel,new ModelTexture(loader.loadTexture("white")));


      //  Player player1 = new Player(watTexture, new Vector3f(400,0,-400),0,0,0,13);


        AnimatedModel entity = AnimatedModelLoader.
                loadEntity(new MyFile(GeneralSettings.RES_FOLDER, GeneralSettings.MODEL_FILE),
                        new MyFile(GeneralSettings.RES_FOLDER, GeneralSettings.DIFFUSE_FILE));
        Animation animation = AnimationLoader.
                loadAnimation(new MyFile(GeneralSettings.RES_FOLDER, GeneralSettings.ANIM_FILE));
        entity.doAnimation(animation);
        //ВОТ ТУТ  НУЖНО ПОДУМАТЬ КАК ОСТАНАВЛИВАТЬ АНИМАЦИЮ

        AnimatedModel animatedModel = new AnimatedModel(entity,new Vector3f(400,0,-400) ,
                0f,0f,0f,2f);



        Player player = new Player(animatedModel);

        Camera camera = new Camera(player);

        animEntities.add(animatedModel);
        animEntities.add(animatedModel);

      // entities.add(player);
       //entities.add(player);//==================КОСТЫЛЬ=====АЛЁРТ=====

        MasterRenderer renderer = new MasterRenderer(loader , camera);

//========================================================================================

        //===============================TESTS===============================





        // GuiTexture gui1 = new GuiTexture(loader.loadTexture("blow") , new Vector2f(0.5f,0.5f) , new Vector2f(0.25f,0.25f));
        //GuiTexture gui1 = new GuiTexture(loader.loadTexture("heightMap") , new Vector2f(0.4f,0.4f) , new Vector2f(0.25f,0.25f));
        // guis.add(gui1);
        //guis.add(gui1);

        GuiRenderer guiRenderer = new GuiRenderer(loader);



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//=============================END==TESTS==============================




//====================================TERRAINS========================================

       // TerrainTexture backgraondTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture backgraondTexture = new TerrainTexture(loader.loadTexture("sand2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("sand1"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path1"));


        TerrainTexturePack texturePack = new TerrainTexturePack(backgraondTexture,rTexture,gTexture,bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0,-1,loader , texturePack , blendMap , "heightMap");
        terrains.add(terrain);


//==============================ENTITIES=========================================================================

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

        ModelData boxData = OBJFileLoader.loadOBJ("wall2");

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




//-----------------------------------LAMP-----------------------------------------------

        ModelData lampData = OBJFileLoader.loadOBJ("lamp");

        RawModel lampModel = loader.loadToVAO(
                lampData.getVertices(),
                lampData.getTextureCoords() ,
                lampData.getNormals(),
                lampData.getIndices()
        );

        TexturedModel lampTexture = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));



//-------------------------------TOWER---------------------------------

        ModelData towerData = OBJFileLoader.loadOBJ("woodTower3");


        RawModel towerModel = loader.loadToVAO(
                towerData.getVertices(),
                towerData.getTextureCoords() ,
                towerData.getNormals(),
                towerData.getIndices()
        );

        TexturedModel towerTexture = new TexturedModel(towerModel,new ModelTexture(loader.loadTexture("woodTowerTex1")));
        towerTexture.getTexture().setHasTransparancey(true);


        //-------------------------------WELL---------------------------------

        ModelData wellData = OBJFileLoader.loadOBJ("well10");


        RawModel wellModel = loader.loadToVAO(
                wellData.getVertices(),
                wellData.getTextureCoords() ,
                wellData.getNormals(),
                wellData.getIndices()
        );

        TexturedModel wellTexture = new TexturedModel(wellModel,new ModelTexture(loader.loadTexture("white")));
        wellTexture.getTexture().setReflectivity(10);




        //-------------------------------House---------------------------------

        ModelData watHouseData = OBJFileLoader.loadOBJ("watHouse10");


        RawModel watHouseModel = loader.loadToVAO(
                watHouseData.getVertices(),
                watHouseData.getTextureCoords() ,
                watHouseData.getNormals(),
                watHouseData.getIndices()
        );

        TexturedModel watHouseTe = new TexturedModel(watHouseModel,new ModelTexture(loader.loadTexture("watHouseTex")));
        watHouseTe.getTexture().setHasTransparancey(true);
//---------------------------------------------------------




//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=LIGHTS==+=+=+=+=++==++=+=+=+=+=+=+=+=+
        //lights.add(new Light(new Vector3f(0,1200,-3000),new Vector3f(1f,1f,1f)));//это типо солнце
        Light sun = new Light(new Vector3f(1000000,1500000,-100000),new Vector3f(1f,1f,1f));//это типо солнце
        lights.add(sun);
        lights.add(new Light(new Vector3f(185,10,-293) , new Vector3f(4,0,0) , new Vector3f(1,0.01f,0.002f)));//а вот это фонари
        lights.add(new Light(new Vector3f(370,terrain.getHeightOfTerrain(370,-300) + 8,-300) , new Vector3f(0,4,4), new Vector3f(1,0.01f,0.002f)));
        lights.add(new Light(new Vector3f(293,11,-305) , new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f)));

//+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=++=+=+=+=+=+=+=+=+=+=++==++=+=+=+=+=+=+=+=+


 //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        entities.add(new Entity(treeLowPoly_1_Texture , new Vector3f(185 , terrain.getHeightOfTerrain(185,-293) , -293) ,
                0,0,0,4));
        entities.add(new Entity(treeLowPoly_1_Texture , new Vector3f(185 , terrain.getHeightOfTerrain(185,-293) , -293) ,
                0,0,0,4));
        entities.add(new Entity(lampTexture , new Vector3f(370 , terrain.getHeightOfTerrain(370,-300) , -300) ,
                0,0,0,3));
        entities.add(new Entity(lampTexture , new Vector3f(293 ,terrain.getHeightOfTerrain(293,-305)  , -305) ,
                0,0,0,4));
        entities.add(new Entity(lampTexture , new Vector3f(185 , terrain.getHeightOfTerrain(185,-293) , -293) ,
                0,0,0,4));

        Entity tower = new Entity(towerTexture , new Vector3f(403 , terrain.getHeightOfTerrain(403,-245) - 7 , -245) ,
                0,0,0,13);
        entities.add(tower);
        entities.add(tower);

        entities.add(new Entity(wellTexture , new Vector3f(350 , terrain.getHeightOfTerrain(350,-250)  , -250) ,
                0,0,0,0.8F));
                entities.add(new Entity(wellTexture , new Vector3f(350 , terrain.getHeightOfTerrain(350,-250) , -250) ,
                        0,0,0,0.8f));

        Entity watHouse = new Entity( watHouseTe,
                new Vector3f(350 , terrain.getHeightOfTerrain(350,-390) -26 , -390) ,
                0,0,0,17);
        entities.add(watHouse);
        entities.add(watHouse);

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
        TexturedModel pineNormal  = new TexturedModel(NormalMappedObjLoader.loadOBJ("pine" , loader),
                new ModelTexture(loader.loadTexture("pine")));
        pineNormal.getTexture().setShineDamper(2);
        pineNormal.getTexture().setReflectivity(0.1f);

        pineNormal.getTexture().setNormalMap(loader.loadTexture("pineNormal"));


        for(int i=0 ; i <80 ; i++)
        {
           float x = random.nextFloat()*1000;
            float z = random.nextFloat()* -1000 ;
            float y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(fernTexture,random.nextInt(4) ,
                    new Vector3f(x,y,z),
                    0,random.nextFloat() *500,0,random.nextFloat()*3f));






            x = random.nextFloat()*1000;
            z = random.nextFloat()* -1000;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(grassTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,2));

            x = random.nextFloat()*1000;
            z = random.nextFloat()* -1000;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(flowerTexture,
                    new Vector3f(x, y,z)
                    ,0,random.nextFloat() *500,0,3));
            x = random.nextFloat()*800 -400;
            z = random.nextFloat()* -600;
            y = terrain.getHeightOfTerrain(x,z);

            entities.add(new Entity(treeLowPoly_1_Texture,
                    new Vector3f(x,y,z)
                    ,0,random.nextFloat() *500,0,random.nextFloat()*2));

            x = random.nextFloat()*1000;
            z = random.nextFloat()* -1000;
            y = terrain.getHeightOfTerrain(x,z);
            float s = random.nextFloat()*4;
            Entity pineN = new Entity(pineNormal , new Vector3f(x,y,z)  ,
                    0,s * 500,0,s*1.01f);

            normalMapEntities.add(pineN);
            normalMapEntities.add(pineN);

            entities.add(new Entity(pineTexture, new Vector3f(x+10, y,z),
                    0,s *500,0,s));

        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




//=====================================================
        //играемся с мышью
        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix() , terrain);

        Entity lampEnt = (new Entity(lampTexture , new Vector3f(293,-6.8f , -305) , 0 , 0, 0, 1));
       entities.add(lampEnt);
        Light lightLamp = (new Light( new Vector3f(293,-6.8f , -305) , new Vector3f(1,2,2) , new Vector3f(0.01f , 0.01f, 0.01f)));
        lights.add(lightLamp);


//=====================================================

        //играемся с водой

        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader , renderer.getProjectionMatrix() , fbos);
        WaterTile water = new WaterTile(800,-800,-3);
        waters.add(water);

        //ТЕНИ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture() , new Vector2f(0.5f,0.5f) , new Vector2f(0.5f,0.5f));
       // guis.add(shadowMap);






//=====================================================
        //NORMAL MAPPING ENTETIES
        TexturedModel barrelModel  = new TexturedModel(NormalMappedObjLoader.loadOBJ("sphere1" , loader),
                new ModelTexture(loader.loadTexture("bricks2")));
        barrelModel.getTexture().setShineDamper(3);
        barrelModel.getTexture().setReflectivity(0.7f);

        barrelModel.getTexture().setNormalMap(loader.loadTexture("bricks2_normal"));//wat3
        barrelModel.getTexture().setDepthMap(loader.loadTexture("bricks2_disp"));


        Entity barrel = new Entity(barrelModel , new Vector3f(400,terrain.getHeightOfTerrain(400,-400)+35,-400) ,
                0f,0f,0f,5f);
        normalMapEntities.add(barrel);
        normalMapEntities.add(barrel);
//=====================================================
        TexturedModel towerNorModel  = new TexturedModel(NormalMappedObjLoader.loadOBJ("woodTower3" , loader),
                new ModelTexture(loader.loadTexture("woodTowerTex1")));
        towerNorModel.getTexture().setShineDamper(10);
        towerNorModel.getTexture().setReflectivity(0.5f);

        towerNorModel.getTexture().setNormalMap(loader.loadTexture("woodTowerTex2"));
        Entity towerNormal = new Entity(towerNorModel , new Vector3f(453,terrain.getHeightOfTerrain(453,-245)-5,-245)  ,
                0,0,0,15f);
        normalMapEntities.add(towerNormal);
        normalMapEntities.add(towerNormal);

//=========================
        TexturedModel wall  = new TexturedModel(NormalMappedObjLoader.loadOBJ("wall4" , loader),
                new ModelTexture(loader.loadTexture("walTex")));
        wall.getTexture().setShineDamper(10);
        wall.getTexture().setReflectivity(0.1f);

        wall.getTexture().setNormalMap(loader.loadTexture("wallNormal"));
        Entity wallNormal = new Entity(wall , new Vector3f(300,terrain.getHeightOfTerrain(300,-500)-3,-500)  ,
                0,50,0,15f);
        normalMapEntities.add(wallNormal);
        normalMapEntities.add(wallNormal);
//
//==================================



//-*-*-*-**-*-*-*-*-*--*-*-*--*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*--*-*
        //TEXT RENDERING//*****************************************

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadFontTextureAtlas("arial") , new File("res/arial.fnt"));
        GUIText text = new GUIText("this is a test text" , 10 , font , new Vector2f(0.5f,0.5f) , 0.5f , true);



//-*-*-*-**-*-*-*-*-*--*-*-*--*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*--*-*








        //===============================TESTS===============================

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        AnimatedModel animatedModel1= new AnimatedModel(entity,new Vector3f(300,terrain.getHeightOfTerrain(300 , -400) + 2,-400) ,
                0f,0f,0f,2f);
        animEntities.add(animatedModel1);



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        while(!Display.isCloseRequested()){


            player.move(terrain ,animatedModel);//если не анимированная модель то без плеера
            camera.move();
           // picker.update();


           // renderer.renderShadowMap(entities,sun);
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);



            entity.update();
            if (Keyboard.isKeyDown(Keyboard.KEY_Q))
            {
                entity.test(animation);
            }
            /*
            Вот тут нужно придумать оооочень интересную хрень
            Вообщем нужно чтоб когда позиция не менялась то есть игрок останавливаля то
            анимация ПЛАВНО переходила в начальную позицию - руки вниз и ноги вместе
            Но как
             */



//------------------------------------------------
            //ОТРАЖЕННЫЙ

            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();



            renderer.renderScene(entities ,normalMapEntities, animEntities ,terrains , lights , camera , new Vector4f(0,1,0,
                    -water.getHeight()+1f));


            camera.getPosition().y += distance;
            camera.invertPitch();


            //ПРЕЛАМЛЁННЫЙ
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities ,normalMapEntities, animEntities, terrains , lights , camera , new Vector4f(0,-1,0, water.getHeight()));




            //ВСЁ ОСТАЛЬНОЕ
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(entities ,normalMapEntities, animEntities, terrains , lights , camera , new Vector4f(0,-1,0,100000));




            waterRenderer.render(waters , camera , sun);
            guiRenderer.render(guis);
            TextMaster.render();




            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if(terrainPoint!=null)
            {
                lampEnt.setPosition(terrainPoint);
                lightLamp.setPosition(new Vector3f(terrainPoint.x , terrainPoint.y +5 , terrainPoint.z));
            }


            DisplayManager.updateDisplay();


        }
        fbos.cleanUp();

        guiRenderer.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        TextMaster.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
