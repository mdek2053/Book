package nl.tudelft.sem11b.data.sqlite;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SqliteIdentity extends IdentityColumnSupportImpl {
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return false;
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        return "SELECT last_insert_rowid()";
    }

    @Override
    public String getIdentityColumnString(int type) {
        return "INTEGER NOT NULL";
    }
}
