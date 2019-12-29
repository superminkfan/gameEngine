package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import utils.SmoothFloat;

public class Camera  implements ICamera{

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 7000f;
    private static final float MAX_PITCH = 90;



    //private float distanceFromPlayer = 0;
    private SmoothFloat distanceFromPlayer = new SmoothFloat(0, 10);
    // private float angleAroundPlayer = 0;
    private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);



    private Vector3f position = new Vector3f(100,40,-20);
   // private float pitch = 0;
    private SmoothFloat pitch = new SmoothFloat(0 , 10);
    private float yaw;
    private float roll;


    private  Player player;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix = new Matrix4f();



    public Camera(Player player){
        this.player = player;
        this.projectionMatrix = createProjectionMatrix();
    }
//***************************************************************************************************

    private static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();
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
        return projectionMatrix;
    }
    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f getProjectionViewMatrix() {
        return Matrix4f.mul(projectionMatrix, viewMatrix, null);
    }

  /*  private void updateViewMatrix() {
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(pitch.get()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f negativeCameraPos = new Vector3f(-position.x, -position.y, -position.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    }*/

//***************************************************************************************************


    public void move(){

        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance , verticalDistance);

        this.yaw = 180 - (player.getRotY() + angleAroundPlayer.get());
        //настраеваем движение типо камеры






    }

    private  void calculateCameraPosition(float horizDistance , float verticalDistance)
    {
        float theta = player.getRotY() + angleAroundPlayer.get();
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance + 10;

    }



    private float calculateHorizontalDistance()
    {
        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
    }

    private float calculateVerticalDistance()
    {
        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
    }



    private void calculateZoom()
    {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer.increaseTarget(-zoomLevel);
        distanceFromPlayer.update(DisplayManager.getFrameTimeSecinds());
    }

    private void calculatePitch()
    {
        if (Mouse.isButtonDown(1))
        {
            float pitchChane = Mouse.getDY() * 0.4f;
            //pitch = pitch - pitchChane;
            //pitch = new SmoothFloat(pitch.get() - pitchChane , 10);
            pitch.increaseTarget(-pitchChane);
            clampPitch();

        }
        pitch.update(DisplayManager.getFrameTimeSecinds());

    }
    private void clampPitch() {
        if (pitch.getTarget() < 0) {
            pitch.setTarget(0);
        } else if (pitch.getTarget() > MAX_PITCH) {
            pitch.setTarget(MAX_PITCH);
        }
    }



    private void calculateAngleAroundPlayer()
    {
        if(Mouse.isButtonDown(0))
        {
            float angleChange = Mouse.getDX() * 0.3f;

            angleAroundPlayer.increaseTarget(-angleChange);
        }
        angleAroundPlayer.update(DisplayManager.getFrameTimeSecinds());
    }


    public void invertPitch()
    {

        //this.pitch = -pitch;
        this.pitch = new SmoothFloat(-pitch.get() , 10);
    }



    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch.get();
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }



}