package models;

public class RawModel {
    private int vaoID;
    private int vetexCount;

    public RawModel(int vaoID, int vetexCount) {
        this.vaoID = vaoID;
        this.vetexCount = vetexCount;
    }

    public int getVaoID() {
        return vaoID;
    }



    public int getVertexCount() {
        return vetexCount;
    }


}
