package com.meiqi.dsmanager.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * User: 
 * Date: 13-7-10
 * Time: 上午10:48
 */
public class LogUtil {
    private static final Logger logger = Logger.getLogger(LogUtil.class);
    private static final ConcurrentLinkedQueue<LoggingEvent> queue = new ConcurrentLinkedQueue<LoggingEvent>();
    private static final String className = LogUtil.class.getName();
    private static final Log LOG =  LogFactory.getLog("request");
    
    static {
        new Thread(new Runnable() {
            public void run() {
                Thread.currentThread().setName("log thread");
                LoggingEvent event;

                while (true) {

                    while ((event = queue.poll()) != null) {
                        logger.callAppenders(event);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        //
                    }

                }

            }
        }).start();
    }

    private static LoggingEvent getEvent(Level level, String msg, Throwable t) {
        LoggingEvent event = new LoggingEvent(className, logger, level, msg, t);
        event.getLocationInformation();

        return event;
    }

    public static void info(String msg, Throwable t) {
        queue.offer(getEvent(Level.INFO, msg, t));
    }

    public static void info(String msg) {
        //queue.offer(getEvent(Level.INFO, msg, null));
        LOG.info(msg);
    }

    public static void debug(String msg, Throwable t) {
        queue.offer(getEvent(Level.DEBUG, msg, t));
    }

    public static void debug(String msg) {
        queue.offer(getEvent(Level.DEBUG, msg, null));
    }

    public static void warn(String msg, Throwable t) {
        queue.offer(getEvent(Level.WARN, msg, t));
    }

    public static void warn(String msg) {
        queue.offer(getEvent(Level.WARN, msg, null));
    }

    public static void error(Throwable t) {
        queue.offer(getEvent(Level.ERROR, null, t));
    }
    
    public static void error(String msg, Throwable t) {
        queue.offer(getEvent(Level.ERROR, msg, t));
    }

    public static void error(String msg) {
        //queue.offer(getEvent(Level.ERROR, msg, null));
        logger.error(msg);
    }

    public static void fatal(String msg, Throwable t) {
        queue.offer(getEvent(Level.FATAL, msg, t));
    }

    public static void fatal(String msg) {
        queue.offer(getEvent(Level.FATAL, msg, null));
    }

    public static void trace(String msg, Throwable t) {
        queue.offer(getEvent(Level.TRACE, msg, t));
    }

    public static void trace(String msg) {
        queue.offer(getEvent(Level.TRACE, msg, null));
    }
}
