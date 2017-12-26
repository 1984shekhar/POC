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

[csp@dhcppc1 .m2]$ curl --version
curl 7.53.1 (x86_64-redhat-linux-gnu) libcurl/7.53.1 NSS/3.33 zlib/1.2.11 libidn2/2.0.4 libpsl/0.18.0 (+libidn2/2.0.3) libssh2/1.8.0 nghttp2/1.21.1
Protocols: dict file ftp ftps gopher http https imap imaps ldap ldaps pop3 pop3s rtsp scp sftp smb smbs smtp smtps telnet tftp 
Features: AsynchDNS IDN IPv6 Largefile GSS-API Kerberos SPNEGO NTLM NTLM_WB SSL libz HTTP2 UnixSockets HTTPS-proxy Metalink PSL 
[csp@dhcppc1 .m2]$ 


[csp@dhcppc1 .m2]$ curl -v --http2 http://localhost:7766/foo/bar
*   Trying ::1...
* TCP_NODELAY set
* connect to ::1 port 7766 failed: Connection refused
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 7766 (#0)
> GET /foo/bar HTTP/1.1
> Host: localhost:7766
> User-Agent: curl/7.53.1
> Accept: */*
> Connection: Upgrade, HTTP2-Settings
> Upgrade: h2c
> HTTP2-Settings: AAMAAABkAARAAAAAAAIAAAAA
> 
< HTTP/1.1 101 Switching Protocols
< Connection: Upgrade
< Upgrade: h2c
< Date: Sun, 10 Dec 2017 06:56:51 GMT
* Received 101
* Using HTTP2, server supports multi-use
* Connection state changed (HTTP/2 confirmed)
* Copying HTTP/2 data in stream buffer to connection buffer after upgrade: len=0
* Connection state changed (MAX_CONCURRENT_STREAMS updated)!
< HTTP/2 200 
< accept: */*
< http2-settings: AAMAAABkAARAAAAAAAIAAAAA
< breadcrumbid: ID-dhcppc1-1512886066149-0-23
< content-length: 16
< user-agent: curl/7.53.1
< date: Sun, 10 Dec 2017 06:56:51 GMT
< 
* Connection #0 to host localhost left intact
Sending Response[csp@dhcppc1 .m2]$ 
