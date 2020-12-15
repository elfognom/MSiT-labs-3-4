class ImaginaryRoom {
    private int exist;
    private int stench;
    private int breeze;
    private int pit;
    private int wumpus;
    private int ok;
    private int gold;
    private int noWay;

    public ImaginaryRoom() {
        this.exist = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.stench = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.breeze = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.pit = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.wumpus = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.ok = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.gold = NavigatorAgent.ROOM_STATUS_NO_STATUS;
        this.noWay = NavigatorAgent.ROOM_STATUS_NO_STATUS;
    }
    public void addEvent(String event_name){
        switch (event_name){
            case NavigatorAgent.START:
                break;
            case NavigatorAgent.WUMPUS:
                this.setWumpus(NavigatorAgent.ROOM_STATUS_TRUE);
                break;
            case NavigatorAgent.PIT:
                this.setPit(NavigatorAgent.ROOM_STATUS_TRUE);
                break;
            case NavigatorAgent.BREEZE:
                this.setBreeze(NavigatorAgent.ROOM_STATUS_TRUE);
                break;
            case NavigatorAgent.STENCH:
                this.setStench(NavigatorAgent.ROOM_STATUS_TRUE);
                break;
            case NavigatorAgent.SCREAM:
                break;
            case NavigatorAgent.GOLD:
                this.setGold(NavigatorAgent.ROOM_STATUS_TRUE);
                break;
            case NavigatorAgent.BUMP:
                break;
        }
    }

    public int getExist() {
        return exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }

    public int getStench() {
        return stench;
    }

    public void setStench(int stench) {
        this.stench = stench;
    }

    public int getBreeze() {
        return breeze;
    }

    public void setBreeze(int breeze) {
        this.breeze = breeze;
    }

    public int getPit() {
        return pit;
    }

    public void setPit(int pit) {
        this.pit = pit;
    }

    public int getWumpus() {
        return wumpus;
    }

    public void setWumpus(int wumpus) {
        this.wumpus = wumpus;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getNoWay() {
        return noWay;
    }

    public void setNoWay(int noWay) {
        this.noWay = noWay;
    }

}
