package net.coderodde.vfs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a view over a virtual file system via its application 
 * programming interface.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 6, 2017)
 */
public final class VirtualFilesystem {

    /**
     * The number of blocks in this virtual file system; both occupied and 
     * unoccupied.
     */
    private int totalBlocks;
    
    /**
     * Reads a virtual file system from a given native image file.
     * 
     * @param file the file containing the virtual file system.
     * @return the virtual file system.
     */
    public static VirtualFilesystem 
        readVirtualFileSystemFromNativeImage(File file) {
        return null;
    }
        
    /**
     * Reads a virtual file system from a given native image file by the name
     * of the image file.
     * 
     * @param fileName the name of the file that stores the desired virtual
     *                 file system.
     * @return the virtual file system.
     */    
    public static VirtualFilesystem readVirtualFileSystemFromNativeImage(String fileName) {
        return readVirtualFileSystemFromNativeImage(new File(fileName));
    }
    
    /**
     * Holds all the unoccupied block ranges that are ready for reuse.
     */
    private final List<VirtualFileSystemBlockIndexRange> emptyBlockRangeList =
            new ArrayList<>();
}
