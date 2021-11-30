package nl.tudelft.sem11b.data.sqlite;


import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;

public class SqliteLimits extends AbstractLimitHandler {
    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean bindLimitParametersInReverseOrder() {
        return true;
    }

    @Override
    public String processSql(String sql, RowSelection selection) {
        return sql + (LimitHelper.hasFirstRow(selection) ? " LIMIT ? OFFSET ?" : " LIMIT ?");
    }
}
