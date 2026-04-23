package BUSINESS;

import MODEL.Server;
import MODEL.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public Server addTask(List<Server> servers, Task t) {
        Server bestServer = null;
        int minSize = Integer.MAX_VALUE;

        for (Server s : servers) {
            if (s.getTasks().size() < minSize) {
                minSize = s.getTasks().size();
                bestServer = s;
            }
        }

        bestServer.addTask(t);
        return bestServer;
    }
}
