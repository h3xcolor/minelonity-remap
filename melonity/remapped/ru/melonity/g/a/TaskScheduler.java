// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.g.a;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.LinkedList;

@Environment(EnvType.CLIENT)
public class TaskScheduler implements Scheduler {
    private final LinkedList<Task> tasks = new LinkedList<>();

    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }

    @Override
    public void processTasks() {
        tasks.removeAll(tasks.stream()
                .peek(Task::markProcessing)
                .filter(Task::isReadyToRun)
                .peek(this::runTaskSafely)
                .toList());
    }

    private void runTaskSafely(Task task) {
        try {
            task.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

interface Scheduler {
    void addTask(Task task);
    void processTasks();
}

interface Task {
    void markProcessing();
    boolean isReadyToRun();
    void run();
}