package net.coderodde.vfs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements a virtual file system node.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 4, 2017)
 */
public class VirtualFileSystemNode {
    
    /**
     * The byte value indicating that the node has a password.
     */
    private static final byte PASSWORD_ON = 1;
    
    /**
     * The byte value indicating that the node has no password.
     */
    private static final byte PASSWORD_OFF = 0;
    
    /**
     * Used to indicate in the metablock that this node is a symbolic link.
     */
    private static final byte SYMBOLIC_LINK_ON = 1;
    
    /**
     * Used to indicate in the metablock that this node is not a symbolic link.
     */
    private static final byte SYMBOLIC_LINK_OFF = 0;
    
    /**
     * Each file system block is half kilobytes large.
     */
    private static final int BLOCK_SIZE = 512;
    
    /**
     * Maximum number of {@code char}s in a node name regardless of the type of
     * the node.
     */
    private static final int MAXIMUM_NODE_NAME_LENGTH = 64;
    
    /**
     * The number of bytes in a password hash.
     */
    private static final int PASSWORD_HASH_LENGTH = 32;
    
    /**
     * Stores the node type of this node.
     */
    private final VirtualFileSystemNodeType nodeType;
        
    /**
     * Points to the parent node that is a directory. For all nodes except the
     * root node "/" this field is not null.
     */
    private VirtualFileSystemNode parent;
    
    /**
     * If this node is a file and was accessed, stores the binary data of the 
     * file.
     */
    private byte[] fileData;
    
    /**
     * If this object is a file, caches the size of the file. Otherwise, this 
     * object is directory, and this field caches the size of all files in this
     * subtree. If this node is a link, caches the size of the linked node.
     */
    private int nodeSize;
    
    /**
     * Stores the time at which this node was created. The number of 
     * milliseconds since the Unix Epoch (00:00:00 UTC Thursday January 1, 
     * 1970).
     */
    private long creationTime;
    
    /**
     * The time stamp of the most recent access to the node. This time stamp 
     * handles only read operations to the node.
     */
    private long lastAccessedTime;
    
    /**
     * The time stamp of the most recent modification to the node. For a file,
     * modification are modifying the data or renaming the file. For a
     * directory, these are adding/removing a file to the directory or renaming
     * it.
     */
    private long lastModificationTime;
    
    /**
     * If this node is a directory, this map maps each node name to its node.
     */
    private Map<String, VirtualFileSystemNode> childMap;
    
    /**
     * This field contains the name of this node. If this node represents a
     * file, this is the name of that very file. Otherwise, this object 
     * represents a directory.
     */
    private String nodeName;
    
    /**
     * If this node is a symbolic link, this field points to the linked node.
     */
    private VirtualFileSystemNode link;
    
    /**
     * If this node is password protected, this field contains the SHA-256 hash
     * of the password. If no password is set for this node, this field is
     * {@code null}.
     */
    private byte[] passwordHash;
    
    /**
     * Set to {@code true} whenever this node represents a regular file whose
     * data was modified but not committed.
     */
    private boolean dirty;
    
    /**
     * The index of the block that contains the metadata of this node or -1 if
     * this node was not persisted into the native image file.
     */
    private int metablockIndex;
    
    private final List<VirtualFileBlock> dataBlockList = null;
    
    
    
    public static VirtualFileSystemNode 
        createRegularFile(String fileName, 
                          String password, 
                          VirtualFileSystemNode parentDirectory) {
        VirtualFileSystemNode node =
                new VirtualFileSystemNode(
                        fileName,
                        password,
                        parentDirectory,
                        VirtualFileSystemNodeType.REGULAR_FILE,
                        null);
                                                               
        return node;
    }
        
    public static VirtualFileSystemNode
        createDirectory(String directoryName,
                        String password,
                        VirtualFileSystemNode parentDirectory) {
        VirtualFileSystemNode node = 
                new VirtualFileSystemNode(
                        directoryName,
                        password,
                        parentDirectory,
                        VirtualFileSystemNodeType.DIRECTORY,
                        null);
        
        return node;
    }
        
    public static VirtualFileSystemNode
        createSymbolicLink(String linkName,
                           String password,
                           VirtualFileSystemNode parentDirectory,
                           VirtualFileSystemNode linkedNode) {
        Objects.requireNonNull(linkedNode, "The linked node is null.");
        VirtualFileSystemNode node =
                new VirtualFileSystemNode(
                        linkName,
                        password,
                        parentDirectory,
                        VirtualFileSystemNodeType.SYMBOLIC_LINK,
                        linkedNode);
        return null;
    }
    
    private VirtualFileSystemNode(String nodeName,
                                  String password,
                                  VirtualFileSystemNode parentDirectory,
                                  VirtualFileSystemNodeType nodeType,
                                  VirtualFileSystemNode linked) {
        Objects.requireNonNull(nodeName, "The node name is null.");
        Objects.requireNonNull(parentDirectory,     
                               "The parent directory is null.");
        Objects.requireNonNull(nodeType, "The node type is null.");
        
        this.nodeName = checkNodeName(nodeName, parentDirectory);
        this.nodeType = nodeType;
        
        switch (nodeType) {
            case REGULAR_FILE:
                
            case DIRECTORY:
                
            case SYMBOLIC_LINK:
                
        }
        
        if (password != null) {
            try {
                byte[] passwordHash = 
                        MessageDigest
                            .getInstance("SHA-256")
                            .digest(password.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("Cannot hash the password.", ex);
            }
        }
        
        long currentTime = System.currentTimeMillis();
        
        this.creationTime         = currentTime;
        this.lastAccessedTime     = currentTime;
        this.lastModificationTime = currentTime;        
    }
        
    public boolean containsNodeName(String name) {
        switch (nodeType) {
            case DIRECTORY:
                return childMap.containsKey(name);
                
            case REGULAR_FILE:
                throw new IllegalStateException("This node is a regular file.");
                
            case SYMBOLIC_LINK:
                return link.containsNodeName(name);
                
            default:
                throw new IllegalStateException("This should not be thrown.");
        }
    }
    
    public void attachNode(VirtualFileSystemNode node) {
        Objects.requireNonNull(node, "The input node is null.");
    }
    
    /**
     * Returns the node size. If this node represents a regular file, the size
     * of that file is returned. If this node represents a directory, returns 
     * the size of all the file in the subtree represented by this directory.
     * If this node is a link, returns recursively the size of the linked node.
     * 
     * @return the size in bytes.
     */
    public int getNodeSize() {
        return nodeSize;
    }
    
    /**
     * Returns the creation time stamp of this node that is represented by the
     * number of milliseconds since the Unix Epoch (00:00:00 UTC Thursday 1
     * January, 1970).
     * 
     * @return the creation time stamp.
     */
    public long getCreationTimestamp() {
        return creationTime;
    }
    
    /**
     * Returns the last access time stamp in milliseconds since the Unix Epoch.
     * 
     * @return the last access time stamp.
     */
    public long getLastAccessTimestamp() {
        return lastAccessedTime;
    }
    
    /**
     * Returns the last modification time stamp in milliseconds since the Unix
     * Epoch.
     * 
     * @return the last modification time stamp.
     */
    public long getLastModificationTimestamp() {
        return lastModificationTime;
    }
    
    public void loadFile() {
        
    }
    
    public void storeFile() {
        
    }
    
    /**
     * Converts this node's metadata into a metablock, that describes the node
     * and not its content.
     * 
     * @return the metablock bytes.
     */
    byte[] convertToMetaBlock() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK_SIZE)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        // Emit the node type:
        byteBuffer.put(nodeType.getValue());
        
        // The index of the block that contains the metadata of the parent node:
        byteBuffer.putInt(parent.metablockIndex);
        
        // The number of characters in the name of this node:
        int nodeNameLength = nodeName.length();
        byteBuffer.putInt(nodeNameLength);
        
        // The actual node name characters:
        for (int i = 0; i < nodeNameLength; ++i) {
            byteBuffer.putChar(nodeName.charAt(i));
        }
        
        // The file size:
        byteBuffer.putInt(nodeSize);
        
        // Password?
        byteBuffer.put(passwordHash != null ? PASSWORD_ON : PASSWORD_OFF);
        
        if (passwordHash != null) {
            // Indicate that password present:
            byteBuffer.put(PASSWORD_ON);
            
            // Emit the password hash:
            byteBuffer.put(passwordHash);
        } else {
            byteBuffer.put(PASSWORD_OFF);
        }
        
        // The creation time stamp:
        byteBuffer.putLong(creationTime);
        
        // The last access time stamp:
        byteBuffer.putLong(lastAccessedTime);
        
        // The last modification time stamp:
        byteBuffer.putLong(lastModificationTime);
        
        if (link != null) {
            byteBuffer.put(SYMBOLIC_LINK_ON);
            byteBuffer.putInt(link.metablockIndex);
        } else {
            byteBuffer.put(SYMBOLIC_LINK_OFF);
        }
        
        if (nodeType.equals(VirtualFileSystemNodeType.DIRECTORY)) {
            // Emit something relevant for directory. TODO:
        }
        
        byteBuffer.position(0);
        return byteBuffer.array();
    }
    
    private String checkNodeName(String name, 
                                 VirtualFileSystemNode parentDirectory) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name of a node is empty.");
        }
        
        if (name.length() > MAXIMUM_NODE_NAME_LENGTH) {
            throw new IllegalArgumentException("The node name is " +
                    (name.length() - MAXIMUM_NODE_NAME_LENGTH) +
                    " character(s) too long.");
        }
        
        if (parentDirectory.containsNodeName(name)) {
            throw new IllegalArgumentException("The name \"" + name + "\"" +
                    " is already occupied.");
        }
        
        return name;
    }
}
