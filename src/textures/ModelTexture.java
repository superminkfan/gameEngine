package textures;

import org.lwjgl.util.vector.Vector3f;

public class ModelTexture {

private int textureID;
private int normalMap;
private int depthMap;
private Vector3f wat;

private float shineDamper = 100;
private float reflectivity = 2;

public boolean hasTransparancey = false;
private boolean useFakeLighting = false;

private  int numberOfRows = 1;

    public int getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(int normalMap) {
        this.normalMap = normalMap;
    }

    public int getDepthMap() {
        return depthMap;
    }

    public void setDepthMap(int depthMap) {
        this.depthMap = depthMap;
    }



    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }



    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public boolean isHasTransparancey() {
        return hasTransparancey;
    }

    public void setHasTransparancey(boolean hasTransparancey) {
        this.hasTransparancey = hasTransparancey;
    }

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
