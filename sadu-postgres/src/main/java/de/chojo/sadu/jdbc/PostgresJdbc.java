/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

public class PostgresJdbc extends RemoteJdbcConfig<PostgresJdbc> {
    @Override
    public String driver() {
        return "postgresql";
    }

    /**
     * The connect timeout value, in milliseconds, or zero for no timeout.
     * Default: 30 000.
     *
     * @param millis milliseconds
     * @return builder instance
     */
    public PostgresJdbc connectTimeout(int millis) {
        return addParameter("connectTimeout", millis);
    }

    /**
     * Connect using SSL. The server must have been compiled with SSL support.
     * This property does not need a value associated with it.
     * The mere presence of it specifies a SSL connection.
     * However, for compatibility with future versions, the value "true" is preferred.
     * For more information see <a href="https://jdbc.postgresql.org/documentation/head/ssl.html">Chapter 4, Using SSL.</a>
     *
     * @param ssl ssl mode.
     * @return builder instance
     */
    public PostgresJdbc ssl(boolean ssl) {
        return addParameter("ssl", ssl);
    }

    /**
     * The provided value is a class name to use as the SSLSocketFactory when establishing a SSL connection.
     * For more information see the section called <a href="https://jdbc.postgresql.org/documentation/head/ssl-factory.html">Custom SSLSocketFactory”</a>. defaults to LibPQFactory
     *
     * @param sslFactory ssl factory.
     * @return builder instance
     */
    public PostgresJdbc sslFactory(String sslFactory) {
        return addParameter("sslFactory", sslFactory);
    }

    /**
     * Enables SSL/TLS in a specific mode.
     * <p>
     * This option replaces the deprecated options: disableSslHostnameVerification, trustServerCertificate, useSsl
     *
     * @param sslMode ssl mode. Default {@link SslMode#PREFER}
     * @return builder instance
     */
    public PostgresJdbc sslMode(SslMode sslMode) {
        return addParameter("sslMode", sslMode);
    }

    /**
     * Provide the full path for the certificate file. Defaults to /defaultdir/postgresql.crt, where defaultdir is ${user.home}/.postgresql/ in *nix systems and %appdata%/postgresql/ on windows.
     * <p>
     * It can be a PEM encoded X509v3 certificate
     * <p>
     * Note: This parameter is ignored when using PKCS-12 keys, since in that case the certificate is also retrieved from the same keyfile.
     *
     * @param path vert path
     * @return builder instance
     */
    public PostgresJdbc sslcert(String path) {
        return addParameter("sslcert", path);
    }

    /**
     * Provide the full path for the key file. Defaults to /defaultdir/postgresql.pk8.
     * <p>
     * Note: The key file must be in PKCS-12 or in PKCS-8 DER format. A PEM key can be converted to DER format using the openssl command:
     *
     * @param path path
     * @return builder instance
     */
    public PostgresJdbc sslkey(String path) {
        return addParameter("sslkey", path);
    }

    /**
     * File name of the SSL root certificate. Defaults to defaultdir/root.crt
     * <p>
     * It can be a PEM encoded X509v3 certificate
     *
     * @param sslrootcert sslrootcert
     * @return builder instance
     */
    public PostgresJdbc sslrootcert(String sslrootcert) {
        return addParameter("sslrootcert", sslrootcert);
    }

    /**
     * Class name of hostname verifier. Defaults to using org.postgresql.ssl.PGjdbcHostnameVerifier
     *
     * @param sslhostnameverifier sslhostnameverifier
     * @return builder instance
     */
    public PostgresJdbc sslhostnameverifier(String sslhostnameverifier) {
        return addParameter("sslhostnameverifier", sslhostnameverifier);
    }

    /**
     * Class name of the SSL password provider. Defaults to {@code org.postgresql.ssl.jdbc4.LibPQFactory.ConsoleCallbackHandler}
     *
     * @param sslpasswordcallback sslpasswordcallback
     * @return builder instance
     */
    public PostgresJdbc sslpasswordcallback(String sslpasswordcallback) {
        return addParameter("sslpasswordcallback", sslpasswordcallback);
    }

    /**
     * If provided will be used by ConsoleCallbackHandler
     *
     * @param sslpassword sslpassword
     * @return builder instance
     */
    public PostgresJdbc sslpassword(int sslpassword) {
        return addParameter("sslpassword", sslpassword);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public PostgresJdbc allowMultiQueries(boolean state) {
        return addParameter("allowMultiQueries", state);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @return builder instance
     */
    public PostgresJdbc allowMultiQueries() {
        return allowMultiQueries(true);
    }

    /**
     * If set to 'true', an exception is thrown during query execution containing a query string.
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public PostgresJdbc dumpQueriesOnException(boolean state) {
        return addParameter("dumpQueriesOnException", state);
    }

    /**
     * {@inheritDoc}
     *
     * @see <a href="https://jdbc.postgresql.org/documentation/head/connect.html">Postgres parameter</a>
     */
    @Override
    public <V> PostgresJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }

    @Override
    protected String defaultDriverClass() {
        return "org.postgresql.Driver";
    }

    public enum SslMode {
        /**
         * No validation
         */
        DISABLE("disable"),
        /**
         * No validation
         */
        ALLOW("allow"),
        /**
         * No validation
         */
        PREFER("prefer"),
        /**
         * Only use SSL/TLS for encryption. Do not perform certificate or hostname verification. This mode is not safe for production applications.
         */
        REQUIRE("trust"),
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
