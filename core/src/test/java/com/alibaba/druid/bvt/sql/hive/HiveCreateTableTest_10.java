/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.bvt.sql.hive;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;

import java.util.List;

public class HiveCreateTableTest_10 extends OracleTest {
    public void test_0() throws Exception {
        String sql = //
                "CREATE TABLE new_key_value_store\n" +
                        "   ROW FORMAT SERDE \"org.apache.hadoop.hive.serde2.columnar.ColumnarSerDe\"\n" +
                        "   STORED AS RCFile\n" +
                        "   AS\n" +
                        "SELECT (key % 1024) new_key, concat(key, value) key_value_pair\n" +
                        "FROM key_value_store\n" +
                        "SORT BY new_key, key_value_pair;\n"; //

        List<SQLStatement> statementList = SQLUtils.toStatementList(sql, JdbcConstants.HIVE);
        SQLStatement stmt = statementList.get(0);
        System.out.println(stmt.toString());

        Assert.assertEquals(1, statementList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.HIVE);
        stmt.accept(visitor);

        {
            String text = SQLUtils.toSQLString(stmt, JdbcConstants.HIVE);

            assertEquals("CREATE TABLE new_key_value_store\n" +
                    "ROW FORMAT\n" +
                    "\tSERDE 'org.apache.hadoop.hive.serde2.columnar.ColumnarSerDe'\n" +
                    "STORED AS RCFile\n" +
                    "AS\n" +
                    "SELECT (key % 1024) AS new_key, concat(key, value) AS key_value_pair\n" +
                    "FROM key_value_store\n" +
                    "SORT BY new_key, key_value_pair;", text);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(2, visitor.getTables().size());
        assertEquals(4, visitor.getColumns().size());
        assertEquals(0, visitor.getConditions().size());
        assertEquals(0, visitor.getRelationships().size());
        assertEquals(0, visitor.getOrderByColumns().size());

        assertTrue(visitor.containsTable("new_key_value_store"));

    }
}
