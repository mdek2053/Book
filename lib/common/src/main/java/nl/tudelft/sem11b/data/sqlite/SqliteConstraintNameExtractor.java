package nl.tudelft.sem11b.data.sqlite;

import java.sql.SQLException;

import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.internal.util.JdbcExceptionHelper;

public class SqliteConstraintNameExtractor extends TemplatedViolatedConstraintNameExtracter {
    @Override
    protected String doExtractConstraintName(SQLException ex) throws NumberFormatException {
        final int errorCode = JdbcExceptionHelper.extractErrorCode(ex) & 0xFF;
        final int errorCodeComp = 0x13;
        if (errorCode == errorCodeComp) {
            return extractUsingTemplate("constraint ", " failed", ex.getMessage());
        }
        return null;
    }
}

