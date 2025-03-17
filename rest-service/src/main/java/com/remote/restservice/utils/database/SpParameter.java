package com.remote.restservice.utils.database;

import lombok.Builder;
import lombok.Data;

import java.sql.JDBCType;

@Data
@Builder
public class SpParameter {
    private String name;
    private Object value;

    /**
     * SQL 데이터 형식	    Java 데이터 형식
     * bit	                boolean
     * Tinyint	            short
     * Smallint	            short
     * Int	                int
     * Real	                float
     * Bigint	            long
     * float	            double
     * nchar(n)	            문자열
     * nvarchar(n)	        문자열
     * binary(n)	        byte[]
     * varbinary(n)	        byte[]
     * nvarchar(max)	    String
     * varbinary(max)	    byte[]
     * uniqueidentifier	    문자열
     * char(n)	            문자열	UTF8 문자열만 지원됨
     * varchar(n)	        문자열	UTF8 문자열만 지원됨
     * varchar(max)	        문자열	UTF8 문자열만 지원됨
     * date	                java.sql.date
     * numeric	            java.math.BigDecimal
     * decimal	            java.math.BigDecimal
     * money	            java.math.BigDecimal
     * smallmoney	        java.math.BigDecimal
     * smalldatetime	    java.sql.timestamp
     * Datetime	            java.sql.timestamp
     * datetime2	        java.sql.timestamp
     */
    private JDBCType jdbcType;

    private Direction direction;

    public enum Direction {
        IN,
        OUT,
        INOUT
    }

    public enum SpType {
        INIT,
        QUERY,
        FIND,
        FIND2,
        DELETE,
        INSERT,
        UPDATE,
        ALL
    }
}
