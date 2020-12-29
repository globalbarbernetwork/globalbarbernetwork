/*
 * Copyright (C) 2020 IOC DAW
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
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.globalbarbernetwork.jobs.StateReserveJob;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import org.quartz.Job;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author IOC DAW
 */
@WebListener
public class QuartzService implements ServletContextListener {

    private static Scheduler scheduler;
    private boolean activeQuartz;
    private Properties props;

    public QuartzService() {
        props = new Properties();
        try {
            InputStream is = new FileInputStream("D:/tmp/config.properties");
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
                    .withSchedule(cronSchedule("0 0/1 * * * ?"))
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        activeQuartz = Boolean.parseBoolean(props.getProperty("activeQuartz"));
        if (activeQuartz) {
            callJobs();
        }
    }

    @Override
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
