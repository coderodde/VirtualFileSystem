package net.coderodde.vfs;

public enum VirtualFileSystemNodeType {

    /**
     * Denotes a regular file.
     */
    REGULAR_FILE((byte) 1),
    
    /**
     * Denotes a directory.
     */
    DIRECTORY((byte) 2),
    
    /**
     * Denotes a symbolic link.
     */
    SYMBOLIC_LINK((byte) 3);
    
    private byte value;

    private VirtualFileSystemNodeType(byte value) {
        this.value = value;
    }
    
    public byte getValue() {
        return value;
    }
}
