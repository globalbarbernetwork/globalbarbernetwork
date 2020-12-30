/*
 * Copyright (C) 2020 Grup 3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.globalbarbernetwork.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.globalbarbernetwork.jobs.StateReserveJob;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Grup 3
 */
@WebListener
public class QuartzService implements ServletContextListener {

    private static Scheduler scheduler;
    private boolean activeQuartz;
    private Properties props;

    /**
     *
     * Quartz service constructor that initialize properties for read
     * configuration
     *
     */
    public QuartzService() {

        props = new Properties();
        try {
            InputStream is = new FileInputStream("D:/tmp/config.properties");
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Method that handles calls to jobs
     *
     */
    public void callJobs() {

        try {
            System.out.println("Estamos Dentro Gente del MAIN");
            StdSchedulerFactory factory = new StdSchedulerFactory();
            scheduler = factory.getScheduler();

            JobDetail job = newJob(StateReserveJob.class)
                    .withIdentity("job", "group1")
                    .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myCronTrigger", "group1")
                    .withSchedule(cronSchedule("0 0/30 * * * ?"))
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override

    /**
     *
     * Context initialized that call method callJobs
     *
     * @param sce the ServletContextEvent
     */
    public void contextInitialized(ServletContextEvent sce) {

        activeQuartz = Boolean.parseBoolean(props.getProperty("activeQuartz"));
        if (activeQuartz) {
            callJobs();
        }
    }

    @Override

    /**
     *
     * Context destroyed that shutdown scheduler
     *
     * @param sce the ServletContextEvent
     */
    public void contextDestroyed(ServletContextEvent sce) {

        if (activeQuartz) {
            try {
                scheduler.shutdown(true);
            } catch (SchedulerException ex) {
                Logger.getLogger(QuartzService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
