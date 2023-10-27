package com.colak.hzcli.commands.executor;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.executor.LocalExecutorStats;
import com.hazelcast.executor.impl.ExecutorServiceProxy;
import com.hazelcast.spi.impl.executionservice.ExecutionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@ShellComponent
public class HzExecutorStatsCommand extends AbstractCommand {

    @ShellMethod(key = "e.stats", prefix = "-", value = "List all executor stats")
    void showExecutorStats() {
        List<String> executorNamesList = List.of(
                ExecutionService.SYSTEM_EXECUTOR,
                ExecutionService.ASYNC_EXECUTOR,
                ExecutionService.SCHEDULED_EXECUTOR,
                ExecutionService.CLIENT_EXECUTOR,
                ExecutionService.CLIENT_QUERY_EXECUTOR,
                ExecutionService.CLIENT_BLOCKING_EXECUTOR,
                ExecutionService.QUERY_EXECUTOR,
                ExecutionService.IO_EXECUTOR,
                ExecutionService.OFFLOADABLE_EXECUTOR,
                ExecutionService.MAP_STORE_OFFLOADABLE_EXECUTOR,
                ExecutionService.MAP_TIERED_STORE_COMPACTION_OFFLOADABLE_EXECUTOR,
                ExecutionService.MAP_LOADER_EXECUTOR,
                ExecutionService.MAP_LOAD_ALL_KEYS_EXECUTOR,
                ExecutionService.MC_EXECUTOR,
                ExecutionService.JOB_OFFLOADABLE_EXECUTOR
        );

        List<LocalExecutorStats> localExecutorStatsList = getLocalExecutorStats(executorNamesList);
        embedInTable(
                new String[]{"name", "stats"},
                builder -> IntStream.range(0, executorNamesList.size())
                        .forEach(index -> {
                            String executorName = executorNamesList.get(index);
                            LocalExecutorStats localExecutorStats = localExecutorStatsList.get(index);
                            builder.addRow();
                            builder.addValue(executorName);
                            builder.addValue(Objects.toString(localExecutorStats));
                        }));
    }

    List<LocalExecutorStats> getLocalExecutorStats(List<String> executorNamesList) {
        List<LocalExecutorStats> result = new ArrayList<>();

        for (String executorName : executorNamesList) {
            IExecutorService executorService = hazelcastClient.getExecutorService(executorName);
            if (executorService instanceof ExecutorServiceProxy) {
                result.add(executorService.getLocalExecutorStats());
            } else {
                result.add(null);
            }

        }
        return result;
    }
}
