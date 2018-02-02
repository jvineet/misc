package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GameServer {
    private final List<GameSubscriber> subscribers;
    private int index = 0;

    public GameServer() {
        subscribers = new ArrayList<GameSubscriber>();
    }

    public void subscribe(GameSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(GameSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public GameSubscriber getNextPanel(){
    	index = (index+1)%subscribers.size();
    	GameSubscriber ret = subscribers.get(index);
    	return ret;
    }
    
    public void publish() {
        for (GameSubscriber s : subscribers) {
            s.updateDisplay();
        }
    }
}
