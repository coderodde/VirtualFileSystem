package net.coderodde.vfs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class represents a range of virtual file system blocks. Blocks with 
 * indices from {@code minimumRangeBlockIndex} to {@code maximumRangeBlockIndex}
 * are considered to belong to this range (both indices inclusive).
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 6, 2017)
 */
final class VirtualFileSystemBlockIndexRange {

    /**
     * The smallest index of the range.
     */
    private final int minimumRangeBlockIndex;
    
    /**
     * The largest index of the range.
     */
    private final int maximumRangeBlockIndex;

    /**
     * Constructs this block index range.
     * 
     * @param minimumBlockRangeIndex the minimum block range index.
     * @param maximumBlockRangeIndex the maximum block range index.
     */
    VirtualFileSystemBlockIndexRange(int minimumBlockRangeIndex,
                                     int maximumBlockRangeIndex) {
        this.minimumRangeBlockIndex = minimumBlockRangeIndex;
        this.maximumRangeBlockIndex = maximumBlockRangeIndex;
    }
    
    /**
     * Returns the minimum index.
     * 
     * @return the minimum index.
     */
    int getMinimumBlockRangeIndex() {
        return minimumRangeBlockIndex;
    }
    
    /**
     * Returns the maximum index.
     * 
     * @return the maximum index.
     */
    int getMaximumBlockRangeIndex() {
        return maximumRangeBlockIndex;
    }
    
    /**
     * Converts this block index range to a byte array representing its state.
     * 
     * @return the byte array encoding this block index range.
     */
    byte[] toByteArray() {
        ByteBuffer byteBuffer = 
                ByteBuffer.allocate(2 * Integer.BYTES)
                          .order(ByteOrder.LITTLE_ENDIAN);
        
        byteBuffer.putInt(minimumRangeBlockIndex);
        byteBuffer.putInt(maximumRangeBlockIndex);
        
        byteBuffer.position(0);
        
        return byteBuffer.array();
    }
}
