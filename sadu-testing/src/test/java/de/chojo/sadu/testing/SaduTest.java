package de.chojo.sadu.testing;

import de.chojo.sadu.postgresql.databases.PostgreSql;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SaduTest {
    @Test
    public void checkDatabase() throws IOException {
        SaduTests.execute(1, PostgreSql.get());
    }
}
