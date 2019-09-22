package textures;

public class ModelTexture {

private int textureID;
private float shineDamper = 10;
private float reflectivity = 0.5f;

private boolean hasTransparancey = false;
private boolean useFakeLighting = false;

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