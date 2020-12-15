import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;

import java.util.ArrayList;

class Room {
    ArrayList<String> events = new ArrayList<>();
    Room (int... args){
        for (int i: args){
            events.add(WumpusWorldAgent.roomCodes.get(i));
        }
    }
}

class Coords {
    int row = 0;
    int column = 0;
    Coords(int row, int column){
        this.row = row;
        this.column = column;
    }
}

public class WumpusWorldAgent extends Agent {

    String map="****\nP***\n**G*\n*W**";

    public static String SERVICE_DESCRIPTION = "Wumpus-WORLD";
    private static int START = -1;
    //    private static int EMPTY = 0;
    private static int WUMPUS = 1;
    private static int PIT = 2;
    private static int BREEZE = 3;
    private static int STENCH = 4;
    private static int SCREAM = 5;
    private static int GOLD = 6;
    private static int BUMP = 7;
    public static HashMap<Integer, String> roomCodes = new HashMap<Integer, String>() {{
        put(START, NavigatorAgent.START);
        put(WUMPUS, NavigatorAgent.WUMPUS);
        put(PIT, NavigatorAgent.PIT);
        put(BREEZE, NavigatorAgent.BREEZE);
        put(STENCH, NavigatorAgent.STENCH);
        put(SCREAM, NavigatorAgent.SCREAM);
        put(GOLD, NavigatorAgent.GOLD);
        put(BUMP, NavigatorAgent.BUMP);
    }};
    private static int NUM_OF_ROWS = 4;
    private static int NUM_OF_COLUMNS = 4;

    private Room[][] wumpusMap;
    private HashMap<AID, Coords> Speleologists;

    String nickname = "WumpusWorld";
    AID id = new AID(nickname, AID.ISLOCALNAME);

    @Override
    protected void setup() {
        System.out.println("Hello! WumpusWorld-agent " + getAID().getName() + " is ready.");
        Speleologists = new HashMap<>();
        generateMap();
        showMap();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SpeleologistAgent.WUMPUS_WORLD_TYPE);
        sd.setName(SERVICE_DESCRIPTION);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new SpeleologistConnectPerformer());
        addBehaviour(new SpeleologistArrowPerformer());
        addBehaviour(new SpeleologistGoldPerformer());
        addBehaviour(new SpeleologistMovePerformer());
    }

    private void generateMap() {
        this.wumpusMap = new Room[NUM_OF_ROWS][NUM_OF_COLUMNS];
        this.wumpusMap[0][0] = new Room();
        this.wumpusMap[0][1] = new Room(BREEZE);
        this.wumpusMap[0][2] = new Room(PIT);
        this.wumpusMap[0][3] = new Room(BREEZE);
        this.wumpusMap[1][0] = new Room(STENCH);
        this.wumpusMap[1][1] = new Room(GOLD);
        this.wumpusMap[1][2] = new Room(BREEZE);
        this.wumpusMap[2][0] = new Room(WUMPUS, STENCH);
        this.wumpusMap[2][1] = new Room(BREEZE, STENCH);
        this.wumpusMap[2][2] = new Room(PIT);
        this.wumpusMap[2][3] = new Room(BREEZE);
        this.wumpusMap[3][0] = new Room(STENCH);
        this.wumpusMap[3][2] = new Room(BREEZE);
        this.wumpusMap[3][3] = new Room(PIT);
        for (int i=0; i < this.wumpusMap.length; i++){
            for (int j= 0; j < this.wumpusMap[i].length; j++){
                if (this.wumpusMap[i][j] == null) {
                    this.wumpusMap[i][j] = new Room();
                }
            }

        }

    }
    private void showMap(){
        for (int i = 0; this.wumpusMap.length > i; i++){
            for (int j= 0; j < this.wumpusMap[i].length; j++){
                System.out.println("(МИР) Текущая позиция спелеолога: " + i + ", " + j + "; \n(МИР) В комнате: " + wumpusMap[i][j].events);
            }

        }
    }
    private class SpeleologistConnectPerformer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String message = msg.getContent();
                if (Objects.equals(message, SpeleologistAgent.GO_INSIDE)){
                    AID current_Speleologist = msg.getSender();
                    Coords Speleologist_coords = Speleologists.get(current_Speleologist);
                    if (Speleologist_coords == null){
                        Speleologists.put(current_Speleologist, new Coords(0, 0));
                    }
                    else {
                        Speleologists.put(current_Speleologist, new Coords(0, 0));
                    }
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.CONFIRM);
                    reply.setContent(wumpusMap[0][0].events.toString());
                    myAgent.send(reply);
                }
//
            }
            else {
                block();
            }
        }
    }
    private class SpeleologistArrowPerformer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(SpeleologistAgent.SHOOT_ARROW);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(SpeleologistAgent.SHOOT_ARROW);

                String message = msg.getContent();
                AID current_Speleologist = msg.getSender();
                Coords Speleologist_coords = Speleologists.get(current_Speleologist);

                int row = Speleologist_coords.row;
                int column = Speleologist_coords.column;
                String answer = "";
                if (message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_DOWN))){
                    for (int i = 0; i < row; ++i){
                        if (wumpusMap[i][column].events.contains(WumpusWorldAgent.roomCodes.get(WUMPUS))){
                            answer = NavigatorAgent.SCREAM;
                        }
                    }
                }
                else if(message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_UP))){
                    for (int i = row+1; i < NUM_OF_ROWS; ++i){
                        if (wumpusMap[i][column].events.contains(WumpusWorldAgent.roomCodes.get(WUMPUS))){
                            answer = NavigatorAgent.SCREAM;
                        }
                    }
                }
                else if(message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_LEFT))){
                    for (int i = 0; i < column; ++i){
                        if (wumpusMap[row][i].events.contains(WumpusWorldAgent.roomCodes.get(WUMPUS))){
                            answer = NavigatorAgent.SCREAM;
                        }
                    }
                }
                else if (message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_RIGHT))){
                    for (int i = column+1; i < NUM_OF_COLUMNS; ++i){
                        if (wumpusMap[row][i].events.contains(WumpusWorldAgent.roomCodes.get(WUMPUS))){
                            answer = NavigatorAgent.SCREAM;
                        }
                    }
                }

                reply.setContent(answer);

                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }
    private class SpeleologistMovePerformer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(SpeleologistAgent.MOVE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(SpeleologistAgent.MOVE);

                String message = msg.getContent();
                AID current_Speleologist = msg.getSender();
                Coords Speleologist_coords = Speleologists.get(current_Speleologist);
                System.out.println("(МИР) Перемещаю агента на: " + Speleologist_coords.row + ", " + Speleologist_coords.column);
                if (Speleologist_coords == null){
                    Speleologists.put(current_Speleologist, new Coords(0, 0));
                    Speleologist_coords = Speleologists.get(current_Speleologist);
                }
                int row = Speleologist_coords.row;
                int column = Speleologist_coords.column;
                if (message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_DOWN))){
                    row -= 1;
                }
                else if(message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_UP))){
                    row += 1;
                }
                else if(message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_LEFT))){
                    column -=1;
                }
                else if (message.equals(SpeleologistAgent.actionCodes.get(SpeleologistAgent.LOOK_RIGHT))){
                    column += 1;
                }
                if (row > -1 && column > -1 && row < NUM_OF_ROWS && column < NUM_OF_COLUMNS){
                    Speleologist_coords.column = column;
                    Speleologist_coords.row = row;
                    reply.setContent(wumpusMap[row][column].events.toString());
                }
                else {
                    reply.setContent(String.valueOf(new ArrayList<String>(){{
                        add(NavigatorAgent.BUMP);
                    }}));
                }
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }
    private class SpeleologistGoldPerformer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(SpeleologistAgent.TAKE_GOLD);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String message = msg.getContent();
                AID current_Speleologist = msg.getSender();
                Coords Speleologist_coords = Speleologists.get(current_Speleologist);
                if (Speleologist_coords == null){
                    Speleologists.put(current_Speleologist, new Coords(0, 0));
                }
                else {
                    if (wumpusMap[Speleologist_coords.row][Speleologist_coords.column].events.contains(WumpusWorldAgent.roomCodes.get(GOLD))){
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(SpeleologistAgent.TAKE_GOLD);
                        reply.setContent("GOLD");
                        myAgent.send(reply);
                    }
                }
            }
            else {
                block();
            }
        }
    }
}

