package nl.tudelft.sem11b.data.sqlite;

import java.sql.Types;

import org.hibernate.ScrollMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.hql.spi.id.IdTableSupportStandardImpl;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.hibernate.hql.spi.id.local.AfterUseAction;
import org.hibernate.hql.spi.id.local.LocalTemporaryTableBulkIdStrategy;
import org.hibernate.internal.util.JdbcExceptionHelper;

/**
 * SQLite dialect implementation for Hibernate.
 * Implementation based on https://github.com/gwenn/sqlite-dialect.
 */
public class SqliteDialect extends Dialect {
    private static final IdentityColumnSupportImpl identityImpl = new SqliteIdentity();
    private static final AbstractLimitHandler limitImpl = new SqliteLimits();
    private static final ViolatedConstraintNameExtracter constraintExtractorImpl =
        new SqliteConstraintNameExtractor();
    private final transient UniqueDelegate uniqueImpl;

    /**
     * Instantiates the {@link SqliteDialect} class.
     */
    public SqliteDialect() {
        uniqueImpl = new SqliteUniqueDelegate(this);

        registerColumnType(Types.BIT, "boolean");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARBINARY, "blob");
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return identityImpl;
    }

    @Override
    public LimitHandler getLimitHandler() {
        return limitImpl;
    }

    @Override
    public boolean supportsLockTimeouts() {
        return false;
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return "SELECT current_timestamp";
    }

    @Override
    public MultiTableBulkIdStrategy getDefaultMultiTableBulkIdStrategy() {
        return new LocalTemporaryTableBulkIdStrategy(new IdTableSupportStandardImpl() {
            @Override
            public String getCreateIdTableCommand() {
                return "CREATE TEMPORARY TABLE";
            }
        }, AfterUseAction.CLEAN, null);
    }

    @Override
    public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
        return (ex, msg, sql) -> {
            final int code = JdbcExceptionHelper.extractErrorCode(ex) & 0xFF;

            if (code == 0x12 || code == 0x14) {
                return new DataException(msg, ex, sql);
            } else if (code == 0x05 || code == 0x06) {
                return new LockAcquisitionException(msg, ex, sql);
            } else if ((code >= 0x0A && code <= 0x0F) || code == 0x1A) {
                return new JDBCConnectionException(msg, ex, sql);
            }

            return null;
        };
    }

    @Override
    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return constraintExtractorImpl;
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public boolean canCreateSchema() {
        return false;
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public boolean qualifyIndexName() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "ADD COLUMN";
    }

    @Override
    public String getDropForeignKeyString() {
        throw new UnsupportedOperationException(
            "No drop foreign key syntax supported by SQLiteDialect");
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName,
                                                   String[] foreignKey, String referencedTable,
                                                   String[] primaryKey,
                                                   boolean referencesPrimaryKey) {
        throw new UnsupportedOperationException(
            "No add foreign key syntax supported by SQLiteDialect");
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        throw new UnsupportedOperationException(
            "No add primary key syntax supported by SQLiteDialect");
    }

    @Override
    public boolean supportsCommentOn() {
        return true;
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return true;
    }

    @Override
    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
        return true;
    }

    @Override
    public boolean supportsTupleDistinctCounts() {
        return false;
    }

    @Override
    public int getInExpressionCountLimit() {
        return 1000;
    }

    @Override
    public UniqueDelegate getUniqueDelegate() {
        return uniqueImpl;
    }

    @Override
    public String getSelectGUIDString() {
        return "SELECT hex(randomblob(16))";
    }

    @Override
    public ScrollMode defaultScrollMode() {
        return ScrollMode.FORWARD_ONLY;
    }
}
