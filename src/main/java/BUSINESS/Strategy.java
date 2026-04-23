package BUSINESS;

import MODEL.Server;
import MODEL.Task;

import java.util.List;
public interface Strategy {
    public Server addTask(List<Server> servers, Task t);
}
