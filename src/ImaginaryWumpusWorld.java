import java.util.Hashtable;
import java.util.Set;

public class ImaginaryWumpusWorld {


    private Hashtable<Position, ImaginaryRoom> worldGrid;
    private boolean isWumpusAlive;
    private int wumpusRoomCount;
    private Position wumpusCoords;

    ImaginaryWumpusWorld() {
        worldGrid = new Hashtable<>();
        isWumpusAlive = true;
        wumpusRoomCount = 0;
    }

    public Position getWumpusCoords() {
        int xWumpusCoord = 0;
        int yWumpusCoord = 0;

        Set<Position> keys = worldGrid.keySet();
        for (Position roomPosition : keys) {
            ImaginaryRoom room = worldGrid.get(roomPosition);
            if (room.getWumpus() == NavigatorAgent.ROOM_STATUS_POSSIBLE) {
                xWumpusCoord += roomPosition.getX();
                yWumpusCoord += roomPosition.getY();
            }
        }
        xWumpusCoord /= wumpusRoomCount;
        yWumpusCoord /= wumpusRoomCount;
        this.wumpusCoords = new Position(xWumpusCoord, yWumpusCoord);
        return this.wumpusCoords;
    }

    public Hashtable<Position, ImaginaryRoom> getWorldGrid() {
        return worldGrid;
    }


    public boolean isWumpusAlive() {
        return isWumpusAlive;
    }

    public void setWumpusAlive(boolean wumpusAlive) {
        isWumpusAlive = wumpusAlive;
    }

    public int getWumpusRoomCount() {
        return wumpusRoomCount;
    }

    public void setWumpusRoomCount(int wumpusRoomCount) {
        this.wumpusRoomCount = wumpusRoomCount;
    }
}

class Position {
    private int x;
    private int y;
    Position(){
        this.x = 0;
        this.y = 0;
    }
    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Position position = (Position)obj;
        return this.x == position.getX() && this.y == position.getY();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}