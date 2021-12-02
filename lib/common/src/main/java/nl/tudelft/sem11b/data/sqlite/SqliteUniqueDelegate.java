package nl.tudelft.sem11b.data.sqlite;

import java.util.Iterator;

import org.hibernate.boot.Metadata;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;

public class SqliteUniqueDelegate extends DefaultUniqueDelegate {
    public SqliteUniqueDelegate(Dialect d) {
        super(d);
    }

    @Override
    public String getColumnDefinitionUniquenessFragment(Column column) {
        return "";
    }

    @Override
    public String getTableCreationUniqueConstraintsFragment(Table table) {
        StringBuilder builder = new StringBuilder();
        Iterator<UniqueKey> iter = table.getUniqueKeyIterator();
        while (iter.hasNext()) {
            UniqueKey key = iter.next();
            builder.append(", ").append(uniqueConstraintSql(key));
        }

        return builder.toString();
    }

    @Override
    public String getAlterTableToAddUniqueKeyCommand(UniqueKey uniqueKey, Metadata metadata) {
        return "";
    }

    @Override
    public String getAlterTableToDropUniqueKeyCommand(UniqueKey uniqueKey, Metadata metadata) {
        return "";
    }
}
