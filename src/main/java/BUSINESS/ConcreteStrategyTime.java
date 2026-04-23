package BUSINESS;

import MODEL.Server;
import MODEL.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public Server addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        int minTime = Integer.MAX_VALUE;

        for (Server s : servers) {
            if (s.getWaitingPeriod() < minTime) {
                minTime = s.getWaitingPeriod();
                bestServer = s;
            }
        }
        bestServer.addTask(t);
        return bestServer;
    }
}
