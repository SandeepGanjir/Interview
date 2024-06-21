package lld;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
Design a module for a logging system. The system needs to support different levels of logging (e.g., INFO, DEBUG, ERROR),
and it should be able to log messages to different outputs (e.g., console, file, database). Here are the requirements:

Each log message has a level, a message, and a timestamp.
The system should be able to filter messages based on their level. For example, in a production environment, you might only want to log ERROR messages.
The system should support multiple output destinations. For example, you might want to log ERROR messages to a database for further analysis,
but log INFO messages to the console for immediate feedback.
Create the classes and interfaces necessary to represent these entities and their behaviors. Consider what methods might be necessary for each class.
*/

/**
 * Actors:
 * 1. LogUser
 * - create Logger for each class
 * - logMessage(String msg, level)
 * 2 LogAdmin
 * - configure LogOutputs using LogManager (addOutputMode(Ouputmode, Level))
 *
 *
 @startuml

 package LoggingSystem {

 enum LogLevel {
 DEBUG
 INFO
 ERROR
 }

 class Log {
 - logLevel : LogLevel <<get>>
 - message : String <<get>>
 - timestamp : LocalDateTime
 + Log(String msg, LogLevel level)
 + getTimestamp() : String
 }

 interface Logger {
 + log(String msg, LogLevel level)
 }

 class ConcreteLogger {
 + log(String msg, LogLevel level)
 }

 class LoggerFactory {
 {static} - Map<String, Logger> loggers
 {static} + createLogger(String className) : Logger
 }

 abstract class LogOutput {
 - outputLogLevel : LogLevel <<set>>
 + LogOutput(LogLevel level)
 - shouldProcess(Log logMsg) : boolean
 + processMessage(Log logMsg)
 {abstract} # handleLogMessage(Log logMsg)
 }

 class ConsoleLogOutput {
 + ConsoleLogOutput(LogLevel level)
 # handleLogMessage(Log logMsg)
 }

 class FileLogOutput {
 + FileLogOutput(LogLevel level)
 # handleLogMessage(Log logMsg)
 }

 class DatabseLogOutput {
 + DatabseLogOutput(LogLevel level)
 # handleLogMessage(Log logMsg)
 }

 class LogManager {
 {static} - instance: Singleton<LogManager>
 - logOutputList : List<LogOutput>;
 - msgQueue : Queue<Log>;
 - scheduler : ScheduledExecutorService;
 {static} + addLogOutput(LogOutput logOutput)
 {static} + logsByLevel(LogLevel level) : List<Log>
 {static} + addLog(Log log)
 - void processLogs()
 {static} + start()
 {static} + close()
 }

 class LoggingSystem {
 - logger: Logger
 - test()
 {static} + main(String[] args)
 }

 LogOutput <|-- ConsoleLogOutput
 LogOutput <|-- FileLogOutput
 LogOutput <|-- DatabseLogOutput

 ConcreteLogger ..|> Logger

 LogManager "1" *-- "0..*" LogOutput
 LogManager "1" *-- "0..*" Log

 LoggingSystem --> LoggerFactory
 LoggingSystem --> Logger
 LogManager --> Log
 LogManager --> LogOutput
 LoggerFactory --> Logger
 ConcreteLogger ..> LogManager
 Log --> LogLevel
 LogOutput --> LogLevel
 }

 @enduml

 */

enum LogLevel {
    DEBUG,
    INFO,
    ERROR
}

class Log {
    private final LogLevel logLevel;
    private final String message;
    private final LocalDateTime timestamp;

    public Log(String msg, LogLevel level) {
        message = msg;
        logLevel = level;
        timestamp = LocalDateTime.now();
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public String getMessage() {
        return message;
    }
}

interface Logger {
    void log(String msg, LogLevel level);
}

class ConcreteLogger implements Logger {

    public void log(String msg, LogLevel level) {
        Log log = new Log(msg, level);
        LogManager.addLog(log);
    }
}

class LoggerFactory {
    private static final Map<String, Logger> loggers = new HashMap<>();

    public static Logger createLogger(String className) {
        return loggers.computeIfAbsent(className, c -> new ConcreteLogger());
    }
}

abstract class LogOutput {
    private LogLevel outputLogLevel;
    public LogOutput(LogLevel level) {
        outputLogLevel = level;
    }

    private final boolean shouldProcess(Log logMsg) {
        boolean doProcess = false;
        LogLevel msgLogLevel = logMsg.getLogLevel();
        switch (outputLogLevel) {
            case DEBUG : doProcess = doProcess || (msgLogLevel == LogLevel.DEBUG);
            case INFO : doProcess = doProcess || (msgLogLevel == LogLevel.INFO);
            case ERROR : doProcess = doProcess || (msgLogLevel == LogLevel.ERROR);
        }
        return doProcess;
    }

    public final void processMessage(Log logMsg) {
        if (shouldProcess(logMsg)) {
            handleLogMessage(logMsg);
        }
    }

    abstract protected void handleLogMessage(Log logMsg);

    public void setOutputLogLevel(LogLevel level) {
        this.outputLogLevel = level;
    }
}

class ConsoleLogOutput extends LogOutput {
    public ConsoleLogOutput(LogLevel level) {
        super(level);
    }
    protected void handleLogMessage(Log logMsg) {
        // Write message to Console
        System.out.println(logMsg.getLogLevel() + " : " + logMsg.getTimestamp() + " : " + logMsg.getMessage());
    }
}

class FileLogOutput extends LogOutput {
    public FileLogOutput(LogLevel level) {
        super(level);
    }
    protected void handleLogMessage(Log logMsg) {
        // Write message to File
    }
}

class DatabseLogOutput extends LogOutput {
    public DatabseLogOutput(LogLevel level) {
        super(level);
    }
    protected void handleLogMessage(Log logMsg) {
        // Write message to Database
    }
}

class LogManager {
    private static class Singleton {
        private static final LogManager instance = new LogManager();
    }

    private final List<LogOutput> logOutputList;
    private final Queue<Log> msgQueue;
    private final ScheduledExecutorService scheduler;

    private LogManager() {
        logOutputList = new ArrayList<>();
        msgQueue = new ConcurrentLinkedQueue<>();
        scheduler = Executors.newScheduledThreadPool(1);
    }

    public static void addLogOutput(LogOutput logOutput) {
        Singleton.instance.logOutputList.add(logOutput);
    }

    public static List<Log> logsByLevel(LogLevel level) {
        // ToDo:
        return null;
    }

    public static void addLog(Log log) {
        Singleton.instance.msgQueue.offer(log);
    }

    private void processLogs() {
        while (!Singleton.instance.msgQueue.isEmpty() && !Singleton.instance.logOutputList.isEmpty()) {
            Log msg = Singleton.instance.msgQueue.poll();
            for (LogOutput logOutput : Singleton.instance.logOutputList) {
                logOutput.processMessage(msg);
            }
        }
    }

    public static void start() {
        Singleton.instance.scheduler.scheduleWithFixedDelay(Singleton.instance::processLogs, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public static void close() {
        Singleton.instance.processLogs();
        Singleton.instance.scheduler.shutdown();
    }
}


public class LoggingSystem {
    private final Logger logger = LoggerFactory.createLogger("App.class");

    private void test() {
        logger.log("DEBUG", LogLevel.DEBUG);
        logger.log("INFO", LogLevel.INFO);
        logger.log("ERROR", LogLevel.ERROR);
    }

    public static void main(String[] args) {
        LogManager.addLogOutput(new ConsoleLogOutput(LogLevel.INFO));
        LogManager.start();

        LoggingSystem ins = new LoggingSystem();
        ins.test();

        LogManager.close();
    }
}
