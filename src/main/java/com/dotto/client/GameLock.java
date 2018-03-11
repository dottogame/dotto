package com.dotto.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stops the game from ever having more than one instance of itself.
 * 
 * @author SoraKatadzuma
 */
public final class GameLock {
    /** Our file that we will be using to lock with. */
    private static File lockFile;
    /** A channel by which we can retrieve our lock. */
	private static FileChannel channel;
    /** Our lock. */
	private static FileLock lock;
    
    /**
     * Locks the game by creating a lock file, and prevents any other
     * instances of the game from being created.
     * 
     * @throws java.net.URISyntaxException
     */
    public static void lockGame() throws URISyntaxException {
        try {
            String path = GameLock.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI().getPath();
            
            File root = new File(path).getParentFile()
                .getParentFile();
            
            lockFile = new File(root, "bin/game.lock");
            
            if (lockFile.exists()) lockFile.delete();
            
            channel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = channel.tryLock();
            
            if (lock == null) {
                channel.close();
                System.exit(0);
            }
            
            Runtime.getRuntime()
                .addShutdownHook(
                    new Thread(
                        () -> Core.shutdown()
                    )
                );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameLock.class.getName())
                    .log(Level.SEVERE, "File not found.", ex);
        } catch (IOException ex) {
            Logger.getLogger(GameLock.class.getName())
                    .log(Level.SEVERE, "File read/write problem.", ex);
        }
    }
    
    /**
     * Unlocks the lock file when the program exits.
     */
    public static void unlockFile() {
        try {
            if(lock != null)
                lock.release();

            channel.close();
            lockFile.delete();
        } catch(IOException e) {
            Logger.getLogger(GameLock.class.getName())
                    .log(Level.SEVERE, "File read/write problem.", e);
        }
    }
}
