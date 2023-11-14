package com.colak.hzcli.commands.jet;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.jet.JetService;
import com.hazelcast.jet.Job;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
public class HzJobsCommand extends AbstractCommand {

    @ShellMethod(key = "jet.jobs", value = "List all Jet jobs")
    void showExecutorStats() {
        JetService jetService = hazelcastClient.getJet();
        List<Job> jobs = jetService.getJobs();
        embedInTable(
                new String[]{"name", "light job", "id", "status", "submission time" , "job config"},
                builder -> jobs
                        .forEach(job -> {
                            builder.addRow();
                            builder.addValue(job.getName());
                            builder.addValue(job.isLightJob());
                            builder.addValue(job.getIdString());
                            builder.addValue(job.getStatus());

                            String formattedDateTime = formatInstant(job.getSubmissionTime());
                            builder.addValue(formattedDateTime);

                            builder.addValue(job.getConfig());
                        }));
    }

    private String formatInstant(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        // Convert Instant to LocalDateTime using a system time zone
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Define the desired format pattern
        String pattern = "yyyy-MM-dd HH:mm:ss";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Format LocalDateTime using the formatter
        return localDateTime.format(formatter);
    }
}
