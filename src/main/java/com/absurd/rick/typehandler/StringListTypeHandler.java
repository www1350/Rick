package com.absurd.rick.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwenwei on 17/8/1.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class StringListTypeHandler extends BaseTypeHandler<List> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(",",parameter));
    }

    @Override
    public List getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String reStr = rs.getString(columnName);
        return converList(reStr);
    }

    private List converList(String reStr) {
        if (reStr == null) {
            return null;
        }
        List list = new ArrayList();
        String[] arr = reStr.split(",");
        for (String e:arr) {
            list.add(e);
        }
        return list;
    }

    @Override
    public List getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String reStr = rs.getString(columnIndex);
        return converList(reStr);
    }

    @Override
    public List getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String reStr = cs.getString(columnIndex);
        return converList(reStr);
    }
}
