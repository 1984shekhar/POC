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
    
    
    
    One can easily test it with Linux utility 'nghttp' for 'http2' like below:
    
    cpandey@cpandey camel-undertow]$ nghttp -v http://localhost:7766/foo/bar
[ERROR] Could not connect to the address ::1
Trying next address 127.0.0.1
[  0.000] Connected
[  0.000] send SETTINGS frame <length=12, flags=0x00, stream_id=0>
          (niv=2)
          [SETTINGS_MAX_CONCURRENT_STREAMS(0x03):100]
          [SETTINGS_INITIAL_WINDOW_SIZE(0x04):65535]
[  0.000] send PRIORITY frame <length=5, flags=0x00, stream_id=3>
          (dep_stream_id=0, weight=201, exclusive=0)
[  0.000] send PRIORITY frame <length=5, flags=0x00, stream_id=5>
          (dep_stream_id=0, weight=101, exclusive=0)
[  0.000] send PRIORITY frame <length=5, flags=0x00, stream_id=7>
          (dep_stream_id=0, weight=1, exclusive=0)
[  0.000] send PRIORITY frame <length=5, flags=0x00, stream_id=9>
          (dep_stream_id=7, weight=1, exclusive=0)
[  0.000] send PRIORITY frame <length=5, flags=0x00, stream_id=11>
          (dep_stream_id=3, weight=1, exclusive=0)
[  0.000] send HEADERS frame <length=45, flags=0x25, stream_id=13>
          ; END_STREAM | END_HEADERS | PRIORITY
          (padlen=0, dep_stream_id=11, weight=16, exclusive=0)
          ; Open new stream
          :method: GET
          :path: /foo/bar
          :scheme: http
          :authority: localhost:7766
          accept: */*
          accept-encoding: gzip, deflate
          user-agent: nghttp2/1.21.1
[  0.001] recv SETTINGS frame <length=18, flags=0x00, stream_id=0>
          (niv=3)
          [SETTINGS_HEADER_TABLE_SIZE(0x01):4096]
          [SETTINGS_MAX_FRAME_SIZE(0x05):16384]
          [SETTINGS_INITIAL_WINDOW_SIZE(0x04):65535]
[  0.001] send SETTINGS frame <length=0, flags=0x01, stream_id=0>
          ; ACK
          (niv=0)
[  0.001] recv SETTINGS frame <length=0, flags=0x01, stream_id=0>
          ; ACK
          (niv=0)
[  0.003] recv (stream_id=13) :status: 200
[  0.003] recv (stream_id=13) accept: */*
[  0.003] recv (stream_id=13) accept-encoding: gzip, deflate
[  0.003] recv (stream_id=13) breadcrumbid: ID-cpandey-pnq-csb-1512577017865-0-3
[  0.003] recv (stream_id=13, sensitive) content-length: 16
[  0.003] recv (stream_id=13) user-agent: nghttp2/1.21.1
[  0.003] recv (stream_id=13, sensitive) date: Wed, 06 Dec 2017 17:11:51 GMT
[  0.003] recv HEADERS frame <length=88, flags=0x04, stream_id=13>
          ; END_HEADERS
          (padlen=0)
          ; First response header
Sending Response[  0.003] recv DATA frame <length=16, flags=0x01, stream_id=13>
          ; END_STREAM
[  0.003] send GOAWAY frame <length=8, flags=0x00, stream_id=0>
          (last_stream_id=0, error_code=NO_ERROR(0x00), opaque_data(0)=[])
[cpandey@cpandey camel-undertow]$ 

