package com.colak.hzcli.commands.jet;

import com.colak.hzcli.commands.AbstractCommand;
import com.hazelcast.jet.JetService;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.impl.JetClientInstanceImpl;
import com.hazelcast.jet.impl.SubmitJobParameters;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
@Slf4j
public class HzJobsCommand extends AbstractCommand {

    @ShellMethod(key = "jet.jobs", value = "List all Jet jobs")
    void showJetJobs() {
        JetService jetService = hazelcastClient.getJet();
        List<Job> jobs = jetService.getJobs();
        embedInTable(
                new String[]{"name", "light job", "id", "status", "submission time", "job config"},
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

    // jet.submitjar "D:/Users/user/.m2/com/hazelcast/jet/tests/kafka-connect-source/5.4.0-SNAPSHOT/kafka-connect-source-5.4.0-SNAPSHOT.jar"
    @ShellMethod(key = "jet.submitjar", value = "Submit Jet Job jar")
    void submitJar(@ShellOption @Valid @NotNull String filePath) {
        try {
            Path jarPath = Paths.get(filePath);

            JetClientInstanceImpl jetClientInstance = getClientJetService();
            SubmitJobParameters submitJobParameters = SubmitJobParameters.withJarOnClient()
                    .setJarPath(jarPath);

            jetClientInstance.submitJobFromJar(submitJobParameters);

            shellHelper.printSuccess("Success");
        } catch (Exception exception) {
            log.error("Sql Exception: {}", exception.getMessage(), exception);
            shellHelper.printError("Failed");
        }
    }

    private JetClientInstanceImpl getClientJetService() {
        return (JetClientInstanceImpl) hazelcastClient.getJet();
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
