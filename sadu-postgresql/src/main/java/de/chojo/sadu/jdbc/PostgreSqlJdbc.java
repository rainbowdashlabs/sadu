/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

/**
 * A builder to create a PostgreSQL jdbc url.
 */
public class PostgreSqlJdbc extends RemoteJdbcConfig<PostgreSqlJdbc> {
    @Override
    public String driver() {
        return "postgresql";
    }

    /**
     * The connection timeout value, in milliseconds, or zero for no timeout.
     * Default: 30 000.
     *
     * @param millis milliseconds
     * @return builder instance
     */
    public PostgreSqlJdbc connectTimeout(int millis) {
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
    public PostgreSqlJdbc ssl(boolean ssl) {
        return addParameter("ssl", ssl);
    }

    /**
     * The provided value is a class name to use as the SSLSocketFactory when establishing a SSL connection.
     * For more information see the section called <a href="https://jdbc.postgresql.org/documentation/head/ssl-factory.html">Custom SSLSocketFactory”</a>. defaults to LibPQFactory
     *
     * @param sslFactory ssl factory.
     * @return builder instance
     */
    public PostgreSqlJdbc sslFactory(String sslFactory) {
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
    public PostgreSqlJdbc sslMode(SslMode sslMode) {
        return addParameter("sslMode", sslMode);
    }

    /**
     * Provide the full path for the certificate file. Defaults to /defaultdir/postgresql.crt, where defaultdir is ${user.home}/.postgresql/ in *nix systems and %appdata%/postgresql/ on Windows.
     * <p>
     * It can be a PEM encoded X509v3 certificate
     * <p>
     * Note: This parameter is ignored when using PKCS-12 keys, since in that case the certificate is also retrieved from the same keyfile.
     *
     * @param path vert path
     * @return builder instance
     */
    public PostgreSqlJdbc sslcert(String path) {
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
    public PostgreSqlJdbc sslkey(String path) {
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
    public PostgreSqlJdbc sslrootcert(String sslrootcert) {
        return addParameter("sslrootcert", sslrootcert);
    }

    /**
     * Class name of hostname verifier. Defaults to using org.postgresql.ssl.PGjdbcHostnameVerifier
     *
     * @param sslhostnameverifier sslhostnameverifier
     * @return builder instance
     */
    public PostgreSqlJdbc sslhostnameverifier(String sslhostnameverifier) {
        return addParameter("sslhostnameverifier", sslhostnameverifier);
    }

    /**
     * Class name of the SSL password provider. Defaults to {@code org.postgresql.ssl.jdbc4.LibPQFactory.ConsoleCallbackHandler}
     *
     * @param sslpasswordcallback sslpasswordcallback
     * @return builder instance
     */
    public PostgreSqlJdbc sslpasswordcallback(String sslpasswordcallback) {
        return addParameter("sslpasswordcallback", sslpasswordcallback);
    }

    /**
     * If provided will be used by ConsoleCallbackHandler
     *
     * @param sslpassword sslpassword
     * @return builder instance
     */
    public PostgreSqlJdbc sslpassword(int sslpassword) {
        return addParameter("sslpassword", sslpassword);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public PostgreSqlJdbc allowMultiQueries(boolean state) {
        return addParameter("allowMultiQueries", state);
    }

    /**
     * Allow multi-queries like {@code insert into ab (i) values (1); insert into ab (i) values}
     *
     * @return builder instance
     */
    public PostgreSqlJdbc allowMultiQueries() {
        return allowMultiQueries(true);
    }

    /**
     * If set to 'true', an exception is thrown during query execution containing a query string.
     *
     * @param state state. Default: false
     * @return builder instance
     */
    public PostgreSqlJdbc dumpQueriesOnException(boolean state) {
        return addParameter("dumpQueriesOnException", state);
    }

    /**
     * @see <a href="https://jdbc.postgresql.org/documentation/head/connect.html">Postgres parameter</a>
     */
    @Override
    public <V> PostgreSqlJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }

    /**
     * Specify the schema or several schema to be set in the search-path. This schema will be used to resolve
     * unqualified object names used in statements over this connection.
     *
     * @param schema  the default schema
     * @param schemas additionally used schemas
     * @return builder instance
     */
    public PostgreSqlJdbc currentSchema(String schema, String... schemas) {
        return addParameter("currentSchema", String.join(",", schema, String.join(",")));
    }

    /**
     * Specifies the name of the application that is using the connection. This allows a database administrator to see
     * what applications are connected to the server and what resources they are using through views like pgstatactivity.
     *
     * @param applicationName application name
     * @return builder instance
     */
    public PostgreSqlJdbc applicationName(String applicationName) {
        return addParameter("ApplicationName", applicationName);
    }

    @Override
    protected String defaultDriverClass() {
        return "org.postgresql.Driver";
    }

    /**
     * Represents different SSL modes.
     */
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
