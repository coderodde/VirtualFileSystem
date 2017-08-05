package net.coderodde.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class represents a block in the native file representing the file
 * system.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 4, 2017)
 */
final class VirtualFileBlock {

    /**
     * Number of bytes each block can accommodate.
     */
    private static final int BLOCK_SIZE = 512;
    
    /**
     * Number of bytes used in this block.
     */
    private int size;
    
    /**
     * In the actual file, the offset of this block.
     */
    private int offset;
    
    /**
     * Returns the size of this block (number of bytes considered to belong to
     * this block).
     * 
     * @return the number of significant bytes in this block. 
     */
    int getSize() {
        return size;
    }
    
    /**
     * Sets the size of this block.
     * 
     * @param size the new size.
     */
    void setSize(int size) {
        this.size = checkSize(size);
    }
    
    /**
     * Returns the offset of this block in the native file representing this
     * file system.
     * 
     * @return the native file offset.
     */
    int getOffset() {
        return offset;
    }
    
    /**
     * Sets the offset of this block in the native file.
     * 
     * @param offset the new offset.
     */
    void setOffset(int offset) {
        this.offset = checkOffset(offset);
    }
    
    /**
     * Reads this block from the given file.
     * 
     * @param file the native file containing this block.
     * 
     * @return the data read.
     */
    byte[] read(File file) {
        byte[] data = new byte[size];
        
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data, offset, size);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(
                    "File \"" + file.getAbsolutePath() + "\" is not found.",
                    ex);
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Cannot read the file \"" + file.getAbsolutePath() + 
                    "\".", ex);
        }
        
        return data;
    }
    
    /**
     * Saves this block to the native file representing this file system.
     * 
     * @param file the file to write to.
     * @param data the data to write.
     */
    void write(File file, byte[] data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data, offset, size);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(
                    "File \"" + file.getAbsolutePath() + "\" is not found.",
                    ex);
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Cannot write the file \"" + file.getAbsolutePath() +
                    "\".", ex);
        }
    }
    
    private int checkSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(
                    "The requested block size is negative: " + size);
        }
        
        if (size > BLOCK_SIZE) {
            throw new IllegalArgumentException(
                    "The requested block size (" + size + ") exceeds the " +
                    "block size (" + BLOCK_SIZE + ").");
        }
        
        return size;
    }
    
    private int checkOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException(
                    "The requested offset (" + offset + ") is negative.");
        }
        
        return offset;
    }
}
