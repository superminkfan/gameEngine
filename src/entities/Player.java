package entities;

import entities.animatedModel.AnimatedModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

import java.util.logging.SocketHandler;

public class Player extends Entity {

    private static final float RUN_SPEED = 50;
    private static final float TURN_SPEED = 100;
    private static final float GRAVITY = -60;
    private static final float JUMP_POWER = 60;
    private static final float TERRAIN_HEIGHT = 0;



    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;




    //нужен новый контруктор чтоб с анимейтид модел

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);





    }

    public Player(AnimatedModel model) {
        super(model);

    }

    public Player(AnimatedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);

    }


    public void move(Terrain terrain)
    {
        checkInputs();
        //System.out.println("move rot y" +currentTurnSpeed * DisplayManager.getFrameTimeSecinds() );
        super.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSecinds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSecinds();

        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));


        super.increasePosition(dx,0,dz);


        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSecinds();
        super.increasePosition(0,upwardsSpeed*DisplayManager.getFrameTimeSecinds() , 0);


        float terreinHeight = terrain.getHeightOfTerrain(super.getPosition().x , super.getPosition().z);//опасно!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (super.getPosition().y < terreinHeight)
            //здесь будет проверка на
            // соприкосновение с землёй
            //terrainCollisionEfeect
        {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terreinHeight;
        }

    }


    public void move(Terrain terrain,AnimatedModel animatedModel)//АПРИОРИ КОСТЫЛЬНЫЙ МЕТОД!!!!!!!!!!!!!
    {
        checkInputs();
        //System.out.println("move rot y" +currentTurnSpeed * DisplayManager.getFrameTimeSecinds() );
        animatedModel.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSecinds(), 0);
        super.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSecinds(), 0);

        float distance = currentSpeed * DisplayManager.getFrameTimeSecinds();

        float dx = (float) (distance * Math.sin(Math.toRadians(animatedModel.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(animatedModel.getRotY())));


        animatedModel.increasePosition(dx,0,dz);
        super.increasePosition(dx,0,dz);



        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSecinds();
        animatedModel.increasePosition(0,upwardsSpeed*DisplayManager.getFrameTimeSecinds() , 0);
        super.increasePosition(0,upwardsSpeed*DisplayManager.getFrameTimeSecinds() , 0);



        float terreinHeight = terrain.getHeightOfTerrain(animatedModel.getPosition().x , animatedModel.getPosition().z);//опасно!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (animatedModel.getPosition().y < terreinHeight)
        //здесь будет проверка на
        // соприкосновение с землёй
        //terrainCollisionEfeect
        {
            upwardsSpeed = 0;
            isInAir = false;
            animatedModel.getPosition().y = terreinHeight;
            super.getPosition().y = terreinHeight;

        }

    }

    private void jump()
    {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }

    }

    private void checkInputs()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            this.currentSpeed = RUN_SPEED;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            this.currentSpeed = -RUN_SPEED;

        }else
            {
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.currentTurnSpeed = -TURN_SPEED;

        }else if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            this.currentTurnSpeed = TURN_SPEED;


        }else
        {
            this.currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            jump();

        }

    }

}
