/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mariadb.jdbc;

import de.chojo.sadu.core.jdbc.RemoteJdbcConfig;

/**
 * A builder to create a MariaDB jdbc url.
 */
public class MariaDbJdbc extends RemoteJdbcConfig<MariaDbJdbc> {
    public MariaDbJdbc() {
    }

    @Override
    public String driver() {
        return "mariadb";
    }

    /**
     * The connection timeout value, in milliseconds, or zero for no timeout.
     * Default: 30 000.
     *
     * @param millis milliseconds
     * @return builder instance
     */
    public MariaDbJdbc connectTimeout(int millis) {
        return addParameter("connectTimeout", millis);
    }

    /**
     * PrepareStatement are prepared on the server side before executing.
     * The applications that repeatedly use the same queries have value to activate this option,
     * but the general case is to use the direct command (text protocol).
     *
     * @return builder instance
     */
    public MariaDbJdbc useServerPrepStmts() {
        return addParameter("useServerPrepStmts", true);
    }

    /**
     * Permit loading data from file
     * <p>
     * See <a href="https://mariadb.com/kb/en/about-mariadb-connector-j/#load-data-infile">LOAD DATA LOCAL INFILE.</a>
     *
     * @return builder instance
     */
    public MariaDbJdbc allowLocalInfile() {
        return addParameter("allowLocalInfile", true);
    }

    /**
     * Enables SSL/TLS in a specific mode.
     * <p>
     * This option replaces the deprecated options: disableSslHostnameVerification, trustServerCertificate, useSsl
     *
     * @param sslMode ssl mode. Default {@link SslMode#DISABLE}
     * @return builder instance
     */
    public MariaDbJdbc sslMode(SslMode sslMode) {
        return addParameter("sslMode", sslMode);
    }

    /**
     * Permits providing server's certificate in DER form, or server's CA certificate. The server will be added to trust storage. This permits a self-signed certificate to be trusted.
     * <p>
     * Can be used in one of 2 forms :
     * <p>
     * - serverSslCert=/path/to/cert.pem (full path to certificate)
     * <p>
     * - serverSslCert=classpath:relative/cert.pem (relative to current classpath
     *
     * @param path vert path
     * @return builder instance
     */
    public MariaDbJdbc serverSslCert(String path) {
        return addParameter("serverSslCert", path);
    }

    /**
     * File path of the keyStore file that contain client private key store and associate certificates (similar to java System property "javax.net.ssl.keyStore", but ensure that only the private key's entries are used).
     *
     * @param path path
     * @return builder instance
     */
    public MariaDbJdbc keyStore(String path) {
        return addParameter("keyStore", path);
    }

    /**
     * Password for the client certificate keyStore (similar to java System property "javax.net.ssl.keyStorePassword").
     *
     * @param password password
     * @return builder instance
     */
    public MariaDbJdbc keyStorePassword(String password) {
        return addParameter("keyStorePassword", password);
    }

    /**
     * Force TLS/SSL cipher (comma separated list).
     * <p>
     * Example : "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_DHE_DSS_WITH_AES_256_GCM_SHA384"
     * <p>
     * Default: use JRE ciphers.
     *
     * @param ciphers ciphers
     * @return builder instance
     */
    public MariaDbJdbc enabledSslCipherSuites(String ciphers) {
        return addParameter("enabledSslCipherSuites", ciphers);
    }

    /**
     * Indicate key store type (JKS/PKCS12). default is null, then using java default type.
     *
     * @param type type
     * @return builder instance
     */
    public MariaDbJdbc keyStoreType(String type) {
        return addParameter("keyStoreType", type);
    }

    /**
     * Only the first characters corresponding to this options size will be displayed in logs
     * Default: 1024.
     *
     * @param maxQuerySizeToLog size
     * @return builder instance
     */
    public MariaDbJdbc maxQuerySizeToLog(int maxQuerySizeToLog) {
        return addParameter("maxQuerySizeToLog", maxQuerySizeToLog);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public MariaDbJdbc allowMultiQueries(boolean state) {
        return addParameter("allowMultiQueries", state);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @return builder instance
     */
    public MariaDbJdbc allowMultiQueries() {
        return allowMultiQueries(true);
    }

    /**
     * If set to 'true', an exception is thrown during query execution containing a query string.
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public MariaDbJdbc dumpQueriesOnException(boolean state) {
        return addParameter("dumpQueriesOnException", state);
    }

    /**
     * @see <a href="https://mariadb.com/kb/en/about-mariadb-connector-j/">MariaDb parameter</a>
     */
    @Override
    public <V> MariaDbJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }

    @Override
    protected String defaultDriverClass() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public Credentials userCredentials() {
        return new Credentials(removeParameter("user"), removeParameter("password"));
    }

    /**
     * Represents different SSL modes.
     */
    public enum SslMode {
        /**
         * Do not use SSL/TLS
         */
        DISABLE("disable"),
        /**
         * Only use SSL/TLS for encryption. Do not perform certificate or hostname verification. This mode is not safe for production applications.
         */
        TRUST("trust"),
        /**
         * Use SSL/TLS for encryption and perform certificates verification, but do not perform hostname verification.
         */
        VERIFY_CA("verify-ca"),
        /**
         * Use SSL/TLS for encryption, certificate verification, and hostname verification.
         */
        VERIFY_FULL("verify-full");

        private final String value;

        SslMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
