package com.tree.clouds.schedule.common.ffmpeg.base;

import com.tree.clouds.schedule.common.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tree.clouds.schedule.common.ffmpeg.Move.doWaitFor;


/**
 * Created by feiFan.gou on 2017/8/17 14:40.
 */
@Slf4j
public class Base {

    private static Lock lock = new ReentrantLock();

    protected static void executeFFMPeg(List<String> command) {
        execute(command, Constants.ffmpeg_path);
    }

//    protected static String executeFFProbe(List<String> command) {
////        return execute(command, Constants.ffprobe_path);
//    }

    private static String execute(List<String> commands, String exePath) {
        lock.lock();
        System.out.println("开始执行命令** " + commands);
        log.info("开始执行命令** " + commands);
        StringBuilder stringBuilder = new StringBuilder();
        Process p = null;
        try {
            commands.add(0, exePath);
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.command(commands);
            p = builder.start();
            p.getOutputStream().close();
            stringBuilder = doWaitFor(p);
            p.destroy();
            System.out.println("执行命令结束** " + commands);
            log.info("执行命令结束** " + commands);
        } catch (Exception e) {
            p.destroy();
        } finally {
            lock.unlock();
        }
        return stringBuilder.toString();
    }
}
