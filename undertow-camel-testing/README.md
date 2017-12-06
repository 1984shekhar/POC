Just a POC to show how hostoptions can be set with camel-undertow.

Run this example as Run as Local camel-context from IDE, it is not Red Hat JBoss Fuse 6.x example.

UndertowHostOptions.java:

  /**
     * The number of worker threads to use in a Undertow host.
     */
    private Integer workerThreads;

    /**
     * The number of io threads to use in a Undertow host.
     */
    private Integer ioThreads;

    /**
     * The buffer size of the Undertow host.
     */
    private Integer bufferSize;

    /**
     * Set if the Undertow host should use direct buffers.
     */
    private Boolean directBuffers;
    
    /**
     * Set if the Undertow host should use http2 protocol.
     */
    private Boolean http2Enabled;
